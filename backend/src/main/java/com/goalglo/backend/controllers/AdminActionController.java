package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.AdminActionDTO;
import com.goalglo.backend.services.AdminActionService;
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
@RequestMapping("/api/admin-actions")
public class AdminActionController {

   private final AdminActionService adminActionService;

   @Autowired
   public AdminActionController(AdminActionService adminActionService) {
      this.adminActionService = adminActionService;
   }

   /**
    * Retrieve all admin actions.
    *
    * @return A list of AdminActionDTOs.
    */
   @GetMapping
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
   @GetMapping("/{id}")
   public ResponseEntity<AdminActionDTO> getAdminActionById(@PathVariable UUID id) {
      return adminActionService.getAdminActionById(id)
         .map(actionDTO -> new ResponseEntity<>(actionDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }
}