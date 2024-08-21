package com.goalglo.backend.entities;

import com.goalglo.backend.dto.ServiceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "services")
public class ServiceEntity {

   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String description;

   @Column(nullable = false)
   private BigDecimal price;

   @Column(nullable = false)
   private Integer duration;  // duration in minutes

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt = LocalDateTime.now();

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
   public ServiceEntity(ServiceDTO serviceDTO) {
      this.name = serviceDTO.getName();
      this.description = serviceDTO.getDescription();
      this.price = serviceDTO.getPrice();
      this.duration = serviceDTO.getDuration();
   }
   public ServiceEntity() {
   }
}