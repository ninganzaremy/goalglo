package com.goalglo.services;

import com.goalglo.common.ResourceNotFoundException;
import com.goalglo.config.SecretConfig;
import com.goalglo.dto.TestimonialDTO;
import com.goalglo.entities.Testimonial;
import com.goalglo.entities.User;
import com.goalglo.repositories.TestimonialRepository;
import com.goalglo.tokens.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TestimonialService {
   private final TestimonialRepository testimonialRepository;
   private final SecretConfig secretConfig;
   private final JwtUtils jwtUtils;

   @Autowired
   public TestimonialService(TestimonialRepository testimonialRepository, JwtUtils jwtUtils, SecretConfig secretConfig) {
      this.testimonialRepository = testimonialRepository;
      this.jwtUtils = jwtUtils;
      this.secretConfig = secretConfig;
   }

   /**
    * Retrieves a list of approved testimonials.
    *
    * @return A list of TestimonialDTO objects representing the approved testimonials.
    */
   public List<TestimonialDTO> getApprovedTestimonials() {
      return testimonialRepository.findByStatus(Testimonial.TestimonialStatus.APPROVED)
         .stream()
         .map(TestimonialDTO::fromEntity)
         .collect(Collectors.toList());
   }

   /**
    * Creates a new testimonial for the authenticated user.
    *
    * @param authentication The authentication object to get the logged-in user.
    * @param testimonialDTO The DTO containing the testimonial data.
    * @return The created TestimonialDTO object.
    */
   public TestimonialDTO createTestimonial(Authentication authentication, TestimonialDTO testimonialDTO) {
      User user = getCurrentUser(authentication);

      Testimonial testimonial = Testimonial.builder()
         .user(user)
         .name(user.getFirstName())
         .text(testimonialDTO.getText())
         .status(Testimonial.TestimonialStatus.PENDING)
         .build();

      Testimonial savedTestimonial = testimonialRepository.save(testimonial);
      log.info("Created new testimonial with ID: {}", savedTestimonial.getId());
      return TestimonialDTO.fromEntity(savedTestimonial);
   }

   /**
    * Updates an existing testimonial for the authenticated user.
    *
    * @param authentication The authentication object to get the logged-in user.
    * @param id             The ID of the testimonial to update.
    * @param testimonialDTO The DTO containing the updated testimonial data.
    * @return The updated TestimonialDTO object.
    * @throws RuntimeException If the user is not the author of the testimonial or if the testimonial is not found.
    */
   public TestimonialDTO updateTestimonial(Authentication authentication, UUID id, TestimonialDTO testimonialDTO) {
      User currentUser = getCurrentUser(authentication);

      Testimonial testimonial = testimonialRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));


      if (!Objects.equals(currentUser.getId(), testimonial.getUser().getId())) {
         throw new RuntimeException("You have to be the author of the Testimony");
      }
      testimonial.setText(testimonialDTO.getText());
      testimonial.setStatus(Testimonial.TestimonialStatus.PENDING);

      Testimonial updatedTestimonial = testimonialRepository.save(testimonial);
      log.info("Updated testimonial with ID: {}", updatedTestimonial.getId());
      return TestimonialDTO.fromEntity(updatedTestimonial);
   }

   /**
    * Retrieves a list of all testimonials for authenticated users with the "Secured Role".
    *
    * @param authentication The authentication object to get the logged-in user.
    * @return A list of TestimonialDTO objects representing all testimonials.
    * @throws RuntimeException If the user does not have the "Secured Role".
    */
   public List<TestimonialDTO> getAllTestimonials(Authentication authentication) {
      User currentUser = getCurrentUser(authentication);
      if (currentUser.getRoles().stream().noneMatch(role -> secretConfig.getRoles().getSecuredRole().equals(role.getName()))) {
         throw new RuntimeException("User has not an Secured Role");
      }
      return testimonialRepository.findAll()
         .stream()
         .map(TestimonialDTO::fromEntity)
         .collect(Collectors.toList());
   }

   /**
    * Updates the status of a testimonial for authenticated users with the "Secured Role".
    *
    * @param authentication The authentication object to get the logged-in user.
    * @param id             The ID of the testimonial to update.
    * @param status         The new status for the testimonial.
    * @return The updated TestimonialDTO object.
    * @throws RuntimeException If the user does not have the "Secured Role" or if the testimonial is not found.
    */
   public TestimonialDTO updateTestimonialStatus(Authentication authentication, UUID id, Testimonial.TestimonialStatus status) {

      User currentUser = getCurrentUser(authentication);
      if (currentUser.getRoles().stream().noneMatch(role -> secretConfig.getRoles().getSecuredRole().equals(role.getName()))) {
         throw new RuntimeException("User has not an Secured Role");
      }
      Testimonial testimonial = testimonialRepository.findById(id)
         .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

      testimonial.setStatus(status);
      Testimonial updatedTestimonial = testimonialRepository.save(testimonial);
      log.info("Updated status of testimonial with ID: {} to {}", updatedTestimonial.getId(), status);
      return TestimonialDTO.fromEntity(updatedTestimonial);
   }

   /**
    * Retrieves the current user based on the authentication object.
    *
    * @param authentication The authentication object containing the current user's information.
    * @return The User object representing the current user.
    * @throws RuntimeException If the user is not found.
    */
   private User getCurrentUser(Authentication authentication) {
      return jwtUtils.getCurrentUser(authentication)
         .orElseThrow(() -> new RuntimeException("User not found"));
   }
}