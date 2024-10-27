import {Navigate} from "react-router-dom";
import FinancialGoalTracker from "../components/goals/FinancialGoalTracker";
import AccountSummary from "../components/dashboard/AccountSummary";
import RecentTransactions from "../components/dashboard/RecentTransactions";
import ServiceBooking from "../components/services/ServiceBooking";
import AppointmentList from "../components/appointments/AppointmentList";
import CreateBlogPostPage from "./CreateBlogPostPage.jsx";
import AdminAppointmentList from "../components/appointments/AdminAppointmentList.jsx";
import CreateServicePage from "./CreateServicePage.jsx";
import AdminTestimonials from "../components/home/AdminTestimonials.jsx";
import {useAuthContext} from "../hooks/useAuthContext";
import {useDashboardHook} from "../hooks/useDashboardHook.js";

/**
 * UserDashboard component
 * This component represents the user dashboard page.
 * It displays the account summary, recent transactions, and a financial goal tracker.
 */
const UserDashboardPage = () => {
   const {user, isAuthenticated, loading, hasSecuredRole} = useAuthContext();
   const {activeTab, handleTabChange} = useDashboardHook();
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
         case "adminAppointments":
            return <AdminAppointmentList/>;
         case "createBlog":
            return <CreateBlogPostPage/>;
         case "createService":
            return <CreateServicePage/>;
         case "manageTestimonials":
            return <AdminTestimonials/>;
         default:
            return null;
      }
   };

   return (
      <div className="user-dashboard">
         <h1>Welcome, {user.firstName}</h1>
         <div className="dashboard-tabs">
            <button onClick={() => handleTabChange("summary")}>Summary</button>
            <button onClick={() => handleTabChange("goals")}>Goals</button>
            <button onClick={() => handleTabChange("services")}>
               Book Service
            </button>
            <button onClick={() => handleTabChange("appointments")}>
               Appointments
            </button>
            {hasSecuredRole && (
               <button onClick={() => handleTabChange("adminAppointments")}>
                  Manage All Appointments
               </button>
            )}
            {hasSecuredRole && (
               <button onClick={() => handleTabChange("createBlog")}>
                  Create Blog Post
               </button>
            )}

            {hasSecuredRole && (
               <button onClick={() => handleTabChange("createService")}>
                  Create a Service
               </button>
            )}
            {hasSecuredRole && (
               <button onClick={() => handleTabChange("manageTestimonials")}>Manage Testimonials</button>
            )}
         </div>
         <div className="dashboard-content">{renderActiveTab()}</div>
      </div>
   );
};

export default UserDashboardPage;