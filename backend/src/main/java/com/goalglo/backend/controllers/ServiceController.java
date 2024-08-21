package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.ServiceDTO;
import com.goalglo.backend.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

   private final ServiceService serviceService;

   @Autowired
   public ServiceController(ServiceService serviceService) {
      this.serviceService = serviceService;
   }

   /**
    * Creates a new service.
    *
    * @param serviceDTO The service data to create.
    * @return The created service.
    */
   @PostMapping
   public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
      return new ResponseEntity<>(serviceService.createService(serviceDTO), HttpStatus.CREATED);
   }


   /**
    * Retrieves a service by its ID.
    *
    * @param id The UUID of the service.
    * @return The service data if found.
    */
   @GetMapping("/{id}")
   public ResponseEntity<ServiceDTO> getServiceById(@PathVariable UUID id) {
      return serviceService.findServiceById(id)
         .map(serviceDTO -> new ResponseEntity<>(serviceDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Retrieves all services.
    *
    * @return A list of all services.
    */
   @GetMapping
   public ResponseEntity<List<ServiceDTO>> getAllServices() {
      return new ResponseEntity<>(serviceService.findAllServices(), HttpStatus.OK);
   }


   /**
    * Updates an existing service by its ID.
    *
    * @param id The UUID of the service.
    * @param serviceDTO The service data to update.
    * @return The updated service data if the update is successful.
    */
   @PutMapping("/{id}")
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
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
      boolean deleted = serviceService.deleteService(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }


}