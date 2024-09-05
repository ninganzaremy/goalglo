package com.goalglo.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Component
public class SecretConfig {
   private final Environment environment;

   @Value("${stripe.endpoint.secret}")
   private String stripeEndpointSecret;

   @Value("${DEV_DOMAIN}")
   private String devDomain;

   @Value("${PROD_DOMAIN}")
   private String prodDomain;

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
   @Value("${stripe.api.key}")
   private String stripeApiKey;

   public SecretConfig(Environment environment) {
      this.environment = environment;
   }

   public List<String> getAllowedDomains() {
      return Arrays.asList(devDomain, prodDomain);
   }

   public String getActiveDomain() {
      return "dev".equalsIgnoreCase(environment.getProperty("spring.profiles.active", "dev")) ? devDomain : prodDomain;
   }

}