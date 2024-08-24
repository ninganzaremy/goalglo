package com.goalglo.backend.dto;

import com.goalglo.backend.entities.Appointment;
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
public class AppointmentDTO {

   private UUID id;
   private UUID userId;
   private UUID serviceId;
   private UUID timeSlotId;
   private LocalDateTime startTime;
   private LocalDateTime endTime;
   private String status;
   private String notes;
   private String firstName;
   private String lastName;
   private String email;
   private String phoneNumber;
   private String address;

   public AppointmentDTO(Appointment appointment) {
      this.id = appointment.getId();
      this.userId = appointment.getUser().getId();
      this.serviceId = appointment.getService() != null ? appointment.getService().getId() : null;
      this.timeSlotId = appointment.getTimeSlot().getId();
      this.startTime = appointment.getTimeSlot().getStartTime();
      this.endTime = appointment.getTimeSlot().getEndTime();
      this.status = appointment.getStatus();
      this.notes = appointment.getNotes();

      this.firstName = appointment.getUser().getFirstName();
      this.lastName = appointment.getUser().getLastName();
      this.email = appointment.getUser().getEmail();
      this.phoneNumber = appointment.getUser().getPhoneNumber();
      this.address = appointment.getUser().getAddress();
   }
}