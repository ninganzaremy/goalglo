
/*
 * @fileoverview This file contains the GoalList component, which displays a list of financial goals
 * and provides functionality to select or delete a goal.
 */
const GoalList = ({ goals, onSelectGoal, onDeleteGoal }) => {
   return (
      <div className="goal-list">
         <h3>Your Financial Goals</h3>
         {goals.length === 0 ? (
            <p>You haven't set any goals yet. Start by adding a new goal!</p>
         ) : (
            <ul>
               {goals.map(goal => (
                  <li key={goal.id} onClick={() => onSelectGoal(goal)}>
                     <span className="goal-name">{goal.name}</span>
                     <span className="goal-amount">${goal.targetAmount}</span>
                     <span className="goal-date">{new Date(goal.targetDate).toLocaleDateString()}</span>
                     <button className="delete-btn" onClick={(e) => {
                        e.stopPropagation();
                        onDeleteGoal(goal.id);
                     }}>Delete</button>
                  </li>
               ))}
            </ul>
         )}
      </div>
   );
};

export default GoalList;