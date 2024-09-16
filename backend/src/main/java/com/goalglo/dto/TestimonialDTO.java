package com.goalglo.dto;

import com.goalglo.entities.Testimonial;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestimonialDTO implements Serializable {
   private UUID id;
   private UUID userId;
   private String name;

   @NotBlank(message = "Text is required")
   @Size(max = 1000, message = "Text must be less than 1000 characters")
   private String text;

   private Testimonial.TestimonialStatus status;

   public static TestimonialDTO fromEntity(Testimonial testimonial) {
      return TestimonialDTO.builder()
         .id(testimonial.getId())
         .userId(testimonial.getUser().getId())
         .name(testimonial.getName())
         .text(testimonial.getText())
         .status(testimonial.getStatus())
         .build();
   }

}