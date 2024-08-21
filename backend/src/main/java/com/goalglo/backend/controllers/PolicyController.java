package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.PolicyDTO;
import com.goalglo.backend.services.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

   private final PolicyService policyService;

   @Autowired
   public PolicyController(PolicyService policyService) {
      this.policyService = policyService;
   }

   /**
    * Creates a new policy.
    *
    * @param policyDTO The policy data to create.
    * @return The created policy.
    */
   @PostMapping
   public ResponseEntity<PolicyDTO> createPolicy(@RequestBody PolicyDTO policyDTO) {
      PolicyDTO createdPolicy = policyService.createPolicy(policyDTO);
      return new ResponseEntity<>(createdPolicy, HttpStatus.CREATED);
   }
   /**
    * Retrieves a policy by its ID.
    *
    * @param id The UUID of the policy.
    * @return The policy data if found.
    */
   @GetMapping("/{id}")
   public ResponseEntity<PolicyDTO> getPolicyById(@PathVariable UUID id) {
      return policyService.findPolicyById(id)
         .map(policyDTO -> new ResponseEntity<>(policyDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Updates an existing policy by its ID.
    *
    * @param id The UUID of the policy.
    * @param policyDTO The policy data to update.
    * @return The updated policy data if the update is successful.
    */
   @PutMapping("/{id}")
   public ResponseEntity<PolicyDTO> updatePolicy(@PathVariable UUID id, @RequestBody PolicyDTO policyDTO) {
      return policyService.updatePolicy(id, policyDTO)
         .map(updatedPolicy -> new ResponseEntity<>(updatedPolicy, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Deletes a policy by its ID.
    *
    * @param id The UUID of the policy.
    * @return A response indicating the result of the delete operation.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deletePolicy(@PathVariable UUID id) {
      boolean deleted = policyService.deletePolicy(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }


   /**
    * Retrieves all policies by a client ID.
    *
    * @param clientId The UUID of the client.
    * @return A list of policies associated with the client.
    */
   @GetMapping("/client/{clientId}")
   public ResponseEntity<List<PolicyDTO>> getPoliciesByClientId(@PathVariable UUID clientId) {
      List<PolicyDTO> policies = policyService.findPoliciesByClientId(clientId);
      return new ResponseEntity<>(policies, HttpStatus.OK);
   }

}