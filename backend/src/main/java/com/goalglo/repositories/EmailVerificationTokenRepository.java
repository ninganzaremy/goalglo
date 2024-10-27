package com.goalglo.repositories;

import com.goalglo.entities.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, UUID> {

   /**
    * Finds an email verification token by its token string.
    *
    * @param token The token string.
    * @return An Optional containing the found EmailVerificationToken or an empty Optional if not found.
    */
   Optional<EmailVerificationToken> findByToken(String token);

   /**
    * Deletes all expired tokens.
    *
    * @param expirationDate The expiration date to compare.
    */
   void deleteAllByExpirationDateBefore(LocalDateTime expirationDate);

   void deleteByToken(String token);

}