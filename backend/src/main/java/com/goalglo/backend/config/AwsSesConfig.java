package com.goalglo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfig {

   private final SecretConfig secretConfig;

   public AwsSesConfig(SecretConfig secretConfig) {
      this.secretConfig = secretConfig;
   }


   @Bean
   public SesClient sesClient() {
      AwsBasicCredentials awsCreds = AwsBasicCredentials.create(secretConfig.getAccessKeyId(), secretConfig.getSecretAccessKey());

      return SesClient.builder()
         .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
         .region(Region.of(secretConfig.getRegion()))
         .build();
   }
}