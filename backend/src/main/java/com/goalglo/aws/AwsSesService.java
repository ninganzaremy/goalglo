package com.goalglo.aws;

import com.goalglo.util.SecretConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Configuration
public class AwsSesService {

   private final SecretConfig secretConfig;

   public AwsSesService(SecretConfig secretConfig) {
      this.secretConfig = secretConfig;
   }


   @Bean
   public software.amazon.awssdk.services.ses.SesClient sesClient() {
      AwsBasicCredentials awsCreds = AwsBasicCredentials.create(secretConfig.getAccessKeyId(), secretConfig.getSecretAccessKey());

      return software.amazon.awssdk.services.ses.SesClient.builder()
         .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
         .region(Region.of(secretConfig.getAwsRegion()))
         .build();
   }
}