package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.ClientDTO;
import com.goalglo.backend.entities.Client;
import com.goalglo.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

   private final ClientService clientService;

   @Autowired
   public ClientController(ClientService clientService) {
      this.clientService = clientService;
   }

   /**
    * Creates a new client.
    *
    * @param client The client entity to be created.
    * @return The created client as a DTO.
    */
   @PostMapping
   public ResponseEntity<ClientDTO> createClient(@RequestBody Client client) {
      ClientDTO clientDTO = clientService.createClient(client);
      return new ResponseEntity<>(clientDTO, HttpStatus.CREATED);
   }

   /**
    * Retrieves a client by its UUID.
    *
    * @param id The UUID of the client.
    * @return The found client as a DTO, or a 404 status if not found.
    */
   @GetMapping("/{id}")
   public ResponseEntity<ClientDTO> getClientById(@PathVariable UUID id) {
      Optional<ClientDTO> clientDTO = clientService.findClientById(id);
      return clientDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Retrieves all clients.
    *
    * @return A list of all clients as DTOs.
    */
   @GetMapping
   public ResponseEntity<List<ClientDTO>> getAllClients() {
      List<ClientDTO> clients = clientService.getAllClients();
      return new ResponseEntity<>(clients, HttpStatus.OK);
   }

   /**
    * Updates an existing client by its UUID.
    *
    * @param id     The UUID of the client to update.
    * @param client The updated client entity.
    * @return The updated client as a DTO, or a 404 status if not found.
    */
   @PutMapping("/{id}")
   public ResponseEntity<ClientDTO> updateClient(@PathVariable UUID id, @RequestBody Client client) {
      Optional<ClientDTO> updatedClientDTO = clientService.updateClient(id, client);
      return updatedClientDTO.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }
   /**
    * Deletes a client by its UUID.
    *
    * @param id The UUID of the client to delete.
    * @return A 204 status if the client was deleted, or a 404 status if not found.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
      boolean deleted = clientService.deleteClient(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }
}