import {Link} from 'react-router-dom';
import logo from '../../assets/images/logo.png';

/**
 * Header component
 * @returns {JSX.Element}
 */
const Header = () => {
   return (
      <header className="header">
         <div className="container">
            <Link to="/" className="logo">
               <img src={logo} alt="GoalGlo Logo"/>
            </Link>
            <nav>
               <ul className="nav-links">
                  <li><Link to="/">Home</Link></li>
                  <li><Link to="/services">Services</Link></li>
                  <li><Link to="/blog">Blog</Link></li>
                  <li><Link to="/about">About</Link></li>
                  <li><Link to="/contact">Contact</Link></li>
               </ul>
            </nav>
            <div className="auth-buttons">
               <Link to="/login" className="btn btn-secondary">Login</Link>
               <Link to="/register" className="btn btn-primary">Register</Link>
               <Link to="/book" className="btn btn-accent">Book Us</Link>
            </div>
         </div>
      </header>
   );
};

export default Header;