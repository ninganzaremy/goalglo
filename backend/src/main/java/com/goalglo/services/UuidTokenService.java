package com.goalglo.services;

import com.goalglo.entities.EmailVerificationToken;
import com.goalglo.entities.User;
import com.goalglo.repositories.EmailVerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Service class for generating UUID tokens and handling token-related operations.
 */
@Service
public class UuidTokenService {

   private final EmailVerificationTokenRepository emailVerificationTokenRepository;

   public UuidTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
      this.emailVerificationTokenRepository = emailVerificationTokenRepository;
   }

   /**
    * Generates a UUID token for a given user and saves it in the EmailVerificationTokenRepository.
    *
    * @param user The user for whom the token is generated.
    * @return The generated token as a String.
    */
   public String generateUuidToken(User user) {
      EmailVerificationToken resetToken = new EmailVerificationToken();
      resetToken.setUser(user);
      String token = UUID.randomUUID().toString();
      resetToken.setToken(token);
      resetToken.setExpirationDate(
         LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault())
      );
      emailVerificationTokenRepository.save(resetToken);
      return token;
   }
}