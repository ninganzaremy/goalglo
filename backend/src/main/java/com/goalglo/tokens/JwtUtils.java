package com.goalglo.tokens;

import com.goalglo.entities.Role;
import com.goalglo.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

   public Optional<User> getCurrentUser(Authentication authentication) {
      try {
         Jwt jwt = getJwtFromAuthentication(authentication);

         String userIdClaim = jwt.getClaim("userId");
         if (userIdClaim == null) {
            return Optional.empty();
         }

         User user = new User();
         user.setId(UUID.fromString(userIdClaim));
         user.setUsername(jwt.getSubject());
         user.setFirstName(jwt.getClaimAsString("firstName"));

         List<String> roleNames = jwt.getClaim("roles");
         if (roleNames != null) {
            Set<Role> roles = roleNames.stream()
               .map(name -> {
                  Role role = new Role();
                  role.setName(name);
                  return role;
               })
               .collect(Collectors.toSet());
            user.setRoles(roles);
         }

         return Optional.of(user);
      } catch (Exception e) {
         throw new IllegalStateException("User not found\"");
      }
   }

   private Jwt getJwtFromAuthentication(Authentication authentication) {
      if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
         throw new IllegalStateException("Invalid authentication object");
      }
      return (Jwt) authentication.getPrincipal();
   }
}