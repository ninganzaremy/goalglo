package com.goalglo.services;

import com.goalglo.dto.GoalDTO;
import com.goalglo.entities.Goal;
import com.goalglo.entities.User;
import com.goalglo.repositories.GoalRepository;
import com.goalglo.tokens.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalService {

  private final GoalRepository goalRepository;
   private final JwtUtils jwtUtils;

  @Autowired
  public GoalService(GoalRepository goalRepository, JwtUtils jwtUtils) {
    this.goalRepository = goalRepository;
     this.jwtUtils = jwtUtils;
  }

  /**
   * Retrieves all goals for the current user.
   *
   * @param authentication The authentication object containing the current user's information.
   * @return A list of GoalDTO objects representing the user's goals.
   */
  public List<GoalDTO> getUserGoals(Authentication authentication) {

     UUID userId = getCurrentUser(authentication).getId();
     return goalRepository.findByUserId(userId).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  /**
   * Creates a new goal for the current user.
   *
   * @param authentication The authentication object containing the current user's information.
   * @param goalDTO        The GoalDTO object representing the new goal.
   * @return The GoalDTO object representing the created goal.
   */
  public GoalDTO createGoal(Authentication authentication, GoalDTO goalDTO) {

     UUID userId = getCurrentUser(authentication).getId();

     Goal goal = convertToEntity(goalDTO);
     User user = new User();
     user.setId(userId);
    goal.setUser(user);
    Goal savedGoal = goalRepository.save(goal);
    return convertToDTO(savedGoal);
  }

   /**
    * Updates an existing goal for the current user.
    *
    * @param authentication The authentication object containing the current user's information.
    * @param goalId         The UUID of the goal to update.
    * @param goalDTO        The GoalDTO object representing the updated goal.
    * @return The GoalDTO object representing the updated goal.
    */
   public GoalDTO updateGoal(Authentication authentication, UUID goalId, GoalDTO goalDTO) {

      UUID userId = getCurrentUser(authentication).getId();
      Goal existingGoal = goalRepository.findById(goalId)
         .orElseThrow(() -> new RuntimeException("Goal not found"));

      if (!existingGoal.getUser().getId().equals(userId)) {
         throw new RuntimeException("Not authorized to update this goal");
      }

      existingGoal.setName(goalDTO.getName());
      existingGoal.setTargetAmount(goalDTO.getTargetAmount());
      existingGoal.setCurrentAmount(goalDTO.getCurrentAmount());
      existingGoal.setDeadline(goalDTO.getDeadline());

      Goal updatedGoal = goalRepository.save(existingGoal);
      return convertToDTO(updatedGoal);
   }

   public void deleteGoal(Authentication authentication, UUID goalId) {
      UUID userId = getCurrentUser(authentication).getId();

      Goal goal = goalRepository.findById(goalId)
         .orElseThrow(() -> new RuntimeException("Goal not found"));

      if (!goal.getUser().getId().equals(userId)) {
         throw new RuntimeException("Not authorized to delete this goal");
      }

      goalRepository.delete(goal);
   }

   /**
    * Retrieves the current user based on the authentication object.
    *
    * @param authentication The authentication object containing the current user's information.
    * @return The User object representing the current user.
    * @throws RuntimeException If the user is not found.
    */
   private User getCurrentUser(Authentication authentication) {
      return jwtUtils.getCurrentUser(authentication)
         .orElseThrow(() -> new RuntimeException("User not found"));
   }

   /**
   * Converts a Goal entity to a GoalDTO object.
   *
   * @param goal The Goal entity to convert.
    * @return The GoalDTO object representing the goal.
   */
  private GoalDTO convertToDTO(Goal goal) {
    GoalDTO dto = new GoalDTO();
    dto.setId(goal.getId());
    dto.setName(goal.getName());
    dto.setTargetAmount(goal.getTargetAmount());
    dto.setCurrentAmount(goal.getCurrentAmount());
    dto.setDeadline(goal.getDeadline());
    return dto;
  }

  /**
   * Converts a GoalDTO object to a Goal entity.
   *
   * @param dto The GoalDTO object to convert.
   * @return The Goal entity representing the goal.
   */
  private Goal convertToEntity(GoalDTO dto) {
    Goal goal = new Goal();
    goal.setName(dto.getName());
    goal.setTargetAmount(dto.getTargetAmount());
    goal.setCurrentAmount(dto.getCurrentAmount());
    goal.setDeadline(dto.getDeadline());
    return goal;
  }
}