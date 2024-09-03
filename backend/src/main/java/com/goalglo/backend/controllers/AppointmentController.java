package com.goalglo.backend.controllers;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.dto.AppointmentDTO;
import com.goalglo.backend.dto.TimeSlotDTO;
import com.goalglo.backend.services.AppointmentService;
import com.goalglo.backend.services.TimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

   private final AppointmentService appointmentService;
   private final TimeSlotService timeSlotService;

   @Autowired
   public AppointmentController(AppointmentService appointmentService, TimeSlotService timeSlotService) {
      this.appointmentService = appointmentService;
      this.timeSlotService = timeSlotService;

   }

   /**
    * Books an appointment and assigns a time slot.
    *
    * @param appointmentDTO The DTO containing appointment details.
    * @param timeSlotId     The UUID of the time slot to be booked.
    * @param authentication The authentication object to get the logged-in user, if
    *                       any.
    * @return The created AppointmentDTO.
    */
   @PostMapping("/book-appointment/{timeSlotId}")
   public ResponseEntity<AppointmentDTO> bookAppointment(@RequestBody AppointmentDTO appointmentDTO,
                                                         @PathVariable UUID timeSlotId,
                                                         Authentication authentication) {
      String username = (authentication != null) ? authentication.getName() : null;
      AppointmentDTO createdAppointment = appointmentService.bookAppointment(appointmentDTO, timeSlotId, username);
      return new ResponseEntity<>(createdAppointment, HttpStatus.CREATED);
   }

   /**
    * Retrieves all appointments.
    *
    * @return A ResponseEntity containing a list of appointment DTOs and HTTP
    * status OK.
    */
   @GetMapping("/all")
   public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
      List<AppointmentDTO> appointments = appointmentService.findAllAppointments();
      return new ResponseEntity<>(appointments, HttpStatus.OK);
   }

   /**
    * Retrieves an appointment by its ID.
    *
    * @param id The UUID of the appointment to retrieve.
    * @return A ResponseEntity containing the appointment DTO if found, or HTTP
    *         status NOT_FOUND if not.
    */
   @GetMapping("/{id}")
   public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable UUID id) {
      return appointmentService.findAppointmentById(id)
         .map(appointmentDTO -> new ResponseEntity<>(appointmentDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Retrieves all appointments associated with a specific user.
    *
    * @param authentication The UUID of the user whose appointments to retrieve.
    * @return A ResponseEntity containing a list of appointment DTOs and HTTP
    *         status OK.
    */
   @GetMapping
   public ResponseEntity<List<AppointmentDTO>> getUserAppointments(Authentication authentication) {
      String username = authentication.getName();

      List<AppointmentDTO> appointments = appointmentService.findAppointmentsByAuthenticatedUser(username);
      return new ResponseEntity<>(appointments, HttpStatus.OK);
   }

   /**
    * Updates an existing appointment.
    *
    * @param id             The UUID of the appointment to update.
    * @param appointmentDTO The updated appointment data.
    * @return A ResponseEntity containing the updated appointment DTO if
    *         successful, or HTTP status NOT_FOUND if the appointment doesn't
    *         exist.
    */
   @PutMapping("/{id}")
   public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable UUID id,
                                                           @RequestBody AppointmentDTO appointmentDTO) {
      return appointmentService.updateAppointment(id, appointmentDTO)
         .map(updatedAppointment -> new ResponseEntity<>(updatedAppointment, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Deletes an appointment by its ID.
    *
    * @param id The UUID of the appointment to delete.
    * @return A ResponseEntity with HTTP status NO_CONTENT if deleted, or HTTP
    *         status NOT_FOUND if the appointment doesn't exist.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteAppointment(@PathVariable UUID id) {
      boolean deleted = appointmentService.deleteAppointment(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   /**
    * Cancels a booking for a specific time slot.
    *
    * @param slotId The UUID of the time slot to cancel the booking for.
    * @return A ResponseEntity with HTTP status NO_CONTENT if successful, or
    *         NOT_FOUND if the slot doesn't exist.
    */
   @DeleteMapping("/slots/{slotId}/cancel")
   public ResponseEntity<Void> cancelBooking(@PathVariable UUID slotId) {
      try {
         timeSlotService.cancelBooking(slotId);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      } catch (ResourceNotFoundException e) {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   /**
    * Retrieves all available time slots.
    *
    * @return A ResponseEntity containing a list of available TimeSlotDTOs and HTTP
    *         status OK.
    */
   @GetMapping("/slots/available")
   public ResponseEntity<List<TimeSlotDTO>> getAvailableSlots() {
      List<TimeSlotDTO> availableSlots = timeSlotService.getAvailableSlots();
      return new ResponseEntity<>(availableSlots, HttpStatus.OK);
   }

}