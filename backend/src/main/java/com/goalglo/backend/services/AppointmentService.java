package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.dto.AppointmentDTO;
import com.goalglo.backend.entities.Appointment;
import com.goalglo.backend.entities.ServiceEntity;
import com.goalglo.backend.entities.TimeSlot;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.AppointmentRepository;
import com.goalglo.backend.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

   private final AppointmentRepository appointmentRepository;
   private final TimeSlotService timeSlotService;
   private final UserService userService;
   private final ServiceRepository serviceRepository;




   @Autowired
   public AppointmentService(AppointmentRepository appointmentRepository, TimeSlotService timeSlotService, UserService userService, ServiceRepository serviceRepository) {
      this.appointmentRepository = appointmentRepository;
      this.timeSlotService = timeSlotService;
      this.userService = userService;
      this.serviceRepository = serviceRepository;


   }

   /**
    * Creates a new appointment and books a time slot.
    *
    * @param appointmentDTO The DTO object containing the appointment details.
    * @param timeSlotId     The UUID of the time slot to be booked.
    * @param userDetails    The logged-in user's details, if available.
    * @return The created AppointmentDTO.
    */
   public AppointmentDTO bookAppointment(AppointmentDTO appointmentDTO, UUID timeSlotId, UserDetails userDetails) {
      // Check if the time slot is available
      TimeSlot timeSlot = timeSlotService.findTimeSlotById(timeSlotId)
         .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found"));

      if (timeSlot.isBooked()) {
         throw new IllegalStateException("Time slot is already booked");
      }

      User user;
      if (userDetails != null) {
         // Find the logged-in user
         user = userService.findByUsernameOrEmail(userDetails.getUsername());
      } else {
         // Handle non-logged-in users by finding or creating a user by email
         user = userService.findOrCreateUserByEmail(appointmentDTO.getEmail(), appointmentDTO);
      }

      // Create the appointment and link it to the user and time slot
      Appointment appointment = new Appointment(appointmentDTO);
      appointment.setUser(user);
      appointment.setTimeSlot(timeSlot);

      // Set the service entity if available in the DTO
      if (appointmentDTO.getServiceId() != null) {
         ServiceEntity service = serviceRepository.findById(appointmentDTO.getServiceId())
            .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
         appointment.setService(service);
      }

      // Save the appointment
      appointment = appointmentRepository.save(appointment);

      // Mark the time slot as booked
      timeSlotService.bookSlot(timeSlotId, appointment);

      return new AppointmentDTO(appointment);
   }


   /**
    * Finds an appointment by its UUID.
    *
    * @param id The UUID of the appointment to find.
    * @return An Optional containing the appointment if found, or an empty Optional if not.
    */
   public Optional<AppointmentDTO> findAppointmentById(UUID id) {
      return appointmentRepository.findById(id).map(AppointmentDTO::new);
   }
   /**
    * Finds all appointments associated with a specific user.
    *
    * @param userId The UUID of the user.
    * @return A list of appointments associated with the user.
    */
   public List<AppointmentDTO> findAppointmentsByUserId(UUID userId) {
      return appointmentRepository.findByUserId(userId).stream()
         .map(AppointmentDTO::new)
         .collect(Collectors.toList());
   }


   /**
    * Updates an existing appointment with new information.
    *
    * @param id                The UUID of the appointment to update.
    * @return An Optional containing the updated appointment if found and updated, or an empty Optional if not.
    */
   public Optional<AppointmentDTO> updateAppointment(UUID id, AppointmentDTO appointmentDTO) {
      return appointmentRepository.findById(id).map(existingAppointment -> {
         // Update the service if it's provided
         if (appointmentDTO.getServiceId() != null) {
            ServiceEntity service = serviceRepository.findById(appointmentDTO.getServiceId())
               .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
            existingAppointment.setService(service);
         }

         // Sync start and end times with the provided time slot, if available
         if (appointmentDTO.getTimeSlotId() != null) {
            TimeSlot timeSlot = timeSlotService.findTimeSlotById(appointmentDTO.getTimeSlotId())
               .orElseThrow(() -> new ResourceNotFoundException("TimeSlot not found"));
            existingAppointment.setTimeSlot(timeSlot);
         }

         // Update status, notes, etc., if provided
         if (appointmentDTO.getStatus() != null) {
            existingAppointment.setStatus(appointmentDTO.getStatus());
         }
         if (appointmentDTO.getNotes() != null) {
            existingAppointment.setNotes(appointmentDTO.getNotes());
         }

         // Save and return the updated appointment
         Appointment updatedAppointment = appointmentRepository.save(existingAppointment);
         return new AppointmentDTO(updatedAppointment);
      });
   }

   /**
    * Deletes an appointment by its UUID.
    *
    * @param id The UUID of the appointment to delete.
    * @return true if the appointment was deleted, false if the appointment was not found.
    */
   public boolean deleteAppointment(UUID id) {
      if (appointmentRepository.existsById(id)) {
         appointmentRepository.deleteById(id);
         return true;
      }
      return false;
   }
}