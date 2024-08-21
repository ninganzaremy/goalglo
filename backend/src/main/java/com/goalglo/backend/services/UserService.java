package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.common.TokenCommons;
import com.goalglo.backend.config.SecreteConfig;
import com.goalglo.backend.dto.UserDTO;
import com.goalglo.backend.entities.EmailVerificationToken;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.EmailVerificationTokenRepository;
import com.goalglo.backend.repositories.UserRepository;
import com.goalglo.backend.tokens.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;


@Service
public class UserService {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final EmailVerificationTokenRepository emailVerificationTokenRepository;
   private final EmailTemplateService emailTemplateService;
   private final AwsSesEmailService awsSesEmailService;
   private final UuidTokenService uuidTokenService;
   private final SecreteConfig secreteConfig;
   private final JwtTokenUtil jwtTokenUtil;



   @Autowired
   public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, EmailVerificationTokenRepository emailVerificationTokenRepository, EmailTemplateService emailTemplateService, AwsSesEmailService awsSesEmailService, UuidTokenService uuidTokenService, SecreteConfig secreteConfig, JwtTokenUtil jwtTokenUtil) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailTemplateService = emailTemplateService;
      this.emailVerificationTokenRepository = emailVerificationTokenRepository;
      this.awsSesEmailService = awsSesEmailService;
      this.uuidTokenService = uuidTokenService;
      this.secreteConfig = secreteConfig;
      this.jwtTokenUtil = jwtTokenUtil;

   }
   /**
    * Registers a new user in the system by encoding their password and saving them in the repository.
    * Generates an email verification token and sends a verification email.
    *
    * @param user The user to be registered.
    * @return The registered user.
    */
   public UserDTO registerUser(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setEmailVerified(false);
      User savedUser = userRepository.save(user);
      String token = uuidTokenService.generateUuidToken(savedUser);
      sendVerificationEmail(savedUser, token);
      return new UserDTO(savedUser);
   }

   /**
    * Sends an email verification email to the user's registered email address.
    *
    * @param user  The user to whom the verification email is to be sent.
    * @param token The verification token generated for the user.
    */
   private void sendVerificationEmail(User user, String token) {
      String emailVerificationTemplate = secreteConfig.getEmailVerificationTemplate();
      String domain = secreteConfig.getDomain();

      String subject = emailTemplateService.getSubjectByTemplateName(emailVerificationTemplate);
      String body = emailTemplateService.getBodyByTemplateName(emailVerificationTemplate)
         .replace("{verificationUrl}", domain+"/api/users/verify-email?token=" + token);

      awsSesEmailService.sendEmail(user.getEmail(), subject, body);
   }
   /**
    * Verifies the user's email based on the provided token.
    *
    * @param token The email verification token.
    * @return
    */
   public boolean verifyEmailToken(String token) {
      EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
         .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired token"));

      if (verificationToken.getExpirationDate().isBefore(LocalDateTime.now())) {
         throw new TokenCommons("Token has expired");
      }

      User user = verificationToken.getUser();
      user.setEmailVerified(true);

      userRepository.save(user);
      emailVerificationTokenRepository.delete(verificationToken);

      return user.isEmailVerified();
   }
   /**
    * Logs in a user by checking their email or username and password.
    *
    * @param identifier The email or username of the user.
    * @param password   The password of the user.
    * @return An Optional containing the UserDTO with a token if the login is successful, or an empty Optional if not.
    */
   public Optional<UserDTO> loginUser(String identifier, String password) {
      Optional<User> userOpt = userRepository.findByEmail(identifier);

      if (userOpt.isEmpty()) {
         userOpt = userRepository.findByUsername(identifier);
      }

      if (userOpt.isPresent()) {
         User user = userOpt.get();
         if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
         }
         if (passwordEncoder.matches(password, user.getPassword())) {
            String jwtToken = jwtTokenUtil.generateJwtToken(user.getRole(), user.getUsername(), 3600);
            UserDTO userDTO = new UserDTO(user);
            userDTO.setToken(jwtToken);
            return Optional.of(userDTO);
         }
      }

      return Optional.empty();
   }



   /**
    * Finds a user by their UUID.
    *
    * @param userId The UUID of the user.
    * @return An Optional containing the user if found, or an empty Optional if not.
    */
   public Optional<UserDTO> findUserById(UUID userId) {
      return userRepository.findById(userId).map(UserDTO::new);
   }

   /**
    * Updates an existing user's information based on the provided fields in the updatedUser object.
    * Only the non-null and changed fields in updatedUser will be updated in the database.
    *
    * @param userId      The UUID of the user to update.
    * @param updatedUser The User object containing the updated fields. Fields that are null will not be updated.
    * @return An Optional containing the updated UserDTO if the user was found and updated, or an empty Optional if the user was not found.
    */
   public Optional<UserDTO> updateUser(UUID userId, User updatedUser) {
      return userRepository.findById(userId).map(user -> {
         boolean emailUpdated = updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail());

         updateFieldIfPresent(updatedUser::getEmail, user::setEmail);
         updateFieldIfPresent(updatedUser::getPassword, value -> user.setPassword(passwordEncoder.encode(value)));
         updateFieldIfPresent(updatedUser::getRole, user::setRole);

         if (emailUpdated) {
            user.setEmailVerified(false);
            String token = uuidTokenService.generateUuidToken(updatedUser);
            sendVerificationEmail(updatedUser, token);
         }

         return new UserDTO(userRepository.save(user));
      });
   }


   /**
    * Updates a field if the new value is non-null.
    *
    * @param getter The supplier function to get the new value.
    * @param setter The consumer function to set the value in the existing user.
    * @param <T>    The type of the field being updated.
    */
   private <T> void updateFieldIfPresent(Supplier<T> getter, Consumer<T> setter) {
      T value = getter.get();
      if (value != null) {
         setter.accept(value);
      }
   }

   /**
    * Deletes a user by their UUID.
    *
    * @param userId The UUID of the user to delete.
    * @return true if the user was deleted, false if the user was not found.
    */
   public boolean deleteUser(UUID userId) {
      if (userRepository.existsById(userId)) {
         userRepository.deleteById(userId);
         return true;
      }
      return false;
   }

}