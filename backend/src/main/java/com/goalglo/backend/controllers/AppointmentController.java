package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.AppointmentDTO;
import com.goalglo.backend.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

   private final AppointmentService appointmentService;

   @Autowired
   public AppointmentController(AppointmentService appointmentService) {
      this.appointmentService = appointmentService;
   }

   /**
    * Creates a new appointment.
    *
    * @param appointment The appointment to be created.
    * @return A ResponseEntity containing the created appointment DTO and HTTP status CREATED.
    */
   @PostMapping
   public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
      AppointmentDTO createdAppointment = appointmentService.createAppointment(appointmentDTO);
      return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
   }

   /**
    * Retrieves an appointment by its ID.
    *
    * @param id The UUID of the appointment to retrieve.
    * @return A ResponseEntity containing the appointment DTO if found, or HTTP status NOT_FOUND if not.
    */
   @GetMapping("/{id}")
   public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable UUID id) {
      return appointmentService.findAppointmentById(id)
         .map(appointmentDTO -> new ResponseEntity<>(appointmentDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }


   /**
    * Retrieves all appointments associated with a specific client.
    *
    * @param clientId The UUID of the client whose appointments to retrieve.
    * @return A ResponseEntity containing a list of appointment DTOs and HTTP status OK.
    */
   @GetMapping("/client/{clientId}")
   public ResponseEntity<List<AppointmentDTO>> getAppointmentsByClientId(@PathVariable UUID clientId) {
      List<AppointmentDTO> appointments = appointmentService.findAppointmentsByClientId(clientId);
      return new ResponseEntity<>(appointments, HttpStatus.OK);
   }

   /**
    * Updates an existing appointment.
    *
    * @param id The UUID of the appointment to update.
    * @param appointment The updated appointment data.
    * @return A ResponseEntity containing the updated appointment DTO if successful, or HTTP status NOT_FOUND if the appointment doesn't exist.
    */
   @PutMapping("/{id}")
   public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentDTO appointmentDTO) {
      return appointmentService.updateAppointment(id, appointmentDTO)
         .map(updatedAppointment -> new ResponseEntity<>(updatedAppointment, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }


   /**
    * Deletes an appointment by its ID.
    *
    * @param id The UUID of the appointment to delete.
    * @return A ResponseEntity with HTTP status NO_CONTENT if deleted, or HTTP status NOT_FOUND if the appointment doesn't exist.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
      boolean deleted = appointmentService.deleteAppointment(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

}