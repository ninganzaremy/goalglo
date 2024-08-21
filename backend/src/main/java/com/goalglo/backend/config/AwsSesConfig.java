package com.goalglo.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfig {

   private final SecreteConfig secreteConfig;

   public AwsSesConfig(SecreteConfig secreteConfig) {
      this.secreteConfig = secreteConfig;
   }


   @Bean
   public SesClient sesClient() {
      AwsBasicCredentials awsCreds = AwsBasicCredentials.create(secreteConfig.getAccessKeyId(), secreteConfig.getSecretAccessKey());

      return SesClient.builder()
         .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
         .region(Region.of(secreteConfig.getRegion()))
         .build();
   }
}