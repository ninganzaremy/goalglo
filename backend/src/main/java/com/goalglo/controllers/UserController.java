package com.goalglo.controllers;

import com.goalglo.dto.UserDTO;
import com.goalglo.entities.User;
import com.goalglo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * REST controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

   private final UserService userService;

   /**
    * Constructs a new UserController with the specified UserService.
    *
    * @param userService the service to manage users
    */
   @Autowired
   public UserController(UserService userService) {
      this.userService = userService;
   }

   /**
    * Registers a new user in the system.
    *
    * @param user the user to be registered
    * @return a ResponseEntity containing the registered user DTO and HTTP status
    *         CREATED
    */
   @PostMapping("/register")
   public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
      UserDTO registeredUser = userService.registerUser(user);
      return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
   }

   /**
    * Verifies the user's email using the provided token.
    *
    * @param token The email verification token.
    * @return ResponseEntity indicating success or failure of verification.
    */
   @GetMapping("/verify-email")
   public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
      boolean isVerified = userService.verifyEmailToken(token);
      if (isVerified) {
         return ResponseEntity.ok("Email verified successfully.");
      } else {
         return ResponseEntity.badRequest().body("Invalid or expired token.");
      }
   }

   /**
    * Authenticates a user by checking their email and password.
    *
    * @param userDTO the user attempting to log in
    * @return a ResponseEntity containing the authenticated user DTO and HTTP
    *         status OK,
    *         or HTTP status UNAUTHORIZED if authentication fails
    */
   @PostMapping("/login")
   public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
      String trimmedIdentifier = userDTO.getIdentifier().trim();
      return userService.loginUser(trimmedIdentifier, userDTO.getPassword())
         .map(loggedInUserDTO -> new ResponseEntity<>(loggedInUserDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
   }

   /**
    * Retrieves a user by their ID.
    *
    * @param id the UUID of the user to retrieve
    * @return a ResponseEntity containing the user DTO and HTTP status OK,
    *         or HTTP status NOT FOUND if the user does not exist
    */
   @GetMapping("/{id}")
   public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
      return userService.findUserById(id)
         .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Updates an existing user's information.
    *
    * @param id   the UUID of the user to update
    * @param user the updated user information
    * @return a ResponseEntity containing the updated user DTO and HTTP status OK,
    *         or HTTP status NOT FOUND if the user does not exist
    */
   @PutMapping("/{id}")
   public ResponseEntity<UserDTO> updateUser(@PathVariable UUID id, @RequestBody User user) {
      return userService.updateUser(id, user)
         .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Deletes a user by their ID.
    *
    * @param id the UUID of the user to delete
    * @return a ResponseEntity with HTTP status NO_CONTENT if the user was deleted,
    *         or HTTP status NOT_FOUND if the user does not exist
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
      boolean deleted = userService.deleteUser(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   /**
    * Fetches user profile data.
    *
    * @param principal the authenticated user's principal
    * @return a ResponseEntity containing the user DTO and HTTP status OK
    */
   @GetMapping("/profile")
   public ResponseEntity<UserDTO> getUserProfile(Principal principal) {
      if (principal == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }
      UserDTO userDTO = userService.getUserProfile(principal.getName());
      return ResponseEntity.ok(userDTO);
   }
}