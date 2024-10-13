package com.goalglo.dto;

import com.goalglo.entities.AdminAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AdminActionDTO {
   private UUID id;
   private UUID adminId;
   private String actionType;
   private String actionDetails;


   public AdminActionDTO(AdminAction action) {
      this.id = action.getId();
      this.adminId = action.getAdmin().getId();
      this.actionType = action.getActionType();
      this.actionDetails = action.getActionDetails();
   }
}