package com.goalglo.services;

import com.goalglo.entities.ContactMessage;
import com.goalglo.repositories.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactMessageService {

   private final ContactMessageRepository contactMessageRepository;

   @Autowired
   public ContactMessageService(ContactMessageRepository contactMessageRepository) {
      this.contactMessageRepository = contactMessageRepository;
   }

   /**
    * Creates a new contact message.
    *
    * @param contactMessage The contact message to create.
    * @return The created contact message.
    */
   public ContactMessage createContactMessage(ContactMessage contactMessage) {
      return contactMessageRepository.save(contactMessage);
   }

   /**
    * Retrieves a contact message by its UUID.
    *
    * @param id The UUID of the contact message.
    * @return An Optional containing the contact message if found, or an empty Optional if not found.
    */
   public Optional<ContactMessage> findContactMessageById(UUID id) {
      return contactMessageRepository.findById(id);
   }

   /**
    * Retrieves all contact messages.
    *
    * @return A list of all contact messages.
    */
   public List<ContactMessage> findAllContactMessages() {
      return contactMessageRepository.findAll();
   }

   /**
    * Deletes a contact message by its UUID.
    *
    * @param id The UUID of the contact message to delete.
    * @return true if the contact message was deleted, false if not found.
    */
   public boolean deleteContactMessage(UUID id) {
      if (contactMessageRepository.existsById(id)) {
         contactMessageRepository.deleteById(id);
         return true;
      }
      return false;
   }
}