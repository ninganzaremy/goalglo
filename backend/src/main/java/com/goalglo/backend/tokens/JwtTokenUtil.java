package com.goalglo.backend.tokens;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
public class JwtTokenUtil {
   private final JwtEncoder jwtEncoder;

   public JwtTokenUtil(JwtEncoder jwtEncoder) {
      this.jwtEncoder = jwtEncoder;
   }

   public String generateJwtToken(String subject, Set<String> roles, long tokenValidity) {
      Instant now = Instant.now();
      JwtClaimsSet claimsSet = JwtClaimsSet.builder()
         .issuer("self") 
         .issuedAt(now)
         .expiresAt(now.plusSeconds(tokenValidity))
         .subject(subject)
         .claim("roles", roles)
         .build();
      JwsHeader jwsHeader = JwsHeader.with(() -> "HS512").type("JWT").build();
      return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
   }
}