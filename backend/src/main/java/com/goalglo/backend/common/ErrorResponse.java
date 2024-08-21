package com.goalglo.backend.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorResponse {

   private String message;
   private LocalDateTime timestamp;

   public ErrorResponse(String message) {
      this.message = message;
      this.timestamp = LocalDateTime.now();
   }

}