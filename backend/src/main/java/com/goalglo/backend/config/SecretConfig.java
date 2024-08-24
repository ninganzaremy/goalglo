package com.goalglo.backend.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
public class SecretConfig {

   @Value("${DEV_DOMAIN}")
   private String devDomain;

   @Value("${PROD_DOMAIN}")
   private String prodDomain;

   @Value("${spring.profiles.active}")
   private String activeProfile;

   @Value("${obfuscation.secured_role}")
   private String securedRole;

   @Value("${obfuscation.public_role}")
   private String publicRole;

   @Value("${obfuscation.prospect_role}")
   private String prospectRole;

   @Value("${jwt.secret}")
   private String jwtSecret;

   @Value("${AWS_ACCESS_KEY_ID}")
   private String accessKeyId;

   @Value("${AWS_SECRET_ACCESS_KEY}")
   private String secretAccessKey;

   @Value("${ECR_REGION}")
   private String region;

   @Value("${AWS_SES_EMAIL}")
   private String awsSesEmail;

   @Value("${password.reset.email.template.name}")
   private String passwordResetEmailTemplate;

   @Value("${thanks.email.template.name}")
   private String thanksEmailTemplate;

   @Value("${contact.response.email.template.name}")
   private String contactResponseEmailTemplate;

   @Value("${email.verification.email.template.name}")
   private String emailVerificationTemplate;

   @Bean
   public String getDomain() {
      return activeProfile.equalsIgnoreCase("dev") ? devDomain : prodDomain;
   }
}