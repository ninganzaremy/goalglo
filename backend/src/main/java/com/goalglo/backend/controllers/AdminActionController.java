package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.*;
import com.goalglo.backend.services.AdminActionService;
import com.goalglo.backend.services.ServiceService;
import com.goalglo.backend.services.TimeSlotService;
import com.goalglo.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin-actions")
public class AdminActionController {

   private final AdminActionService adminActionService;
   private final TimeSlotService timeSlotService;
   private final ServiceService serviceService;
   private final UserService userService;



   @Autowired
   public AdminActionController(AdminActionService adminActionService, TimeSlotService timeSlotService, ServiceService serviceService, UserService userService) {
      this.adminActionService = adminActionService;
      this.timeSlotService = timeSlotService;
      this.userService = userService;
      this.serviceService = serviceService;

   }

   /**
    * Adds a role to a user.
    *
    * @param userId   The UUID of the user to whom the role should be added.
    * @param roleName The name of the role to add.
    * @return A ResponseEntity containing the updated user and HTTP status OK.
    */
   @PostMapping("/add-role/{userId}/roles")
   public ResponseEntity<UserDTO> addRoleToUser(@PathVariable UUID userId, @RequestParam String roleName) {
      UserDTO updatedUser = userService.addRoleToUser(userId, roleName);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
   }

   /**
    * Removes a role from a user.
    *
    * @param userId   The UUID of the user from whom the role should be removed.
    * @param roleName The name of the role to remove.
    * @return A ResponseEntity containing the updated user and HTTP status OK.
    */
   @DeleteMapping("/remove-role/{userId}/roles")
   public ResponseEntity<UserDTO> removeRoleFromUser(@PathVariable UUID userId, @RequestParam String roleName) {
      UserDTO updatedUser = userService.removeRoleFromUser(userId, roleName);
      return new ResponseEntity<>(updatedUser, HttpStatus.OK);
   }
   /**
    * Retrieve all admin actions.
    *
    * @return A list of AdminActionDTOs.
    */
   @GetMapping("/get-all-actions")
   public ResponseEntity<List<AdminActionDTO>> getAllAdminActions() {
      List<AdminActionDTO> adminActions = adminActionService.getAllAdminActions();
      return new ResponseEntity<>(adminActions, HttpStatus.OK);
   }

   /**
    * Retrieve a specific admin action by ID.
    *
    * @param id The UUID of the admin action.
    * @return The AdminActionDTO if found.
    */
   @GetMapping("/get-action/{id}")
   public ResponseEntity<AdminActionDTO> getAdminActionById(@PathVariable UUID id) {
      return adminActionService.getAdminActionById(id)
         .map(actionDTO -> new ResponseEntity<>(actionDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Creates a new time slot.
    *
    * @param timeSlotDTO The DTO containing the time slot details.
    * @return The created TimeSlotDTO.
    */
   @PostMapping("/post-slot")
   public ResponseEntity<TimeSlotDTO> createTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
      TimeSlotDTO createdSlot = timeSlotService.createTimeSlot(timeSlotDTO);
      return new ResponseEntity<>(createdSlot, HttpStatus.CREATED);
   }

   /**
    * Batch creates time slots for a range.
    *
    * @param timeSlotsDTOs A list of TimeSlotDTOs for the slots to be created.
    * @return List of created TimeSlotDTOs.
    */
   @PostMapping("/post-slot/batch")
   public ResponseEntity<List<TimeSlotDTO>> createTimeSlotsBatch(@RequestBody List<TimeSlotDTO> timeSlotsDTOs) {
      List<TimeSlotDTO> createdSlots = timeSlotService.createTimeSlotsBatch(timeSlotsDTOs);
      return new ResponseEntity<>(createdSlots, HttpStatus.CREATED);
   }

   /**
    * Creates a new service.
    *
    * @param serviceDTO The service data to create.
    * @return The created service.
    */
   @PostMapping("/create-service")
   public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
      return new ResponseEntity<>(serviceService.createService(serviceDTO), HttpStatus.CREATED);
   }


   /**
    * Updates an existing service by its ID.
    *
    * @param id         The UUID of the service.
    * @param serviceDTO The service data to update.
    * @return The updated service data if the update is successful.
    */
   @PutMapping("/update-service/{id}")
   public ResponseEntity<ServiceDTO> updateService(@PathVariable UUID id, @RequestBody ServiceDTO serviceDTO) {
      return serviceService.updateService(id, serviceDTO)
         .map(updatedService -> new ResponseEntity<>(updatedService, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Deletes a service by its ID.
    *
    * @param id The UUID of the service.
    * @return A response indicating the result of the delete operation.
    */
   @DeleteMapping("/delete-service/{id}")
   public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
      boolean deleted = serviceService.deleteService(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   /**
    * Endpoint to handle bulk import of services and time slots.
    *
    * @param bulkImportDTO The DTO containing lists of services and time slots to be created.
    * @return A ResponseEntity indicating the result of the bulk import process.
    */
   @PostMapping("/service-time/bulk-import")
   public ResponseEntity<?> servicesAndTimesBulkImport(@RequestBody BulkImportDTO bulkImportDTO) {
      serviceService.bulkCreateServices(bulkImportDTO.getServices());
      timeSlotService.bulkCreateTimeSlots(bulkImportDTO.getTimeSlots());
      return ResponseEntity.ok("Bulk import completed successfully.");
   }

}