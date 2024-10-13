import {useEffect, useState} from "react";

/*
GoalForm component is responsible for rendering a form to add or edit a goal.
It takes two props:
1. onSubmit: a function to be called when the form is submitted
2. initialGoal: an optional object representing the initial values of the goal form

The component uses the useState hook to manage the state of the goal object.
When the initialGoal prop is provided, the state is initialized with the values of the initialGoal object.
The handleChange function updates the state with the values from the form inputs.
The handleSubmit function is called when the form is submitted, it calls the onSubmit prop with the current state of the goal object.
The form inputs are controlled by the state, and their values are reset after the form is submitted if initialGoal is not provided.

The component renders a form with input fields for the goal name, target amount, current amount, and target date.
It also includes a submit button that triggers the handleSubmit function.
 * GoalForm component
 * @param {function} onSubmit - Function to be called when the form is submitted
 * @param {object} initialGoal - Optional object representing the initial values of the goal form
 * @returns {JSX.Element} - Rendered GoalForm component
 */
const GoalForm = ({ onSubmit, initialGoal }) => {
   const [goal, setGoal] = useState({
      name: "",
      targetAmount: "",
      currentAmount: "",
      deadline: "",
   });

   useEffect(() => {
      if (initialGoal) {
         setGoal(initialGoal);
      }
   }, [initialGoal]);

   const handleChange = (e) => {
      const { name, value } = e.target;
      setGoal((prevGoal) => ({...prevGoal, [name]: value}));
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      onSubmit(goal);
      if (!initialGoal) {
         setGoal({
            name: "",
            targetAmount: "",
            currentAmount: "",
            deadline: "",
         });
      }
   };

   return (
      <form onSubmit={handleSubmit}>
         <div className="form-group">
            <label htmlFor="name">Goal Name</label>
            <input
               type="text"
               id="name"
               name="name"
               value={goal.name}
               onChange={handleChange}
               required
            />
         </div>
         <div className="form-group">
            <label htmlFor="targetAmount">Target Amount</label>
            <input
               type="number"
               id="targetAmount"
               name="targetAmount"
               value={goal.targetAmount}
               onChange={handleChange}
               required
            />
         </div>
         <div className="form-group">
            <label htmlFor="currentAmount">Current Amount</label>
            <input
               type="number"
               id="currentAmount"
               name="currentAmount"
               value={goal.currentAmount}
               onChange={handleChange}
               required
            />
         </div>
         <div className="form-group">
            <label htmlFor="deadline">Target Date</label>
            <input
               type="date"
               id="deadline"
               name="deadline"
               value={goal.deadline}
               onChange={handleChange}
               required
            />
         </div>
         <button type="submit" className="btn-primary">
            {initialGoal ? "Update Goal" : "Add Goal"}
         </button>
      </form>
   );
};

export default GoalForm;