import {useState} from "react";
import {useSelector} from "react-redux";
import {Navigate} from "react-router-dom";
import FinancialGoalTracker from "../components/goals/FinancialGoalTracker";
import AccountSummary from "../components/dashboard/AccountSummary";
import RecentTransactions from "../components/dashboard/RecentTransactions";
import ServiceBooking from "../components/services/ServiceBooking";
import AppointmentList from "../components/appointments/AppointmentList";
import {decryptData, userSecuredRole} from "../security/securityConfig.js";
import CreateBlogPostPage from "./CreateBlogPostPage.jsx";

/**
 * UserDashboard component
 * This component represents the user dashboard page.
 * It displays the account summary, recent transactions, and a financial goal tracker.
 */
const UserDashboardPage = () => {
   const {user, isAuthenticated, loading} = useSelector(
      (state) => state.auth
   );
   const [activeTab, setActiveTab] = useState("summary");
   const canCreateBlogPosts = user && user.roles && (user.roles.includes(decryptData(userSecuredRole())));

   if (loading) return <div>Loading...</div>;
   if (!isAuthenticated || !user) return <Navigate to="/login" replace/>;

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
         case "createBlog":
            return <CreateBlogPostPage/>;
         default:
            return null;
      }
   };

   return (
      <div className="user-dashboard">
         <h1>Welcome, {user.firstName}</h1>
         <div className="dashboard-tabs">
            <button onClick={() => setActiveTab("summary")}>Summary</button>
            <button onClick={() => setActiveTab("goals")}>Goals</button>
            <button onClick={() => setActiveTab("services")}>
               Book Service
            </button>
            <button onClick={() => setActiveTab("appointments")}>
               Appointments
            </button>
            {canCreateBlogPosts && (
               <button onClick={() => setActiveTab("createBlog")}>Create Blog Post</button>
            )}
         </div>
         <div className="dashboard-content">{renderActiveTab()}</div>
      </div>
   );
};

export default UserDashboardPage;