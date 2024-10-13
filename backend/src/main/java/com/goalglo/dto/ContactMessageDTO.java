package com.goalglo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ContactMessageDTO {

   private UUID id;
   private String name;
   private String email;
   private String subject;
   private String message;
   private LocalDateTime createdAt;
}