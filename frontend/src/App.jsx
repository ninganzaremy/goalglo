import {Route, Routes} from "react-router-dom";
import FinancialGoalTracker from "./components/goals/FinancialGoalTracker";
import ServiceBooking from "./components/services/ServiceBooking";
import AppointmentList from "./components/appointments/AppointmentList";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import UserDashboardPage from "./pages/UserDashboardPage.jsx";
import ServicesPage from "./pages/ServicesPage.jsx";
import CreateServicePage from "./pages/CreateServicePage.jsx";
import BlogPage from "./pages/BlogPage.jsx";
import ContactPage from "./pages/ContactPage.jsx";
import AboutUs from "./components/home/AboutUs.jsx";
import CreateBlogPostPage from "./pages/CreateBlogPostPage.jsx";
import Header from "./components/layout/Header.jsx";
import Footer from "./components/layout/Footer.jsx";
import {useEffect} from "react";
import {checkAuthStatus} from "./redux/actions/loginAction.js";
import {useDispatch} from "react-redux";
import EmailVerificationPage from "./pages/EmailVerificationPage.jsx";

import './styles/main.scss';

/**
 * The main App component.
 *
 * @returns {JSX.Element}
 */
function App() {
   const dispatch = useDispatch();

   useEffect(() => {
      dispatch(checkAuthStatus());
   }, [dispatch]);

   return (
      <div className="App">
         <Header/>
         <Routes>
            <Route path="/" element={<HomePage/>}/>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/register" element={<RegisterPage/>}/>
            <Route path="/about" element={<AboutUs/>}/>
            <Route path="/dashboard" element={<UserDashboardPage/>}/>
            <Route
               path="/admin/create-service"
               element={<CreateServicePage/>}
            />
            <Route path="/services" element={<ServicesPage/>}/>
            <Route path="/blog" element={<BlogPage/>}/>
            <Route path="/contact" element={<ContactPage/>}/>
            <Route path="/create-blog" element={<CreateBlogPostPage/>}/>
            <Route path="/goals" element={<FinancialGoalTracker/>}/>
            <Route path="/services" element={<ServiceBooking/>}/>
            <Route path="/appointments" element={<AppointmentList/>}/>
            <Route path="/book" element={<ServiceBooking/>}/>
            <Route path="/verify-email" element={<EmailVerificationPage/>}/>

         </Routes>
         <Footer/>
      </div>
   );
}

export default App;