package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.entities.EmailTemplate;
import com.goalglo.backend.repositories.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

   private final EmailTemplateRepository emailTemplateRepository;

   @Autowired
   public EmailTemplateService(EmailTemplateRepository emailTemplateRepository) {
      this.emailTemplateRepository = emailTemplateRepository;
   }

   /**
    * Retrieves the subject of an email template by its name.
    *
    * @param templateName The name of the email template.
    * @return The subject of the email template.
    */
   public String getSubjectByTemplateName(String templateName) {
      EmailTemplate template = emailTemplateRepository.findByTemplateName(templateName)
         .orElseThrow(() -> new ResourceNotFoundException("Email template not found"));
      return template.getSubject();
   }

   /**
    * Retrieves the body of an email template by its name.
    *
    * @param templateName The name of the email template.
    * @return The body of the email template.
    */
   public String getBodyByTemplateName(String templateName) {
      EmailTemplate template = emailTemplateRepository.findByTemplateName(templateName)
         .orElseThrow(() -> new ResourceNotFoundException("Email template not found"));
      return template.getBody();
   }
}