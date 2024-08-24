package com.goalglo.backend.entities;

import com.goalglo.backend.dto.UserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false, unique = true)
   private String email;

   @Column(nullable = false, unique = true)
   private String username;

   @Column(nullable = false)
   private String password;

   @Column(nullable = false)
   private String firstName;

   @Column(nullable = false)
   private String lastName;

   @Column(nullable = false)
   private String phoneNumber;

   @Column
   private String address;


   @Column(nullable = false, columnDefinition = "boolean default false")
   private boolean emailVerified = false;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt = LocalDateTime.now();

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt = LocalDateTime.now();

   @ManyToMany(fetch = FetchType.EAGER)
   @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
   private Set<Role> roles = new HashSet<>();


   public User(UserDTO userDTO) {
      this.id = userDTO.getId();
      this.username = userDTO.getUsername();
      this.firstName = userDTO.getFirstName();
      this.lastName = userDTO.getLastName();
      this.email = userDTO.getEmail();
      this.phoneNumber = userDTO.getPhoneNumber();
      this.address = userDTO.getAddress();
      this.emailVerified = userDTO.isEmailVerified();
      this.password = userDTO.getPassword();
   }

   public User() {
   }

   // Add methods for adding/removing roles
   public void addRole(Role role) {
      this.roles.add(role);
      role.getUsers().add(this);
   }

   public void removeRole(Role role) {
      this.roles.remove(role);
      role.getUsers().remove(this);
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}