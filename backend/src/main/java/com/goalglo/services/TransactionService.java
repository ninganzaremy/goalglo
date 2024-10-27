package com.goalglo.services;

import com.goalglo.dto.TransactionDTO;
import com.goalglo.entities.Transaction;
import com.goalglo.entities.User;
import com.goalglo.repositories.TransactionRepository;
import com.goalglo.tokens.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final JwtUtils jwtUtils;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository, JwtUtils jwtUtils) {
    this.transactionRepository = transactionRepository;
    this.jwtUtils = jwtUtils;
  }

  /**
   * Get transactions for a specific user
   */
  public Page<TransactionDTO> getUserTransactions(Authentication authentication, Pageable pageable) {
    User currentUser = jwtUtils.getCurrentUser(authentication).orElseThrow(() -> new RuntimeException("User not found"));

    return transactionRepository.findByUserId(currentUser.getId(), pageable)
        .map(this::convertToDTO);
  }

  /**
   * Get all transactions
   */
  public Page<TransactionDTO> getAllTransactions(Pageable pageable) {
    return transactionRepository.findAll(pageable)
        .map(this::convertToDTO);
  }

  /**
   * Get recent transactions for a specific user
   */
  public List<TransactionDTO> getRecentTransactions(Authentication authentication, int limit) {
    User currentUser = jwtUtils.getCurrentUser(authentication).orElseThrow(() -> new RuntimeException("User not found"));
    Pageable pageable = PageRequest.of(0, limit, Sort.by("transactionDate").descending());
    return transactionRepository.findByUserId(currentUser.getId(), pageable)
        .getContent()
        .stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  /**
   * Convert a Transaction entity to a TransactionDTO
   */
  private TransactionDTO convertToDTO(Transaction transaction) {
    TransactionDTO dto = new TransactionDTO();
    dto.setId(transaction.getId());
    dto.setUserId(transaction.getUser().getId());
    dto.setPaymentId(transaction.getPayment() != null ? transaction.getPayment().getId() : null);
    dto.setServiceId(transaction.getService() != null ? transaction.getService().getId() : null);
    dto.setAmount(transaction.getAmount());
    dto.setType(transaction.getType());
    dto.setDescription(transaction.getDescription());
    dto.setTransactionDate(transaction.getTransactionDate());
    return dto;
  }
}