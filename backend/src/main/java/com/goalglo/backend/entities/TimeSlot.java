package com.goalglo.backend.entities;

import com.goalglo.backend.dto.TimeSlotDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
@NoArgsConstructor
public class TimeSlot {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private UUID id;

   @Column(nullable = false)
   private LocalDateTime startTime;

   @Column(nullable = false)
   private LocalDateTime endTime;

   @Column(nullable = false)
   private boolean booked = false;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "appointment_id")
   private Appointment appointment;

   @OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
   private Set<Appointment> appointments = new HashSet<>();


   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;

   public TimeSlot(TimeSlotDTO timeSlotDTO) {
      this.startTime = timeSlotDTO.getStartTime();
      this.endTime = timeSlotDTO.getEndTime();
      this.booked = timeSlotDTO.isBooked();
   }
}