package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.AccountSummaryDTO;
import com.goalglo.backend.services.AccountSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account/summary")
public class AccountSummaryController {

  private final AccountSummaryService accountSummaryService;

  @Autowired
  public AccountSummaryController(AccountSummaryService accountSummaryService) {
    this.accountSummaryService = accountSummaryService;
  }

  /**
   * Retrieves the account summary for the currently authenticated user.
   *
   * @param authentication The authentication object to get the logged-in user.
   * @return The account summary DTO for the user.
   */
  @GetMapping
  public ResponseEntity<AccountSummaryDTO> getAccountSummary(Authentication authentication) {
    String username = authentication.getName();
    AccountSummaryDTO summary = accountSummaryService.getAccountSummary(username);
    return ResponseEntity.ok(summary);
  }
}