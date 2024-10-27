import {useState} from "react";
import GoalForm from "./GoalForm";
import {useFinancialGoalsHook} from "../../hooks/useFinancialGoalsHook.js";

/**
 * FinancialGoalTracker component
 * This component manages the financial goals for the user.
 * It fetches the goals from the Redux store, allows adding new goals,
 * updates existing goals, and deletes goals.
 * The component also handles loading and error states.
 */
const FinancialGoalTracker = () => {

   const {goals, loading, error, handleAddGoal, handleUpdateGoal, handleDeleteGoal} = useFinancialGoalsHook();
   const [editingGoal, setEditingGoal] = useState(null);

   const formatDate = (dateString) => {
      if (!dateString) return "No date set";
      const date = new Date(dateString);
      return isNaN(date.getTime()) ? "Invalid date" : date.toLocaleDateString();
   };

   if (loading) return <div className="loading">Loading goals...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="financial-goal-tracker">
         <h2>Financial Goals</h2>
         <GoalForm
            onSubmit={editingGoal ? handleUpdateGoal : handleAddGoal}
            initialGoal={editingGoal}
         />
         <ul className="goal-list">
            {goals.map((goal) => (
               <li key={goal.id} className="goal-item">
                  <span>
                     {goal.name} - ${goal.targetAmount} by{" "}
                     {formatDate(goal.deadline)}
                  </span>
                  <div className="goal-actions">
                     <button onClick={() => setEditingGoal(goal)}>Edit</button>
                     <button onClick={() => handleDeleteGoal(goal.id)}>
                        Delete
                     </button>
                  </div>
               </li>
            ))}
         </ul>
      </div>
   );
};

export default FinancialGoalTracker;