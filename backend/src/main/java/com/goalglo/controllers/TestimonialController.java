package com.goalglo.controllers;

import com.goalglo.dto.TestimonialDTO;
import com.goalglo.services.TestimonialService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/testimonials")
@Slf4j
public class TestimonialController {
   private final TestimonialService testimonialService;

   @Autowired
   public TestimonialController(TestimonialService testimonialService) {
      this.testimonialService = testimonialService;
   }

   /**
    * Retrieves a list of approved testimonials.
    *
    * @return a ResponseEntity containing a list of approved testimonials
    */
   @GetMapping
   public ResponseEntity<List<TestimonialDTO>> getApprovedTestimonials() {
      List<TestimonialDTO> testimonials = testimonialService.getApprovedTestimonials();
      return ResponseEntity.ok(testimonials);
   }

   /**
    * Creates a new testimonial.
    *
    * @param authentication the authenticated user
    * @param createDTO      the testimonial data to create
    * @return a ResponseEntity containing the created testimonial and HTTP status
    * CREATED
    */
   @PostMapping
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<TestimonialDTO> createTestimonial(Authentication authentication, @Valid @RequestBody TestimonialDTO createDTO) {
      TestimonialDTO createdTestimonial = testimonialService.createTestimonial(authentication, createDTO);
      return ResponseEntity.status(HttpStatus.CREATED).body(createdTestimonial);
   }

   /**
    * Updates an existing testimonial.
    *
    * @param authentication the authenticated user
    * @param id             the ID of the testimonial to update
    * @param updateDTO      the updated testimonial data
    * @return a ResponseEntity containing the updated testimonial and HTTP status
    * OK
    */
   @PutMapping("/{id}")
   @PreAuthorize("isAuthenticated()")
   public ResponseEntity<TestimonialDTO> updateTestimonial(Authentication authentication, @PathVariable UUID id, @Valid @RequestBody TestimonialDTO updateDTO) {
      TestimonialDTO updatedTestimonial = testimonialService.updateTestimonial(authentication, id, updateDTO);
      return ResponseEntity.ok(updatedTestimonial);
   }
}