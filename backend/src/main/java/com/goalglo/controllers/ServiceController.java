package com.goalglo.controllers;

import com.goalglo.dto.ServiceDTO;
import com.goalglo.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}