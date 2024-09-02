import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchRecentTransactions} from '../../redux/actions/recentTransactionActions';

/*
 * This component displays a list of recent transactions.
 * It fetches the recent transactions from the Redux store using the `fetchRecentTransactions` action creator.
 * If the transactions are still loading, it displays a loading message.
 * If there is an error fetching the transactions, it displays the error message.
 * If there are no recent transactions, it displays a message indicating that.
 * Otherwise, it renders a list of transactions, where each transaction is displayed as a list item with the transaction description and amount.
 * The list item class name is determined by the transaction type (e.g., 'income' or 'expense').
 * The component is wrapped in a div with the class name 'recent-transactions'.
 * RecentTransactions component
 * @returns {JSX.Element} The rendered component
 * @constructor
 */
const RecentTransactions = () => {
   const dispatch = useDispatch();
   const { transactions, loading, error } = useSelector(state => state.recentTransactions);

   useEffect(() => {
      dispatch(fetchRecentTransactions());
   }, [dispatch]);

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