package com.goalglo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goalglo.backend.entities.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}