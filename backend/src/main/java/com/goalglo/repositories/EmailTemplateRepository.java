package com.goalglo.repositories;

import com.goalglo.entities.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplate , UUID> {

   /**
    * Finds an email template by its name.
    *
    * @param templateName The name of the email template.
    * @return An Optional containing the found EmailTemplate or an empty Optional if not found.
    */
   Optional<EmailTemplate> findByTemplateName(String templateName);
}