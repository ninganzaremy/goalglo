package com.goalglo.backend.services;

import com.goalglo.backend.dto.AppointmentDTO;
import com.goalglo.backend.entities.Appointment;
import com.goalglo.backend.repositories.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

   private final AppointmentRepository appointmentRepository;

   @Autowired
   public AppointmentService(AppointmentRepository appointmentRepository) {
      this.appointmentRepository = appointmentRepository;
   }

   /**
    * Creates a new appointment and saves it to the repository.
    *
    * @param appointment The appointment to be created.
    * @return The created appointment.
    */
   public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
      Appointment appointment = new Appointment(appointmentDTO);
      return new AppointmentDTO(appointmentRepository.save(appointment));
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
    * Finds all appointments associated with a specific client.
    *
    * @param clientId The UUID of the client.
    * @return A list of appointments associated with the client.
    */
   public List<AppointmentDTO> findAppointmentsByClientId(UUID clientId) {
      return appointmentRepository.findByClientId(clientId).stream()
         .map(AppointmentDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Updates an existing appointment with new information.
    *
    * @param id                The UUID of the appointment to update.
    * @param updatedAppointment The updated appointment information.
    * @return An Optional containing the updated appointment if found and updated, or an empty Optional if not.
    */
   public Optional<AppointmentDTO> updateAppointment(UUID id, AppointmentDTO appointmentDTO) {
      return appointmentRepository.findById(id).map(existingAppointment -> {
         existingAppointment.setDate(appointmentDTO.getDate());
         existingAppointment.setStatus(appointmentDTO.getStatus());
         existingAppointment.setNotes(appointmentDTO.getNotes());
         return new AppointmentDTO(appointmentRepository.save(existingAppointment));
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