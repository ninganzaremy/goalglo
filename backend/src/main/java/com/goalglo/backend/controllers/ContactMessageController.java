package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.ContactMessageDTO;
import com.goalglo.backend.entities.ContactMessage;
import com.goalglo.backend.services.ContactMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contact-messages")
public class ContactMessageController {

   private final ContactMessageService contactMessageService;

   @Autowired
   public ContactMessageController(ContactMessageService contactMessageService) {
      this.contactMessageService = contactMessageService;
   }

   /**
    * Creates a new contact message.
    *
    * @param contactMessage The contact message to create.
    * @return The created contact message.
    */
   @PostMapping
   public ResponseEntity<ContactMessageDTO> createContactMessage(@RequestBody ContactMessage contactMessage) {
      ContactMessage createdMessage = contactMessageService.createContactMessage(contactMessage);
      return new ResponseEntity<>(convertToDTO(createdMessage), HttpStatus.CREATED);
   }

   /**
    * Retrieves a contact message by its UUID.
    *
    * @param id The UUID of the contact message.
    * @return The contact message if found, or a 404 status if not found.
    */
   @GetMapping("/{id}")
   public ResponseEntity<ContactMessageDTO> getContactMessageById(@PathVariable UUID id) {
      Optional<ContactMessage> contactMessage = contactMessageService.findContactMessageById(id);
      return contactMessage.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Retrieves all contact messages.
    *
    * @return A list of all contact messages.
    */
   @GetMapping
   public ResponseEntity<List<ContactMessageDTO>> getAllContactMessages() {
      List<ContactMessage> messages = contactMessageService.findAllContactMessages();
      List<ContactMessageDTO> messageDTOs = messages.stream().map(this::convertToDTO).collect(Collectors.toList());
      return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
   }

   /**
    * Deletes a contact message by its UUID.
    *
    * @param id The UUID of the contact message.
    * @return A 204 status if the contact message was deleted, or a 404 status if not found.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteContactMessage(@PathVariable UUID id) {
      boolean deleted = contactMessageService.deleteContactMessage(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   private ContactMessageDTO convertToDTO(ContactMessage contactMessage) {
      ContactMessageDTO contactMessageDTO = new ContactMessageDTO();
      contactMessageDTO.setId(contactMessage.getId());
      contactMessageDTO.setName(contactMessage.getName());
      contactMessageDTO.setEmail(contactMessage.getEmail());
      contactMessageDTO.setSubject(contactMessage.getSubject());
      contactMessageDTO.setMessage(contactMessage.getMessage());
      contactMessageDTO.setCreatedAt(contactMessage.getCreatedAt());
      return contactMessageDTO;
   }
}