package com.goalglo.backend.dto;

import com.goalglo.backend.entities.Appointment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AppointmentDTO {

   private UUID id;
   private UUID clientId;
   private UUID serviceId;
   private LocalDateTime date;
   private String status;
   private String notes;
   public AppointmentDTO(Appointment appointment) {
      this.id = appointment.getId();
      this.clientId = appointment.getClient().getId();
      this.serviceId = appointment.getService() != null ? appointment.getService().getId() : null;
      this.date = appointment.getDate();
      this.status = appointment.getStatus();
      this.notes = appointment.getNotes();
   }
}