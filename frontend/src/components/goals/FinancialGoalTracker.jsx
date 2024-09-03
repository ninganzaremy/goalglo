import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {addGoal, deleteGoal, fetchGoals, updateGoal,} from "../../redux/actions/goalActions";
import GoalForm from "./GoalForm.jsx";

/**
 * FinancialGoalTracker component
 * This component manages the financial goals for the user.
 * It fetches the goals from the Redux store, allows adding new goals,
 * updates existing goals, and deletes goals.
 * The component also handles loading and error states.
 */
const FinancialGoalTracker = () => {
   const dispatch = useDispatch();
   const {goals, loading, error} = useSelector((state) => state.goals);
   const [newGoal, setNewGoal] = useState({
      name: "",
      targetAmount: "",
      deadline: "",
   });

   useEffect(() => {
      dispatch(fetchGoals());
   }, [dispatch]);

   const handleAddGoal = (e) => {
      e.preventDefault();
      dispatch(addGoal(newGoal));
      setNewGoal({name: "", targetAmount: "", deadline: ""});
   };

   const handleUpdateGoal = (id, updatedGoal) => {
      dispatch(updateGoal(id, updatedGoal));
   };

   const handleDeleteGoal = (id) => {
      dispatch(deleteGoal(id));
   };

   if (loading) return <div className="loading">Loading goals...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="financial-goal-tracker">
         <h2>Financial Goals</h2>
         <GoalForm/>
         <ul className="goal-list">
            {goals.map((goal) => (
               <li key={goal.id} className="goal-item">
                  <span>
                     {goal.name} - ${goal.targetAmount} by{" "}
                     {new Date(goal.deadline).toLocaleDateString()}
                  </span>
                  <div className="goal-actions">
                     <button
                        onClick={() =>
                           handleUpdateGoal(goal.id, {
                              ...goal,
                              targetAmount: goal.targetAmount + 100,
                           })
                        }
                     >
                        Increase Target
                     </button>
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