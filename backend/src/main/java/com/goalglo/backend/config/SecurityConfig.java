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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

   private final UserRepository userRepository;
   private final SecreteConfig secreteConfig;

   public SecurityConfig(UserRepository userRepository, SecreteConfig secreteConfig) {
      this.userRepository = userRepository;
      this.secreteConfig = secreteConfig;
   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
         .csrf(AbstractHttpConfigurer::disable)
         .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
            authorizationManagerRequestMatcherRegistry.requestMatchers(HttpMethod.DELETE).hasRole(secreteConfig.getSecuredRole())
               .requestMatchers("/api/admin-actions/**").hasAnyRole(secreteConfig.getSecuredRole())
               .requestMatchers("/user/**").hasAnyRole(secreteConfig.getPublicRole(), secreteConfig.getSecuredRole())
               .requestMatchers("/login", "/register").permitAll()
               .anyRequest().authenticated())
         .httpBasic(Customizer.withDefaults())
         .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
               .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
         )
         .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
         );

      return http.build();
   }

   @Bean
   CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of(secreteConfig.getDomain()));
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
      return username -> userRepository.findByUsername(username)
         .map(user -> User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRole())
            .build()
         )
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