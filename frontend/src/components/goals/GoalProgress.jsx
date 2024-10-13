/*
 * GoalProgress component
 * Displays the progress of a goal, including the current amount, remaining amount, progress percentage, and days left until the target date.
 * @param {Object} goal - The goal object containing properties like name, currentAmount, targetAmount, and targetDate.
 * @returns {JSX.Element} The rendered GoalProgress component.
 */
const GoalProgress = ({ goal }) => {
   const progress = (goal.currentAmount / goal.targetAmount) * 100;
   const remainingAmount = goal.targetAmount - goal.currentAmount;
   const daysLeft = Math.ceil((new Date(goal.targetDate) - new Date()) / (1000 * 60 * 60 * 24));

   return (
      <div className="goal-progress">
         <h3>Goal Progress: {goal.name}</h3>
         <div className="progress-bar">
            <div className="progress" style={{ width: `${progress}%` }}></div>
         </div>
         <p>Progress: {progress.toFixed(2)}%</p>
         <p>Current Amount: ${goal.currentAmount}</p>
         <p>Remaining: ${remainingAmount}</p>
         <p>Days Left: {daysLeft}</p>
      </div>
   );
};

export default GoalProgress;