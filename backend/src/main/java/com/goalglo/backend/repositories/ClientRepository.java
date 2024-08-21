package com.goalglo.backend.repositories;

import com.goalglo.backend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for Client entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
   // Custom queries can be added here if needed
}