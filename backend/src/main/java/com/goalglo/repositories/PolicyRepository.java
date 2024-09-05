package com.goalglo.repositories;

import com.goalglo.entities.Policy;
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
    * Finds all policies by a specific userId ID.
    *
    * @param userId The UUID of the userId.
    * @return A list of policies associated with the user.
    */
   List<Policy> findByUserId(UUID userId);

   /**
    * Finds a policy by its unique policy number.
    *
    * @param policyNumber The policy number.
    * @return The policy with the specified policy number.
    */
   Policy findByPolicyNumber(String policyNumber);
}