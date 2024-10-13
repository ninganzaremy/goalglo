package com.goalglo.controllers;

import com.goalglo.dto.GoalDTO;
import com.goalglo.services.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
     List<GoalDTO> goals = goalService.getUserGoals(authentication);
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
     GoalDTO createdGoal = goalService.createGoal(authentication, goalDTO);
     return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
  }

   /**
    * Updates an existing goal for the authenticated user.
    *
    * @param authentication The authentication object to get the logged-in user.
    * @param id             The UUID of the goal to update.
    * @param goalDTO        The GoalDTO object containing the updated goal details.
    * @return The updated GoalDTO object.
    */
   @PutMapping("/{id}")
   public ResponseEntity<GoalDTO> updateGoal(Authentication authentication, @PathVariable UUID id,
                                             @RequestBody GoalDTO goalDTO) {
      GoalDTO updatedGoal = goalService.updateGoal(authentication, id, goalDTO);
      return ResponseEntity.ok(updatedGoal);
   }

   /**
    * Deletes a goal for the authenticated user.
    *
    * @param authentication The authentication object to get the logged-in user.
    * @param id             The UUID of the goal to delete.
    * @return A ResponseEntity with no content, indicating the goal was deleted
    * successfully.
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteGoal(Authentication authentication, @PathVariable UUID id) {
      goalService.deleteGoal(authentication, id);
      return ResponseEntity.noContent().build();
  }
}