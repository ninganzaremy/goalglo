import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {addGoal, deleteGoal, fetchGoals, updateGoal,} from "../redux/actions/goalActions";

/**
 * useFinancialGoalsHook
 * @returns {{goals: Array, loading: boolean, error: string, handleAddGoal: function, handleUpdateGoal: function, handleDeleteGoal: function}}
 */
export const useFinancialGoalsHook = () => {
   const dispatch = useDispatch();
   const {goals, loading, error} = useSelector((state) => state.goals);

   useEffect(() => {
      dispatch(fetchGoals());
   }, [dispatch]);

   const handleAddGoal = (newGoal) => dispatch(addGoal(newGoal));
   const handleUpdateGoal = (updatedGoal) =>
      dispatch(updateGoal(updatedGoal.id, updatedGoal));
   const handleDeleteGoal = (id) => dispatch(deleteGoal(id));

   return {
      goals,
      loading,
      error,
      handleAddGoal,
      handleUpdateGoal,
      handleDeleteGoal,
   };
};