package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.GoalDTO;
import com.goalglo.backend.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

  private final GoalService goalService;

  @Autowired
  public GoalController(GoalService goalService) {
    this.goalService = goalService;
  }

  /**
   * Retrieves all goals for the authenticated user.
   *
   * @param authentication The authentication object to get the logged-in user.
   * @return A list of GoalDTO objects representing the user's goals.
   */
  @GetMapping
  public ResponseEntity<List<GoalDTO>> getUserGoals(Authentication authentication) {
    String username = authentication.getName();
    List<GoalDTO> goals = goalService.getUserGoals(username);
    return ResponseEntity.ok(goals);
  }

  /**
   * Creates a new goal for the authenticated user.
   *
   * @param authentication The authentication object to get the logged-in user.
   * @param goalDTO        The GoalDTO object containing the goal details.
   * @return The created GoalDTO object.
   */
  @PostMapping
  public ResponseEntity<GoalDTO> createGoal(Authentication authentication, @RequestBody GoalDTO goalDTO) {
    String username = authentication.getName();
    GoalDTO createdGoal = goalService.createGoal(username, goalDTO);
    return ResponseEntity.ok(createdGoal);
  }
}