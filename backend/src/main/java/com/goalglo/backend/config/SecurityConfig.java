package com.goalglo.backend.config;

import com.goalglo.backend.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

   private final UserRepository userRepository;
   private final SecretConfig secretConfig;

   public SecurityConfig(UserRepository userRepository, SecretConfig secretConfig) {
      this.userRepository = userRepository;
      this.secretConfig = secretConfig;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .csrf(AbstractHttpConfigurer::disable)
         .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
            authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE).hasRole(secretConfig.getSecuredRole())
               .requestMatchers("/api/admin-actions/**").hasAnyAuthority(secretConfig.getSecuredRole())
               .requestMatchers("/user/**").hasAnyAuthority(secretConfig.getPublicRole(), secretConfig.getSecuredRole())
               .requestMatchers("/**").permitAll()
               .anyRequest().authenticated())
         .httpBasic(Customizer.withDefaults())
         .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
               .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
         )
         .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         )
         .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll());

      return http.build();
   }

   @Bean
   CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of(secretConfig.getDomain()));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
      configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }



   @Bean
   public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Bean
   public UserDetailsService userDetailsService() {
      return identifier -> userRepository.findByEmailOrUsername(identifier, identifier)
         .map(user -> {
            Set<GrantedAuthority> authorities = user.getRoles() != null
               ? user.getRoles().stream()
               .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
               .collect(Collectors.toSet())
               : new HashSet<>();

            return org.springframework.security.core.userdetails.User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .authorities(authorities)
               .build();
         })
         .orElseThrow(() -> new UsernameNotFoundException("User not found"));
   }

   @Bean
   public JwtAuthenticationConverter jwtAuthenticationConverter() {
      JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
      grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
      grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");

      JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
      jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
      return jwtAuthenticationConverter;
   }


}