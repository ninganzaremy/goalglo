package com.goalglo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class SecretConfig {

   private Spring spring;
   private Domain domain;
   private Roles roles;
   private Jwt jwt;
   private Aws aws;
   private Email email;
   private Stripe stripe;
   private Contact contact;
   private ApiGateway apiGateway;

   public String getActiveDomain() {
      return domain.getAllowedDomain();
   }

   public boolean isProd() {
      return "prod".equalsIgnoreCase(spring.getProfiles().getActive());
   }


   public String getAwsBucketName() {
      return aws.getBucketName();
   }

   @Getter
   @Setter
   public static class Domain {
      private String allowedDomain;
   }

   @Getter
   @Setter
   public static class Roles {
      private String securedRole;
      private String publicRole;
      private String prospectRole;
   }

   @Getter
   @Setter
   public static class Jwt {
      private String secret;
   }

   @Getter
   @Setter
   public static class Aws {
      private String accessKeyId;
      private String secretAccessKey;
      private String region;
      private String bucketName;
      private Ses ses;

      @Getter
      @Setter
      public static class Ses {
         private String sourceEmail;
      }
   }

   @Getter
   @Setter
   public static class Email {
      private Templates templates;

      @Getter
      @Setter
      public static class Templates {
         private String passwordReset;
         private String thanks;
         private String contactResponse;
         private String emailVerification;
         private String bookingConfirmationEmail;

      }
   }

   @Getter
   @Setter
   public static class Stripe {
      private String apiKey;
      private String endpointSecret;
   }

   @Getter
   @Setter
   public static class Contact {
      private String companyEmail;
      private String companyPhoneNumber;
   }

   @Getter
   @Setter
   public static class Spring {
      private Profiles profiles;

      @Getter
      @Setter
      public static class Profiles {
         private String active;
      }
   }


   @Getter
   @Setter
   public static class ApiGateway {
      private String key;
   }

}