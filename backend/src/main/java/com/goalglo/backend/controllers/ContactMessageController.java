package com.goalglo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.goalglo.backend.entities.ContactMessage;
import com.goalglo.backend.repositories.ContactMessageRepository;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

   private final ContactMessageRepository contactMessageRepository;

   @Autowired
   public ContactMessageController(ContactMessageRepository contactMessageRepository) {
      this.contactMessageRepository = contactMessageRepository;
   }

   @PostMapping
   public ContactMessage sendMessage(@RequestBody ContactMessage contactMessage) {
      return contactMessageRepository.save(contactMessage);
   }

   @GetMapping
   public List<ContactMessage> getAllMessages() {
      return contactMessageRepository.findAll();
   }
}