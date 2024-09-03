import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {fetchUserData} from "../redux/actions/userActions";
import {getEncryptedItem} from "../utils/envConfig";
import FinancialGoalTracker from "../components/goals/FinancialGoalTracker";
import AccountSummary from "../components/dashboard/AccountSummary";
import RecentTransactions from "../components/dashboard/RecentTransactions";
import ServiceBooking from "../components/services/ServiceBooking";
import AppointmentList from "../components/appointments/AppointmentList";

/**
 * UserDashboard component
 * This component represents the user dashboard page.
 * It displays the account summary, recent transactions, and a financial goal tracker.
 */
const UserDashboardPage = () => {
   const {userData, loading, error} = useSelector((state) => state.user);
   const dispatch = useDispatch();
   const navigate = useNavigate();
   const [activeTab, setActiveTab] = useState("summary");

   useEffect(() => {
      const token = getEncryptedItem();
      if (!token) {
         navigate("/login");
      } else {
         dispatch(fetchUserData());
      }
   }, [dispatch, navigate]);


   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;
   if (!userData) return null;

   const renderActiveTab = () => {
      switch (activeTab) {
         case "summary":
            return (
               <>
                  <AccountSummary/>
                  <RecentTransactions/>
               </>
            );
         case "goals":
            return <FinancialGoalTracker/>;
         case "services":
            return <ServiceBooking/>;
         case "appointments":
            return <AppointmentList/>;
         default:
            return null;
      }
   };

   return (
      <div className="user-dashboard">
         <h1>Welcome, {userData.name}</h1>
         <div className="dashboard-tabs">
            <button onClick={() => setActiveTab("summary")}>Summary</button>
            <button onClick={() => setActiveTab("goals")}>Goals</button>
            <button onClick={() => setActiveTab("services")}>
               Book Service
            </button>
            <button onClick={() => setActiveTab("appointments")}>
               Appointments
            </button>
         </div>
         <div className="dashboard-content">{renderActiveTab()}</div>
      </div>
   );
};

export default UserDashboardPage;