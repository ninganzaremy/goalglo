package com.goalglo.controllers;

import com.goalglo.dto.TestimonialDTO;
import com.goalglo.services.TestimonialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/testimonials")

@Slf4j
public class AdminTestimonialController {
   private final TestimonialService testimonialService;

   @Autowired
   public AdminTestimonialController(TestimonialService testimonialService) {
      this.testimonialService = testimonialService;
   }

   @GetMapping("/all")
   public ResponseEntity<List<TestimonialDTO>> getAllTestimonials(Authentication authentication) {
      List<TestimonialDTO> testimonials = testimonialService.getAllTestimonials(authentication);
      return ResponseEntity.ok(testimonials);
   }

   @PutMapping("/{id}/status")
   public ResponseEntity<TestimonialDTO> updateTestimonialStatus(Authentication authentication, @PathVariable UUID id, @RequestBody TestimonialDTO testimonialDTO) {
      TestimonialDTO updatedTestimonial = testimonialService.updateTestimonialStatus(authentication, id, testimonialDTO.getStatus());
      return ResponseEntity.ok(updatedTestimonial);
   }
}