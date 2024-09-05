package com.goalglo.services;

import com.goalglo.util.SecretConfig;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class AwsSesEmailService {

   private final SesClient sesClient;
   private final SecretConfig secretConfig;

   public AwsSesEmailService(SesClient sesClient, SecretConfig secretConfig) {
      this.sesClient = sesClient;
      this.secretConfig = secretConfig;
   }

   /**
    * Sends an email using AWS SES.
    *
    * @param to      The recipient's email address.
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