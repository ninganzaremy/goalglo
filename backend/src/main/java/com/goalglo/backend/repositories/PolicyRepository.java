package com.goalglo.backend.repositories;

import com.goalglo.backend.entities.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Policy entities.
 */
@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {

   /**
    * Finds all policies by a specific client ID.
    *
    * @param clientId The UUID of the client.
    * @return A list of policies associated with the client.
    */
   List<Policy> findByClientId(UUID clientId);

   /**
    * Finds a policy by its unique policy number.
    *
    * @param policyNumber The policy number.
    * @return The policy with the specified policy number.
    */
   Policy findByPolicyNumber(String policyNumber);
}