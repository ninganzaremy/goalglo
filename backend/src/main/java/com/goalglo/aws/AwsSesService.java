package com.goalglo.aws;

import com.goalglo.util.SecretConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class AwsSesService {

   private final SecretConfig secretConfig;
   private final SesClient sesClient;

   /**
    * Constructor for AwsSesService.
    *
    * @param secretConfig The SecretConfig object containing AWS credentials and configuration.
    */
   public AwsSesService(SecretConfig secretConfig) {
      this.secretConfig = secretConfig;

      AwsBasicCredentials awsCreds = AwsBasicCredentials.create(secretConfig.getAccessKeyId(), secretConfig.getSecretAccessKey());

      this.sesClient = SesClient.builder()
         .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
         .region(Region.of(secretConfig.getAwsRegion()))
         .build();

   }

   /**
    * Sends an email using Amazon SES.
    *
    * @param to      The email address of the recipient.
    * @param subject The subject of the email.
    * @param body    The body of the email.
    */
   public void sendEmail(String to, String subject, String body) {
      SendEmailRequest emailRequest = SendEmailRequest.builder()
         .destination(Destination.builder().toAddresses(to).build())
         .message(Message.builder()
            .subject(Content.builder().data(subject).build())
            .body(Body.builder().text(Content.builder().data(body).build()).build())
            .build())
         .source(secretConfig.getAwsSesEmail())
         .build();

      sesClient.sendEmail(emailRequest);
   }
}