package com.goalglo.backend.services;

import com.goalglo.backend.dto.ClientDTO;
import com.goalglo.backend.entities.Client;
import com.goalglo.backend.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

   private final ClientRepository clientRepository;

   @Autowired
   public ClientService(ClientRepository clientRepository) {
      this.clientRepository = clientRepository;
   }

   /**
    * Creates a new client.
    *
    * @param client The client entity to be created.
    * @return The created client entity.
    */
   public ClientDTO createClient(Client client) {
      Client createdClient = clientRepository.save(client);
      return new ClientDTO(createdClient);
   }

   /**
    * Finds a client by its UUID.
    *
    * @param clientId The UUID of the client.
    * @return An Optional containing the found client, or empty if not found.
    */
   public Optional<ClientDTO> findClientById(UUID clientId) {
      return clientRepository.findById(clientId).map(ClientDTO::new);
   }

   /**
    * Retrieves all clients.
    *
    * @return A list of all clients.
    */
   public List<ClientDTO> getAllClients() {
      return clientRepository.findAll().stream()
         .map(ClientDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Updates an existing client's information.
    *
    * @param clientId      The UUID of the client to update.
    * @param updatedClient The updated client information.
    * @return The updated client, or an empty Optional if the client was not found.
    */
   public Optional<ClientDTO> updateClient(UUID clientId, Client updatedClient) {
      return clientRepository.findById(clientId).map(client -> {
         client.setFirstName(updatedClient.getFirstName());
         client.setLastName(updatedClient.getLastName());
         client.setEmail(updatedClient.getEmail());
         client.setPhoneNumber(updatedClient.getPhoneNumber());
         client.setAddress(updatedClient.getAddress());
         return new ClientDTO(clientRepository.save(client));
      });
   }

   /**
    * Deletes a client by its UUID.
    *
    * @param clientId The UUID of the client to delete.
    * @return true if the client was deleted, false if the client was not found.
    */
   public boolean deleteClient(UUID clientId) {
      if (clientRepository.existsById(clientId)) {
         clientRepository.deleteById(clientId);
         return true;
      }
      return false;
   }

}