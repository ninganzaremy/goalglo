package com.goalglo.services;

import com.goalglo.dto.GoalDTO;
import com.goalglo.entities.Goal;
import com.goalglo.entities.User;
import com.goalglo.repositories.GoalRepository;
import com.goalglo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoalService {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;

  @Autowired
  public GoalService(GoalRepository goalRepository, UserRepository userRepository) {
    this.goalRepository = goalRepository;
    this.userRepository = userRepository;
  }

  /**
   * Retrieves all goals for a given user.
   *
   * @param username The username of the user.
   * @return A list of GoalDTO objects representing the user's goals.
   */
  public List<GoalDTO> getUserGoals(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return goalRepository.findByUserId(user.getId()).stream()
        .map(this::convertToDTO)
        .collect(Collectors.toList());
  }

  /**
   * Creates a new goal for a user.
   *
   * @param username The username of the user.
   * @param goalDTO  The GoalDTO object representing the new goal.
   * @return The GoalDTO object representing the newly created goal.
   */
  public GoalDTO createGoal(String username, GoalDTO goalDTO) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    Goal goal = convertToEntity(goalDTO);
    goal.setUser(user);
    Goal savedGoal = goalRepository.save(goal);
    return convertToDTO(savedGoal);
  }

  /**
   * Converts a Goal entity to a GoalDTO object.
   *
   * @param goal The Goal entity to convert.
   * @return The corresponding GoalDTO object.
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
   * @return The corresponding Goal entity.
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