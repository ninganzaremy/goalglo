package com.goalglo.services;

import com.goalglo.dto.TransactionDTO;
import com.goalglo.entities.Transaction;
import com.goalglo.entities.User;
import com.goalglo.repositories.TransactionRepository;
import com.goalglo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final UserRepository userRepository;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
    this.transactionRepository = transactionRepository;
    this.userRepository = userRepository;
  }

  /**
   * Get transactions for a specific user
   */
  public Page<TransactionDTO> getUserTransactions(String username, Pageable pageable) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return transactionRepository.findByUserId(user.getId(), pageable)
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
  public List<TransactionDTO> getRecentTransactions(String username, int limit) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Pageable pageable = PageRequest.of(0, limit, Sort.by("transactionDate").descending());
    return transactionRepository.findByUserId(user.getId(), pageable)
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