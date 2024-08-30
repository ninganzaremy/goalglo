package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.common.TokenCommons;
import com.goalglo.backend.config.SecretConfig;
import com.goalglo.backend.entities.EmailVerificationToken;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.EmailVerificationTokenRepository;
import com.goalglo.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class PasswordResetService {

   private final UserRepository userRepository;
   private final EmailVerificationTokenRepository emailVerificationTokenRepository;
   private final EmailTemplateService emailTemplateService;
   private final AwsSesEmailService awsSesEmailService;
   private final PasswordEncoder passwordEncoder;
   private final UuidTokenService uuidTokenService;
   private final SecretConfig secretConfig;


   @Autowired
   public PasswordResetService(UserRepository userRepository, EmailVerificationTokenRepository emailVerificationTokenRepository, EmailTemplateService emailTemplateService, AwsSesEmailService awsSesEmailService, PasswordEncoder passwordEncoder, UuidTokenService uuidTokenService, SecretConfig secretConfig) {
      this.userRepository = userRepository;
      this.emailVerificationTokenRepository = emailVerificationTokenRepository;
      this.emailTemplateService = emailTemplateService;
      this.awsSesEmailService = awsSesEmailService;
      this.passwordEncoder = passwordEncoder;
      this.uuidTokenService = uuidTokenService;
      this.secretConfig = secretConfig;
   }
   /**
    * Initiates the password reset process by generating a password reset token and sending a password reset email.
    *
    * @param email The email of the user who wants to reset their password.
    */
   public void initiatePasswordReset(String email) {
      User user = userRepository.findByEmail(email)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      // Generate and save password reset token
      String token = uuidTokenService.generateUuidToken(user);

      // Send password reset email
      sendPasswordResetEmail(user, token);
   }


   /**
    * Sends a password reset email to the user's registered email address.
    *
    * @param user  The user who requested the password reset.
    * @param token The password reset token generated for the user.
    */
   private void sendPasswordResetEmail(User user, String token) {
      String passwordResetEmailTemplate = secretConfig.getPasswordResetEmailTemplate();
      String domain = secretConfig.getActiveDomain();

      String subject = emailTemplateService.getSubjectByTemplateName(passwordResetEmailTemplate);
      String body = emailTemplateService.getBodyByTemplateName(passwordResetEmailTemplate)
         .replace("{reset_url}", domain + "/reset-password?token=" + token);

      awsSesEmailService.sendEmail(user.getEmail(), subject, body);
   }

   /**
    * Resets the user's password based on the provided token and new password.
    *
    * @param token       The password reset token.
    * @param newPassword The new password to be set.
    */
   public void resetPassword(String token, String newPassword) {
      EmailVerificationToken resetToken = emailVerificationTokenRepository.findByToken(token)
         .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

      if (resetToken.getExpirationDate().isBefore(LocalDateTime.now())) {
         throw new TokenCommons("Token has expired");
      }

      User user = resetToken.getUser();
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);

      emailVerificationTokenRepository.delete(resetToken);  // Token should not be reused
   }
}