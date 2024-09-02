import {Route, Routes} from 'react-router-dom';

import Header from './components/layout/Header.jsx';
import Footer from './components/layout/Footer.jsx';
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

import './styles/main.scss';


/**
 * The main App component.
 *
 * @returns {JSX.Element}
 */
const App = () => {
   return (
      <div className="app">
         <Header/>
         <main>
            <Routes>
               <Route path="/" element={<HomePage/>}/>
               <Route path="/login" element={<LoginPage/>}/>
               <Route path="/register" element={<RegisterPage/>}/>
               <Route path="/about" element={<AboutUs/>}/>
               <Route path="/dashboard" element={<UserDashboardPage/>}/>
               <Route path="/admin/create-service" element={<CreateServicePage/>}/>
               <Route path="/services" element={<ServicesPage/>}/>
               <Route path="/blog" element={<BlogPage/>}/>
               <Route path="/contact" element={<ContactPage/>}/>
               <Route path="/create-blog" element={<CreateBlogPostPage/>}/>
            </Routes>
         </main>
         <Footer/>
      </div>
   );
}

export default App;