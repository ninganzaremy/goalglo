package com.goalglo.backend.dto;

import com.goalglo.backend.entities.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlotDTO {

   private UUID id;
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   private boolean booked;

   public TimeSlotDTO(TimeSlot timeSlot) {
      this.id = timeSlot.getId();
      this.startTime = timeSlot.getStartTime();
      this.endTime = timeSlot.getEndTime();
      this.booked = timeSlot.isBooked();
   }

}