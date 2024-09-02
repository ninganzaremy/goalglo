import {useSelector} from 'react-redux';
import FinancialGoalTracker from '../components/goals/FinancialGoalTracker';
import AccountSummary from '../components/dashboard/AccountSummary';
import RecentTransactions from '../components/dashboard/RecentTransactions';

/**
 * UserDashboard component
 * This component represents the user dashboard page.
 * It displays the account summary, recent transactions, and a financial goal tracker.
 */
const UserDashboard = () => {
   const user = useSelector(state => state.user);

   return (
      <div className="user-dashboard">
         <h1>Welcome, {user.name}</h1>
         <div className="dashboard-content">
            <div className="main-content">
               <AccountSummary />
               <RecentTransactions />
            </div>
            <div className="side-content">
               <FinancialGoalTracker />
            </div>
         </div>
      </div>
   );
};

export default UserDashboard;