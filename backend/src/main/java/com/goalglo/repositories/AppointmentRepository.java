package com.goalglo.repositories;

import com.goalglo.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
   @Query("SELECT a FROM Appointment a JOIN FETCH a.timeSlot WHERE a.user.id = :userId")
   List<Appointment> findByUserId(@Param("userId") UUID userId);

   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.timeSlot LEFT JOIN FETCH a.user LEFT JOIN FETCH a.service")
   List<Appointment> findAllWithDetails();
}