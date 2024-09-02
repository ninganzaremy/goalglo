import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {addGoal, deleteGoal, fetchGoals, updateGoal} from '../../redux/actions/goalActions';
import GoalList from './GoalList';
import GoalForm from './GoalForm';
import GoalProgress from './GoalProgress';

/**
 * Component for tracking financial goals.
 * @returns {JSX.Element}
 * @constructor
 */
const FinancialGoalTracker = () => {
   const dispatch = useDispatch();
   const { goals, loading, error } = useSelector(state => state.goals);
   const [selectedGoal, setSelectedGoal] = useState(null);

   useEffect(() => {
      dispatch(fetchGoals());
   }, [dispatch]);

   const handleAddGoal = (newGoal) => {
      dispatch(addGoal(newGoal));
   };

   const handleUpdateGoal = (updatedGoal) => {
      dispatch(updateGoal(updatedGoal));
      setSelectedGoal(null);
   };

   const handleDeleteGoal = (goalId) => {
      dispatch(deleteGoal(goalId));
      setSelectedGoal(null);
   };

   const handleSelectGoal = (goal) => {
      setSelectedGoal(goal);
   };

   if (loading) return <div className="loading">Loading goals...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="financial-goal-tracker">
         <h2>Financial Goal Tracker</h2>
         <div className="goal-tracker-content">
            <div className="goal-list-container">
               <GoalList
                  goals={goals}
                  onSelectGoal={handleSelectGoal}
                  onDeleteGoal={handleDeleteGoal}
               />
            </div>
            <div className="goal-form-container">
               <GoalForm
                  onSubmit={selectedGoal ? handleUpdateGoal : handleAddGoal}
                  initialGoal={selectedGoal}
               />
               {selectedGoal && (
                  <GoalProgress goal={selectedGoal} />
               )}
            </div>
         </div>
      </div>
   );
};

export default FinancialGoalTracker;