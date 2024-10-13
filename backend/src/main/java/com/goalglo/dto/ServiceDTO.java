package com.goalglo.dto;

import com.goalglo.entities.ServiceEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ServiceDTO {

   private UUID id;
   private String name;
   private String description;
   private BigDecimal price;
   private Integer duration;
   // Constructor to initialize ServiceDTO using ServiceEntity
   public ServiceDTO(ServiceEntity serviceEntity) {
      this.id = serviceEntity.getId();
      this.name = serviceEntity.getName();
      this.description = serviceEntity.getDescription();
      this.price = serviceEntity.getPrice();
      this.duration = serviceEntity.getDuration();
   }

   // Default constructor
   public ServiceDTO() {
   }
}