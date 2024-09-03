package com.goalglo.backend.repositories;

import com.goalglo.backend.entities.AccountSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountSummaryRepository extends JpaRepository<AccountSummary, UUID> {
  Optional<AccountSummary> findByUserId(UUID userId);
}