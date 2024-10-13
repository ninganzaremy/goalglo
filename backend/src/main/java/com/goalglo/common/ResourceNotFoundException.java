package com.goalglo.common;

public class ResourceNotFoundException extends RuntimeException {
   public ResourceNotFoundException(String message) {
      super(message);
   }
}