package com.goalglo.repositories;

import com.goalglo.entities.AccountSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountSummaryRepository extends JpaRepository<AccountSummary, UUID> {
  Optional<AccountSummary> findByUserId(UUID userId);
}