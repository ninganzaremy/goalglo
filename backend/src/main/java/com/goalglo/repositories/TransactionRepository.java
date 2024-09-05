package com.goalglo.repositories;

import com.goalglo.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
  Page<Transaction> findByUserId(UUID userId, Pageable pageable);

  Page<Transaction> findAll(Pageable pageable);
}