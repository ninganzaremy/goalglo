package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.common.TokenCommons;
import com.goalglo.backend.config.SecretConfig;
import com.goalglo.backend.dto.AppointmentDTO;
import com.goalglo.backend.dto.UserDTO;
import com.goalglo.backend.entities.EmailVerificationToken;
import com.goalglo.backend.entities.Role;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.EmailVerificationTokenRepository;
import com.goalglo.backend.repositories.RoleRepository;
import com.goalglo.backend.repositories.UserRepository;
import com.goalglo.backend.tokens.JwtTokenUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class UserService {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final EmailVerificationTokenRepository emailVerificationTokenRepository;
   private final EmailTemplateService emailTemplateService;
   private final AwsSesEmailService awsSesEmailService;
   private final UuidTokenService uuidTokenService;
   private final SecretConfig secretConfig;
   private final JwtTokenUtil jwtTokenUtil;
   private final RoleRepository roleRepository;

   @Autowired
   public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
                      EmailVerificationTokenRepository emailVerificationTokenRepository, EmailTemplateService emailTemplateService,
                      AwsSesEmailService awsSesEmailService, UuidTokenService uuidTokenService, SecretConfig secretConfig,
                      JwtTokenUtil jwtTokenUtil, RoleRepository roleRepository) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.emailVerificationTokenRepository = emailVerificationTokenRepository;
      this.emailTemplateService = emailTemplateService;
      this.awsSesEmailService = awsSesEmailService;
      this.uuidTokenService = uuidTokenService;
      this.secretConfig = secretConfig;
      this.jwtTokenUtil = jwtTokenUtil;
      this.roleRepository = roleRepository;

   }

   /**
    * Registers a new user or upgrades an existing "PROSPECT" user to a fully
    * registered user.
    *
    * @param user The `User` object containing the details of the user to register
    *             or upgrade.
    *             If the user is an existing "PROSPECT", their account will be
    *             upgraded.
    *             Otherwise, a new account will be created.
    * @return A `UserDTO` containing the details of the registered or upgraded
    *         user.
    */
   public UserDTO registerUser(User user) {

      Role publicRole = roleRepository.findByName(secretConfig.getPublicRole())
         .orElseThrow(() -> new RuntimeException("Public role not found"));

      Optional<User> existingUserOpt = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());

      return existingUserOpt.map(existingUser -> {
         if (existingUser.getRoles().stream().anyMatch(role -> secretConfig.getProspectRole().equals(role.getName()))) {
            // Upgrade the existing "PROSPECT" to a registered user
            return processUserUpdate(existingUser, user, publicRole);
         } else {
            throw new IllegalArgumentException("A user with this email or username already exists.");
         }
      }).orElseGet(() -> {
         // Handle regular user registration
         user.addRole(publicRole);
         return processUserUpdate(user, user, publicRole);
      });
   }

   /**
    * Processes the update of a user's details, including setting the username,
    * password, and roles.
    *
    * @param existingUser The `User` entity representing the existing user in the
    *                     database.
    *                     This user will be updated with the new details.
    * @param newUser      The `User` object containing the new details to be
    *                     applied to the existing user.
    * @param publicRole   The `Role` entity representing the public role to be
    *                     assigned to the user.
    * @return A `UserDTO` containing the updated user details.
    */
   private UserDTO processUserUpdate(User existingUser, User newUser, Role publicRole) {
      existingUser.setUsername(newUser.getUsername());
      existingUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
      existingUser.setEmailVerified(false);
      existingUser.addRole(publicRole);

      User savedUser = userRepository.save(existingUser);
      sendVerificationEmail(savedUser, uuidTokenService.generateUuidToken(savedUser));
      return new UserDTO(savedUser);
   }

   /**
    * Sends an email verification email to the user's registered email address.
    *
    * @param user  The user to whom the verification email is to be sent.
    * @param token The verification token generated for the user.
    */
   private void sendVerificationEmail(User user, String token) {
      String emailVerificationTemplate = secretConfig.getEmailVerificationTemplate();
      String domain = secretConfig.getActiveDomain();

      String subject = emailTemplateService.getSubjectByTemplateName(emailVerificationTemplate);
      String body = emailTemplateService.getBodyByTemplateName(emailVerificationTemplate)
         .replace("{verificationUrl}", domain + "/verify-email?token=" + token);

      awsSesEmailService.sendEmail(user.getEmail(), subject, body);
   }

   /**
    * Verifies the user's email based on the provided token.
    *
    * @param token The email verification token.
    * @return boolean
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
    * @return An Optional containing the UserDTO with a token if the login is
    *         successful, or an empty Optional if not.
    */
   public Optional<UserDTO> loginUser(String identifier, String password) {
      Optional<User> userOpt = userRepository.findByEmailOrUsername(identifier, identifier);

      if (userOpt.isPresent()) {
         User user = userOpt.get();
         if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
         }
         if (passwordEncoder.matches(password, user.getPassword())) {
            String jwtToken = jwtTokenUtil.generateJwtToken(user,
               user.getRoles().stream()
                  .map(Role::getName)
                  .collect(Collectors.toSet()),
               3600);
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
    * @return An Optional containing the user if found, or an empty Optional if
    *         not.
    */
   public Optional<UserDTO> findUserById(UUID userId) {
      return userRepository.findById(userId).map(UserDTO::new);
   }

   /**
    * Updates an existing user's information based on the provided fields in the
    * updatedUser object.
    * Only the non-null and changed fields in updatedUser will be updated in the
    * database.
    *
    * @param userId      The UUID of the user to update.
    * @param updatedUser The User object containing the updated fields. Fields that
    *                    are null will not be updated.
    * @return An Optional containing the updated UserDTO if the user was found and
    *         updated, or an empty Optional if the user was not found.
    */
   public Optional<UserDTO> updateUser(UUID userId, User updatedUser) {
      return userRepository.findById(userId).map(user -> {
         boolean emailUpdated = updatedUser.getEmail() != null && !updatedUser.getEmail().equals(user.getEmail());

         updateFieldIfPresent(updatedUser::getEmail, user::setEmail);
         updateFieldIfPresent(updatedUser::getPassword, value -> user.setPassword(passwordEncoder.encode(value)));
         if (!updatedUser.getRoles().isEmpty()) {
            user.setRoles(updatedUser.getRoles());
         }
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

   /**
    * Finds a user by either their username or email.
    *
    * @param identifier The username or email to search for.
    * @return The user found by the given identifier.
    * @throws ResourceNotFoundException if no user is found with the provided
    *                                   identifier.
    */
   public User findByUsernameOrEmail(String identifier) {
      return userRepository.findByUsername(identifier)
         .or(() -> userRepository.findByEmail(identifier))
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));
   }

   /**
    * Finds a user by their email. If the user does not exist, creates a new user
    * with the provided details.
    *
    * @param email          The email of the user to find or create.
    * @param appointmentDTO The DTO containing the details for creating a new user
    *                       if one is not found.
    * @return The existing or newly created user.
    */
   public User findOrCreateUserByEmail(String email, AppointmentDTO appointmentDTO) {
      Role prospectRole = roleRepository.findByName(secretConfig.getProspectRole())
         .orElseThrow(() -> new RuntimeException("PROSPECT role not found"));

      return userRepository.findByEmail(email)
         .map(existingUser -> {
            if (existingUser.getRoles().stream().anyMatch(role -> secretConfig.getProspectRole().equals(role.getName()))) {
               // Update the existing PROSPECT user
               updateUserFromAppointmentDTO(existingUser, appointmentDTO);
               return userRepository.save(existingUser);
            }
            return existingUser;
         })
         .orElseGet(() -> createNewProspectUser(appointmentDTO, prospectRole));
   }

   /**
    * Creates a new user with the provided details and the "PROSPECT" role.
    *
    * @param appointmentDTO The DTO containing the details for the new user.
    * @param prospectRole   The "PROSPECT" role to be assigned to the new user.
    * @return The newly created user.
    */
   private User createNewProspectUser(AppointmentDTO appointmentDTO, Role prospectRole) {
      User newUser = new User();
      updateUserFromAppointmentDTO(newUser, appointmentDTO);
      newUser.addRole(prospectRole);
      newUser.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(10)));
      return userRepository.save(newUser);
   }

   /**
    * Updates a user's details based on the provided AppointmentDTO.
    *
    * @param user           The user to update.
    * @param appointmentDTO The DTO containing the new details for the user.
    */
   private void updateUserFromAppointmentDTO(User user, AppointmentDTO appointmentDTO) {
      user.setEmail(appointmentDTO.getEmail());
      user.setUsername(appointmentDTO.getEmail());
      user.setFirstName(appointmentDTO.getFirstName());
      user.setLastName(appointmentDTO.getLastName());
      user.setPhoneNumber(appointmentDTO.getPhoneNumber());
      user.setAddress(appointmentDTO.getAddress());
   }

   /**
    * Adds a role to a user.
    *
    * @param userId   The UUID of the user to whom the role should be added.
    * @param roleName The name of the role to add.
    * @return A UserDTO representing the updated user.
    * @throws ResourceNotFoundException if the user or role is not found.
    */
   public UserDTO addRoleToUser(UUID userId, String roleName) {
      User user = userRepository.findById(userId)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      Role role = roleRepository.findByName(roleName)
         .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

      user.getRoles().add(role);
      User updatedUser = userRepository.save(user);

      return new UserDTO(updatedUser);
   }

   /**
    * Removes a role from a user.
    *
    * @param userId   The UUID of the user from whom the role should be removed.
    * @param roleName The name of the role to remove.
    * @return A UserDTO representing the updated user.
    * @throws ResourceNotFoundException if the user or role is not found.
    */
   public UserDTO removeRoleFromUser(UUID userId, String roleName) {
      User user = userRepository.findById(userId)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      Role role = roleRepository.findByName(roleName)
         .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

      user.getRoles().remove(role);
      User updatedUser = userRepository.save(user);

      return new UserDTO(updatedUser);
   }

   public Optional<User> findById(UUID userId) {
      return userRepository.findById(userId);
   }

   public UserDTO getUserProfile(String identifier) {
      User user = userRepository.findByEmailOrUsername(identifier, identifier)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));
      return new UserDTO(user);
   }

}