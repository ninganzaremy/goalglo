package com.goalglo.backend.controllers;

import com.goalglo.backend.services.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {

   private final PasswordResetService passwordResetService;

   public PasswordResetController(PasswordResetService passwordResetService) {
      this.passwordResetService = passwordResetService;
   }

   /**
    * Initiates the password reset process by sending a reset email.
    *
    * @param email The email of the user who wants to reset their password.
    * @return ResponseEntity with HTTP status.
    */
   @PostMapping("/reset")
   public ResponseEntity<Void> initiatePasswordReset(@RequestParam String email) {
      passwordResetService.initiatePasswordReset(email);
      return new ResponseEntity<>(HttpStatus.OK);
   }

   /**
    * Resets the user's password using a provided token.
    *
    * @param token The password reset token.
    * @param newPassword The new password to set.
    * @return ResponseEntity with HTTP status.
    */
   @PostMapping("/reset/confirm")
   public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
      passwordResetService.resetPassword(token, newPassword);
      return new ResponseEntity<>(HttpStatus.OK);
   }
}