package com.goalglo.repositories;

import com.goalglo.entities.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, UUID> {

   /**
    * Finds all contact messages by email.
    *
    * @param email The email of the contact message sender.
    * @return A list of contact messages sent by the specified email.
    */
   List<ContactMessage> findByEmail(String email);

   /**
    * Finds all contact messages by subject.
    *
    * @param subject The subject of the contact message.
    * @return A list of contact messages with the specified subject.
    */
   List<ContactMessage> findBySubject(String subject);

   /**
    * Finds all contact messages by name.
    *
    * @param name The name of the contact message sender.
    * @return A list of contact messages sent by the specified name.
    */
   List<ContactMessage> findByName(String name);

   /**
    * Deletes all contact messages by email.
    *
    * @param email The email of the contact message sender.
    * @return The number of contact messages deleted.
    */
   int deleteByEmail(String email);

   /**
    * Deletes all contact messages by subject.
    *
    * @param subject The subject of the contact message.
    * @return The number of contact messages deleted.
    */
   int deleteBySubject(String subject);
}