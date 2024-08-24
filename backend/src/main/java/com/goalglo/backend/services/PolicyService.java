package com.goalglo.backend.services;

import com.goalglo.backend.dto.PolicyDTO;
import com.goalglo.backend.entities.Policy;
import com.goalglo.backend.repositories.PolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PolicyService {

   private final PolicyRepository policyRepository;

   @Autowired
   public PolicyService(PolicyRepository policyRepository) {
      this.policyRepository = policyRepository;
   }

   /**
    * Creates a new policy.
    *
    * @param policy The policy to be created.
    * @return The created policy.
    */
   public PolicyDTO createPolicy(PolicyDTO policyDTO) {
      Policy policy = new Policy(policyDTO);
      return new PolicyDTO(policyRepository.save(policy));
   }

   /**
    * Finds a policy by its ID.
    *
    * @param id The UUID of the policy.
    * @return An Optional containing the policy if found, or an empty Optional if not.
    */
   public Optional<PolicyDTO> findPolicyById(UUID id) {
      return policyRepository.findById(id).map(PolicyDTO::new);
   }

   /**
    * Updates an existing policy.
    *
    * @param id The UUID of the policy to update.
    * @param updatedPolicy The updated policy information.
    * @return The updated policy, or an empty Optional if the policy was not found.
    */
   public Optional<PolicyDTO> updatePolicy(UUID id, PolicyDTO policyDTO) {
      return policyRepository.findById(id).map(policy -> {
         policy = new Policy(policyDTO);
         return new PolicyDTO(policyRepository.save(policy));
      });
   }

   /**
    * Deletes a policy by its ID.
    *
    * @param id The UUID of the policy to delete.
    * @return true if the policy was deleted, false if the policy was not found.
    */
   public boolean deletePolicy(UUID id) {
      if (policyRepository.existsById(id)) {
         policyRepository.deleteById(id);
         return true;
      }
      return false;
   }

   /**
    * Finds policies by a user ID.
    *
    * @param userId The UUID of the user.
    * @return A list of policies associated with the user.
    */
   public List<PolicyDTO> findPoliciesByUserId(UUID userId) {
      return policyRepository.findByUserId(userId).stream()
         .map(PolicyDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Finds a policy by its unique policy number.
    *
    * @param policyNumber The policy number.
    * @return The policy with the specified policy number.
    */
   public Optional<Policy> findByPolicyNumber(String policyNumber) {
      return Optional.ofNullable(policyRepository.findByPolicyNumber(policyNumber));
   }

}