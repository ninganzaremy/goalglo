package com.goalglo.backend.entities;

import com.goalglo.backend.dto.AppointmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment {

   @Id
   @GeneratedValue
   private UUID id;

   @ManyToOne
   @JoinColumn(name = "client_id", nullable = false)
   private Client client;

   @ManyToOne
   @JoinColumn(name = "service_id", nullable = true)
   private ServiceEntity service;

   @Column(nullable = false)
   private LocalDateTime date;

   @Column(nullable = false)
   private String status;

   @Column(columnDefinition = "TEXT")
   private String notes;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;

   public Appointment(AppointmentDTO appointmentDTO) {
      this.date = appointmentDTO.getDate();
      this.status = appointmentDTO.getStatus();
      this.notes = appointmentDTO.getNotes();
   }
   public Appointment() {
   }
}