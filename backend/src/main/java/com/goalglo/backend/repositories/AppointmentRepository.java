package com.goalglo.backend.repositories;

import com.goalglo.backend.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
   List<Appointment> findByUserId(UUID userId);
}