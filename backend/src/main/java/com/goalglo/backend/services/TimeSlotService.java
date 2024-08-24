package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.dto.TimeSlotDTO;
import com.goalglo.backend.entities.Appointment;
import com.goalglo.backend.entities.TimeSlot;
import com.goalglo.backend.repositories.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TimeSlotService {

   private final TimeSlotRepository timeSlotRepository;

   @Autowired
   public TimeSlotService(TimeSlotRepository timeSlotRepository) {
      this.timeSlotRepository = timeSlotRepository;
   }

   /**
    * Retrieves all available time slots.
    *
    * @return List of available TimeSlotDTOs.
    */
   public List<TimeSlotDTO> getAvailableSlots() {
      return timeSlotRepository.findAllByBookedFalse()
         .stream()
         .map(TimeSlotDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Books a specific time slot for an appointment.
    *
    * @param slotId      The UUID of the time slot to be booked.
    * @param appointment The appointment to associate with the booked time slot.
    * @throws ResourceNotFoundException if the time slot is not found.
    */
   public void bookSlot(UUID slotId, Appointment appointment) {
      timeSlotRepository.findById(slotId).map(slot -> {
         if (!slot.isBooked()) {
            slot.setBooked(true);
            slot.setAppointment(appointment);
            timeSlotRepository.save(slot);
            return new TimeSlotDTO(slot);
         } else {
            return ("This slot is already booked");
         }
      }).orElseThrow(() -> new ResourceNotFoundException("Slot not found"));
   }

   /**
    * Cancels a booking for a specific time slot.
    *
    * @param slotId The UUID of the time slot to cancel the booking for.
    */
   public void cancelBooking(UUID slotId) {
      timeSlotRepository.findById(slotId).ifPresent(slot -> {
         slot.setBooked(false);
         slot.setAppointment(null);
         timeSlotRepository.save(slot);
      });
   }

   /**
    * Admin function to create available time slots.
    *
    * @param timeSlotDTO The DTO containing the time slot details.
    * @return The created TimeSlotDTO.
    */
   public TimeSlotDTO createTimeSlot(TimeSlotDTO timeSlotDTO) {
      TimeSlot timeSlot = new TimeSlot();
      timeSlot.setStartTime(timeSlotDTO.getStartTime());
      timeSlot.setEndTime(timeSlotDTO.getEndTime());
      return new TimeSlotDTO(timeSlotRepository.save(timeSlot));
   }

   /**
    * Batch creation of time slots.
    *
    * @param timeSlotsDTOs A list of TimeSlotDTOs for the slots to be created.
    * @return List of created TimeSlotDTOs.
    */
   public List<TimeSlotDTO> createTimeSlotsBatch(List<TimeSlotDTO> timeSlotsDTOs) {
      List<TimeSlot> slots = new ArrayList<>();
      for (TimeSlotDTO dto : timeSlotsDTOs) {
         TimeSlot slot = new TimeSlot();
         slot.setStartTime(dto.getStartTime());
         slot.setEndTime(dto.getEndTime());
         slots.add(timeSlotRepository.save(slot));
      }
      return slots.stream().map(TimeSlotDTO::new).collect(Collectors.toList());
   }


   public Optional<TimeSlot> findTimeSlotById(UUID timeSlotId) {
      return timeSlotRepository.findById(timeSlotId);
   }

   /**
    * Creates multiple time slots from the provided list of time slot DTOs.
    *
    * @param timeSlots A list of TimeSlotDTO objects to be converted into TimeSlot entities and saved in the database.
    */
   public void bulkCreateTimeSlots(List<TimeSlotDTO> timeSlots) {
      for (TimeSlotDTO timeSlotDTO : timeSlots) {
         TimeSlot timeSlot = new TimeSlot(timeSlotDTO);
         timeSlotRepository.save(timeSlot);
      }
   }
}