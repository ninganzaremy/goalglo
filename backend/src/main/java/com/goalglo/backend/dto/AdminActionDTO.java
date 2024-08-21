package com.goalglo.backend.dto;

import com.goalglo.backend.entities.AdminAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminActionDTO {
   private UUID id;
   private UUID adminId;
   private String actionType;
   private String actionDetails;
   private LocalDateTime createdAt;

   public AdminActionDTO(AdminAction action) {
      this.id = action.getId();
      this.adminId = action.getAdmin().getId();
      this.actionType = action.getActionType();
      this.actionDetails = action.getActionDetails();
      this.createdAt = action.getCreatedAt();
   }
}