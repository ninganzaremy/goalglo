package com.goalglo.services;

import com.goalglo.dto.ServiceDTO;
import com.goalglo.entities.ServiceEntity;
import com.goalglo.repositories.ServiceRepository;
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
   public List<ServiceDTO> getAllServices() {
      return serviceRepository.findAll().stream()
         .map(this::convertToDTO)
         .collect(Collectors.toList());
   }


   /**
    * Converts a ServiceEntity to a ServiceDTO.
    *
    * @param serviceEntity The ServiceEntity to be converted.
    * @return The converted ServiceDTO.
    */
   private ServiceDTO convertToDTO(ServiceEntity serviceEntity) {
      ServiceDTO dto = new ServiceDTO();
      dto.setId(serviceEntity.getId());
      dto.setName(serviceEntity.getName());
      dto.setDescription(serviceEntity.getDescription());
      return dto;
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

   /**
    * Creates multiple services from the provided list of service DTOs.
    *
    * @param services A list of ServiceDTO objects to be converted into Service entities and saved in the database.
    */
   public void bulkCreateServices(List<ServiceDTO> services) {
      for (ServiceDTO serviceDTO : services) {
         ServiceEntity service = new ServiceEntity(serviceDTO);
         serviceRepository.save(service);
      }
   }

   public Optional<ServiceEntity> findById(UUID serviceId) {
      return serviceRepository.findById(serviceId);
   }
}