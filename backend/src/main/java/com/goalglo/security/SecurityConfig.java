package com.goalglo.security;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.goalglo.repositories.UserRepository;
import com.goalglo.util.SecretConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

   private final UserRepository userRepository;
   private final SecretConfig secretConfig;
   private final LoadingCache<String, Integer> requestCountsPerIpAddress;


   public SecurityConfig(UserRepository userRepository, SecretConfig secretConfig, LoadingCache<String, Integer> requestCountsPerIpAddress) {
      this.userRepository = userRepository;
      this.secretConfig = secretConfig;
      this.requestCountsPerIpAddress = requestCountsPerIpAddress;

   }

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
      http
         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
         .csrf(AbstractHttpConfigurer::disable)
         .authorizeHttpRequests(authz -> authz
            // Authenticated endpoints
            .requestMatchers("/api/users/profile", "/api/transactions/user", "/api/appointments",
               "/api/transactions/recent")
            .authenticated()

            // Secured role endpoints
            .requestMatchers(HttpMethod.DELETE, "/api/appointments/all", "/api/transactions/all", "/api/admin-actions/**")
            .hasRole(secretConfig.getSecuredRole())

            // Mixed role endpoints
            .requestMatchers("/user/**", "/api/transactions/**")
            .hasAnyRole(secretConfig.getPublicRole(), secretConfig.getSecuredRole())

            // Allow all other requests
            .anyRequest().permitAll())
         .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
               .jwtAuthenticationConverter(jwtAuthenticationConverter())))
         .addFilterBefore(new JwtAuthenticationFilter(jwtDecoder), UsernamePasswordAuthenticationFilter.class)
         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
         .addFilterBefore(new SecurityRateLimitFilter(requestCountsPerIpAddress), UsernamePasswordAuthenticationFilter.class)
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
      configuration.setAllowedOrigins(secretConfig.getAllowedDomains());
      configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
      configuration.setAllowCredentials(true);
      configuration.setMaxAge(3600L);

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

            return User.builder()
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
      jwtAuthenticationConverter.setPrincipalClaimName("sub");
      return jwtAuthenticationConverter;
   }

}