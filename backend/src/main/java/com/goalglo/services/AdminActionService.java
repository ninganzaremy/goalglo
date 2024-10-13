package com.goalglo.services;

import com.goalglo.dto.AdminActionDTO;
import com.goalglo.entities.AdminAction;
import com.goalglo.entities.User;
import com.goalglo.repositories.AdminActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminActionService {

   private final AdminActionRepository adminActionRepository;

   @Autowired
   public AdminActionService(AdminActionRepository adminActionRepository) {
      this.adminActionRepository = adminActionRepository;
   }

   /**
    * Logs an administrative action.
    *
    * @param admin The admin user performing the action.
    * @param actionType The type of action performed.
    * @param actionDetails Additional details about the action.
    */
   public void logAction(User admin, String actionType, String actionDetails) {
      AdminAction action = new AdminAction();
      action.setAdmin(admin);
      action.setActionType(actionType);
      action.setActionDetails(actionDetails);
      adminActionRepository.save(action);
   }

   /**
    * Retrieves all admin actions from the database.
    *
    * @return A list of all admin actions.
    */
   public List<AdminActionDTO> getAllAdminActions() {
      return adminActionRepository.findAll().stream()
         .map(AdminActionDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Retrieves a specific admin action by its UUID.
    *
    * @param id The UUID of the admin action.
    * @return An Optional containing the admin action if found, or empty if not found.
    */
   public Optional<AdminActionDTO> getAdminActionById(UUID id) {
      return adminActionRepository.findById(id)
         .map(AdminActionDTO::new);
   }
}