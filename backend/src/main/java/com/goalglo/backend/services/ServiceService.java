package com.goalglo.backend.services;

import com.goalglo.backend.dto.ServiceDTO;
import com.goalglo.backend.entities.ServiceEntity;
import com.goalglo.backend.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ServiceService {

   private final ServiceRepository serviceRepository;

   @Autowired
   public ServiceService(ServiceRepository serviceRepository) {
      this.serviceRepository = serviceRepository;
   }

   /**
    * Creates a new service offering.
    *
    * @param serviceEntity The service entity to be created.
    * @return The created service entity.
    */
   public ServiceDTO createService(ServiceDTO serviceDTO) {
      ServiceEntity serviceEntity = new ServiceEntity(serviceDTO);
      return new ServiceDTO(serviceRepository.save(serviceEntity));
   }
   /**
    * Finds a service by its ID.
    *
    * @param id The UUID of the service.
    * @return An Optional containing the service entity if found, or an empty Optional if not.
    */
   public Optional<ServiceDTO> findServiceById(UUID id) {
      return serviceRepository.findById(id).map(ServiceDTO::new);
   }


   /**
    * Retrieves all services.
    *
    * @return A list of all service entities.
    */
   public List<ServiceDTO> findAllServices() {
      return serviceRepository.findAll().stream()
         .map(ServiceDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Updates an existing service.
    *
    * @param id The UUID of the service to update.
    * @param updatedService The updated service information.
    * @return The updated service entity, or an empty Optional if the service was not found.
    */
   public Optional<ServiceDTO> updateService(UUID id, ServiceDTO serviceDTO) {
      return serviceRepository.findById(id).map(service -> {
         service.setName(serviceDTO.getName());
         service.setDescription(serviceDTO.getDescription());
         service.setPrice(serviceDTO.getPrice());
         service.setDuration(serviceDTO.getDuration());
         return new ServiceDTO(serviceRepository.save(service));
      });
   }


   /**
    * Deletes a service by its ID.
    *
    * @param id The UUID of the service to delete.
    * @return true if the service was deleted, false if the service was not found.
    */
   public boolean deleteService(UUID id) {
      if (serviceRepository.existsById(id)) {
         serviceRepository.deleteById(id);
         return true;
      }
      return false;
   }

}