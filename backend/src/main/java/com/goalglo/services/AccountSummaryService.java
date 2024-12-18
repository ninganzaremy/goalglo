package com.goalglo.services;

import com.goalglo.dto.AccountSummaryDTO;
import com.goalglo.entities.AccountSummary;
import com.goalglo.entities.User;
import com.goalglo.repositories.AccountSummaryRepository;
import com.goalglo.tokens.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountSummaryService {

  private final AccountSummaryRepository accountSummaryRepository;
  private final JwtUtils jwtUtils;

  @Autowired
  public AccountSummaryService(AccountSummaryRepository accountSummaryRepository, JwtUtils jwtUtils) {
    this.accountSummaryRepository = accountSummaryRepository;
    this.jwtUtils = jwtUtils;
  }

  /**
   * Retrieves the account summary for the given username.
   *
   * @param username The username of the user.
   * @return The AccountSummaryDTO containing the account summary details.
   */
  public AccountSummaryDTO getAccountSummary(Authentication authentication) {
    User currentUser = jwtUtils.getCurrentUser(authentication).orElseThrow(() -> new RuntimeException("User not found"));

    AccountSummary summary = accountSummaryRepository.findByUserId(currentUser.getId())
       .orElseGet(() -> createDefaultAccountSummary(currentUser));
    return convertToDTO(summary);
  }

  /**
   * Creates a default account summary for the given user.
   *
   * @param user The user for whom the account summary is created.
   * @return The created AccountSummary instance.
   */
  private AccountSummary createDefaultAccountSummary(User user) {
    AccountSummary summary = new AccountSummary();
    summary.setUser(user);
    summary.setTotalBalance(BigDecimal.ZERO);
    summary.setTotalIncome(BigDecimal.ZERO);
    summary.setTotalExpenses(BigDecimal.ZERO);
    return accountSummaryRepository.save(summary);
  }

  /**
   * Converts an AccountSummary entity to an AccountSummaryDTO.
   *
   * @param summary The AccountSummary entity to be converted.
   * @return The corresponding AccountSummaryDTO.
   */
  private AccountSummaryDTO convertToDTO(AccountSummary summary) {
    AccountSummaryDTO dto = new AccountSummaryDTO();
    dto.setTotalBalance(summary.getTotalBalance());
    dto.setTotalIncome(summary.getTotalIncome());
    dto.setTotalExpenses(summary.getTotalExpenses());
    return dto;
  }
}