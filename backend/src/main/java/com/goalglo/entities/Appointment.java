package com.goalglo.entities;

import com.goalglo.dto.AppointmentDTO;
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
   @JoinColumn(name = "user_id", nullable = false)
   private User user;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "time_slot_id", nullable = false)
   private TimeSlot timeSlot;

   @ManyToOne
   @JoinColumn(name = "service_id", nullable = true)
   private ServiceEntity service;

   @Column(nullable = false)
   private String status = "Pending";

   @Column(columnDefinition = "TEXT")
   private String notes;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;

   @Column(nullable = false)
   private LocalDateTime startTime;

   @Column(nullable = false)
   private LocalDateTime endTime;

   public Appointment(AppointmentDTO appointmentDTO) {
      this.status = appointmentDTO.getStatus() != null ? appointmentDTO.getStatus() : "Pending";
      this.notes = appointmentDTO.getNotes();
   }

   @PrePersist
   @PreUpdate
   private void syncTimesWithTimeSlot() {
      if (this.timeSlot != null) {
         this.startTime = this.timeSlot.getStartTime();
         this.endTime = this.timeSlot.getEndTime();
      }
   }

   public Appointment() {
   }
}