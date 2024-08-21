package com.goalglo.backend.dto;

import com.goalglo.backend.entities.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ClientDTO {
   private UUID id;
   private UUID userId;
   private String firstName;
   private String lastName;
   private String email;
   private String phoneNumber;
   private String address;
   public ClientDTO(Client client) {
      this.id = client.getId();
      this.userId = client.getUser().getId();
      this.firstName = client.getFirstName();
      this.lastName = client.getLastName();
      this.email = client.getEmail();
      this.phoneNumber = client.getPhoneNumber();
      this.address = client.getAddress();
   }

}