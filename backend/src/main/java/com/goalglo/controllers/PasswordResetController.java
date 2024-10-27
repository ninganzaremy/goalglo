package com.goalglo.controllers;

import com.goalglo.dto.PasswordResetConfirmDTO;
import com.goalglo.dto.PasswordResetRequestDTO;
import com.goalglo.services.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

   private final PasswordResetService passwordResetService;

   public PasswordResetController(PasswordResetService passwordResetService) {
      this.passwordResetService = passwordResetService;
   }

   /**
    * Initiates the password reset process by generating a token and sending an email to the user.
    *
    * @param passwordResetRequestDTO The DTO containing the user's email.
    * @return ResponseEntity with HTTP status.
    */
   @PostMapping("/reset")
   public ResponseEntity<Void> initiatePasswordReset(@RequestBody PasswordResetRequestDTO passwordResetRequestDTO) {
      passwordResetService.initiatePasswordReset(passwordResetRequestDTO);
      return new ResponseEntity<>(HttpStatus.OK);
   }

   /**
    * Confirms the password reset process by validating the token and resetting the user's password.
    *
    * @param passwordResetConfirmDTO The DTO containing the token and new password.
    * @return ResponseEntity with HTTP status.
    */
   @PostMapping("/reset/confirm")
   public ResponseEntity<Void> resetPassword(@RequestBody PasswordResetConfirmDTO passwordResetConfirmDTO) {
      passwordResetService.resetPassword(passwordResetConfirmDTO);
      return new ResponseEntity<>(HttpStatus.OK);
   }
}