package com.goalglo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO for bulk import of services and time slots.
 */
@Getter
@Setter
@NoArgsConstructor
public class BulkImportDTO {

   /**
    * A list of services to be imported.
    */
   private List<ServiceDTO> services;

   /**
    * A list of time slots to be imported.
    */
   private List<TimeSlotDTO> timeSlots;

}