package com.goalglo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.goalglo.backend.entities.Appointment;
import com.goalglo.backend.repositories.AppointmentRepository;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

   private final AppointmentRepository appointmentRepository;

   @Autowired
   public AppointmentController(AppointmentRepository appointmentRepository) {
      this.appointmentRepository = appointmentRepository;
   }

   @GetMapping
   public List<Appointment> getAllAppointments() {
      return appointmentRepository.findAll();
   }

   @GetMapping("/{id}")
   public Appointment getAppointmentById(@PathVariable Long id) {
      return appointmentRepository.findById(id).orElse(null);
   }

   @PostMapping
   public Appointment createAppointment(@RequestBody Appointment appointment) {
      return appointmentRepository.save(appointment);
   }

   @PutMapping("/{id}")
   public Appointment updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
      Appointment existingAppointment = appointmentRepository.findById(id).orElse(null);
      if (existingAppointment != null) {
         existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
         existingAppointment.setDetails(appointment.getDetails());
         return appointmentRepository.save(existingAppointment);
      }
      return null;
   }

   @DeleteMapping("/{id}")
   public void cancelAppointment(@PathVariable Long id) {
      appointmentRepository.deleteById(id);
   }
}