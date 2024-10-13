import {useRecentTransactionsHook} from "../../hooks/useRecentTransactionsHook.js";

/**
 * RecentTransactions component
 * This component fetches the recent transactions data from the Redux store
 * and displays a list of transactions with their descriptions and amounts.
 * It also handles loading and error states.
 *
 * @returns JSX Element The RecentTransactions component
 * @constructor
 */
const RecentTransactions = () => {

   const {transactions, loading, error} = useRecentTransactionsHook();
   if (loading) return <div>Loading recent transactions...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <div className="recent-transactions">
         <h2>Recent Transactions</h2>
         {transactions.length === 0 ? (
            <p>No recent transactions.</p>
         ) : (
            <ul>
               {transactions.map(transaction => (
                  <li key={transaction.id} className={`transaction ${transaction.type}`}>
                     <span>{transaction.description}</span>
                     <span>${transaction.amount.toFixed(2)}</span>
                  </li>
               ))}
            </ul>
         )}
      </div>
   );
};

export default RecentTransactions;