package com.goalglo.controllers;

import com.goalglo.dto.TransactionDTO;
import com.goalglo.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /**
   * Retrieves transactions for the authenticated user.
   *
   * @param authentication The authentication object to get the logged-in user.
   * @param pageable       The pagination information.
   * @return A ResponseEntity containing a Page of TransactionDTO objects.
   */
  @GetMapping("/user")
  public ResponseEntity<Page<TransactionDTO>> getUserTransactions(Authentication authentication, Pageable pageable) {

    Page<TransactionDTO> transactions = transactionService.getUserTransactions(authentication, pageable);
    return ResponseEntity.ok(transactions);
  }

  /**
   * Retrieves all transactions.
   *
   * @param pageable The pagination information.
   * @return A ResponseEntity containing a Page of TransactionDTO objects.
   */
  @GetMapping("/all")
  public ResponseEntity<Page<TransactionDTO>> getAllTransactions(Pageable pageable) {
    Page<TransactionDTO> transactions = transactionService.getAllTransactions(pageable);
    return ResponseEntity.ok(transactions);
  }

  /**
   * Retrieves the most recent transactions for the authenticated user.
   *
   * @param authentication The authentication object to get the logged-in user.
   * @param limit          The maximum number of transactions to retrieve.
   * @return A ResponseEntity containing a list of TransactionDTO objects.
   */
  @GetMapping("/recent")
  public ResponseEntity<List<TransactionDTO>> getRecentTransactions(
      Authentication authentication,
      @RequestParam(defaultValue = "10") int limit) {

    List<TransactionDTO> recentTransactions = transactionService.getRecentTransactions(authentication, limit);
    return ResponseEntity.ok(recentTransactions);
  }
}