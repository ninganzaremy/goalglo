import {Route, Routes} from 'react-router-dom';
import Home from './pages/Home';
import Blog from './pages/Blog';
import BlogDetail from './pages/BlogDetail';
import Dashboard from './pages/Dashboard';
import AdminPanel from './pages/admin/AdminPanel.jsx';
import Register from './pages/Register';
import Login from './pages/Login';
import Contact from './pages/Contact';
import Header from './components/Header';
import Footer from './components/Footer';
import CreateService from "./pages/CreateService.jsx";

const App = () => {
   return (
      <div>
         <Header/>
         <Routes>
            <Route path="/" element={<Home/>}/>
            <Route path="/blog" element={<Blog/>}/>
            <Route path="/blog/:id" element={<BlogDetail/>}/>
            <Route path="/dashboard" element={<Dashboard/>}/>
            <Route path="/admin" element={<AdminPanel/>}/>
            <Route path="/admin/create-service" element={<CreateService/>}/>

            <Route path="/register" element={<Register/>}/>
            <Route path="/login" element={<Login/>}/>
            <Route path="/contact" element={<Contact/>}/>
         </Routes>
         <Footer/>
      </div>
   );
}

export default App;