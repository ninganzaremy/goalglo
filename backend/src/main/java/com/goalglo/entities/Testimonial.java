package com.goalglo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "testimonials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Testimonial implements Serializable {

   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false, length = 1000)
   private String text;

   @Column(nullable = false)
   @Enumerated(EnumType.STRING)
   private TestimonialStatus status;

   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @UpdateTimestamp
   @Column(nullable = false)
   private LocalDateTime updatedAt;

   public enum TestimonialStatus {
      PENDING, APPROVED, HIDDEN
   }
}