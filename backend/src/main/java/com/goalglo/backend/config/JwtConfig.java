package com.goalglo.backend.config;


import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtConfig {

   private final SecreteConfig secreteConfig;

   public JwtConfig(SecreteConfig secreteConfig) {
      this.secreteConfig = secreteConfig;
   }

   @Bean
   public JwtEncoder jwtEncoder() {
      SecretKey key = new SecretKeySpec(secreteConfig.getJwtSecret().getBytes(), "HmacSHA512");
      JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(key);
      return new NimbusJwtEncoder(immutableSecret);
   }

   @Bean
   public JwtDecoder jwtDecoder() {
      return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secreteConfig.getJwtSecret().getBytes(), "HmacSHA512")).build();
   }

}