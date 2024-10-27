package com.goalglo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtDecoder jwtDecoder;

  public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
    this.jwtDecoder = jwtDecoder;
  }

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
                                  @NonNull HttpServletResponse response,
                                  @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = extractJwtFromRequest(request);
    if (token != null) {
      try {
        Jwt jwt = jwtDecoder.decode(token);
        String username = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");

        if (username != null) {
          // Convert roles to GrantedAuthority objects
          Collection<GrantedAuthority> authorities = roles.stream()
             .map(SimpleGrantedAuthority::new)
             .collect(Collectors.toList());

          // Create authentication token with username and roles
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
             username, null, authorities);

          // Set the authentication in the security context
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      } catch (JwtException e) {
        // Token is invalid, do nothing and continue the filter chain
      }
    }
    filterChain.doFilter(request, response);
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}