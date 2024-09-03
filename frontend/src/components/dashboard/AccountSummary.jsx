import { useDispatch, useSelector } from "react-redux";
import { fetchAccountSummary } from "../../redux/actions/accountSummaryActions";
import { useEffect } from "react";

/**
 * AccountSummary component
 * This component fetches the account summary data from the Redux store
 * and displays the current balance, monthly income, and monthly expenses.
 * It also handles loading and error states.
 *
 * @returns {JSX.Element} The AccountSummary component
 * @constructor
 */
const AccountSummary = () => {
   const dispatch = useDispatch();
   const { balance, income, expenses, loading, error } = useSelector(
      (state) => state.accountSummary || {}
   );

   useEffect(() => {
      dispatch(fetchAccountSummary());
   }, [dispatch]);

   if (loading) return <div>Loading account summary...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <div className="account-summary">
         <h2>Account Summary</h2>
         <div className="summary-item">
            <span>Current Balance:</span>
            <span>${(balance || 0).toFixed(2)}</span>
         </div>
         <div className="summary-item">
            <span>Monthly Income:</span>
            <span>${(income || 0).toFixed(2)}</span>
         </div>
         <div className="summary-item">
            <span>Monthly Expenses:</span>
            <span>${(expenses || 0).toFixed(2)}</span>
         </div>
      </div>
   );
};

export default AccountSummary;