package com.goalglo.backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false, unique = true)
   private String name;

   @ManyToMany(mappedBy = "roles")
   private Set<User> users = new HashSet<>();

   public void addUser(User user) {
      this.users.add(user);
      user.getRoles().add(this);
   }

   public void removeUser(User user) {
      this.users.remove(user);
      user.getRoles().remove(this);
   }
}