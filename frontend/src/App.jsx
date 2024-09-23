import {Route, Routes} from "react-router-dom";

import FinancialGoalTracker from "./components/goals/FinancialGoalTracker";
import ServiceBooking from "./components/services/ServiceBooking";
import AppointmentList from "./components/appointments/AppointmentList";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import UserDashboardPage from "./pages/UserDashboardPage";
import ServicesPage from "./pages/ServicesPage";
import CreateServicePage from "./pages/CreateServicePage";
import BlogPage from "./pages/BlogPage";
import ContactPage from "./pages/ContactPage";
import AboutUs from "./components/home/AboutUs";
import CreateBlogPostPage from "./pages/CreateBlogPostPage";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import EmailVerificationPage from "./pages/EmailVerificationPage";
import BlogPostList from "./components/blog/BlogPostList";
import SingleBlogPostPage from "./components/blog/SingleBlogPostPage";
import SingleServicePage from "./components/services/SingleServicePage";
import PasswordResetConfirmationPage from "./pages/PasswordResetConfirmationPage";
import PasswordResetRequestPage from "./pages/PasswordResetRequestPage";
import BookingConfirmationPage from "./pages/BookingConfirmationPage";


import './styles/main.scss';
import AdminTestimonials from "./components/home/AdminTestimonials.jsx";
import SecuredRoute from "./components/common/SecuredRoute.jsx";
import {AuthProvider} from "./context/AuthContext.jsx";

/**
 * The main App component that renders the application routes and components.
 * @returns {JSX.Element} The rendered App component.
 */
const App = () => {

   return (
      <AuthProvider>
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
                  element={
                     <SecuredRoute>
                        <CreateServicePage/>
                     </SecuredRoute>
                  }
               />
               <Route
                  path="/admin/testimonials"
                  element={
                     <SecuredRoute>
                        <AdminTestimonials/>
                     </SecuredRoute>
                  }
               />
               <Route path="/services" element={<ServicesPage/>}/>
               <Route path="/blog" element={<BlogPage/>}/>
               <Route path="/blog/:id" element={<SingleBlogPostPage/>}/>
               <Route path="/contact" element={<ContactPage/>}/>
               <Route path="/create-blog" element={<CreateBlogPostPage/>}/>
               <Route path="/goals" element={<FinancialGoalTracker/>}/>
               <Route path="/book-service" element={<ServiceBooking/>}/>
               <Route path="/book-service/:id" element={<ServiceBooking/>}/>
               <Route path="/services/:id" element={<SingleServicePage/>}/>
               <Route path="/appointments" element={<AppointmentList/>}/>
               <Route path="/book" element={<ServiceBooking/>}/>
               <Route path="/verify-email" element={<EmailVerificationPage/>}/>
               <Route path="/blog" element={<BlogPostList/>}/>
               <Route path="/password-reset-request" element={<PasswordResetRequestPage/>}/>
               <Route path="/password-reset-confirm" element={<PasswordResetConfirmationPage/>}/>
               <Route path="/booking-confirmation" element={<BookingConfirmationPage/>}/>
            </Routes>
            <Footer/>
         </div>
      </AuthProvider>
   );
}

export default App;