import {useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {checkAuthStatus, logoutUser} from "../../redux/actions/loginAction";
import logo from "../../assets/images/goalglo-logo.svg";
import {useAuthContext} from "../../hooks/useAuthContext";

/**
 * Header component
 * @returns {JSX.Element}
 */
const Header = () => {
   const dispatch = useDispatch();
   const navigate = useNavigate();
   const {isAuthenticated, user, loading} = useAuthContext();
   const [isMenuOpen, setIsMenuOpen] = useState(false);

   useEffect(() => {
      dispatch(checkAuthStatus());
   }, [dispatch]);

   const handleLogout = () => {
      dispatch(logoutUser());
      navigate("/");
   };

   const toggleMenu = () => {
      setIsMenuOpen(!isMenuOpen);
   };

   if (loading) {
      return <div>Loading...</div>;
   }

   return (
      <>
         <header className={`header ${isMenuOpen ? 'menu-open' : ''}`}>
            <div className="container">
               <Link to="/" className="logo">
                  <img src={logo} alt="GoalGlo Logo"/>
               </Link>
               <button className={`menu-toggle ${isMenuOpen ? 'active' : ''}`} onClick={toggleMenu}
                       aria-label="Toggle menu">
                  <span></span>
                  <span></span>
                  <span></span>
               </button>
               <nav className={`nav-menu ${isMenuOpen ? 'active' : ''}`}>
                  <ul className="nav-links">
                     <li><Link to="/" onClick={toggleMenu}>Home</Link></li>
                     <li><Link to="/services" onClick={toggleMenu}>Services</Link></li>
                     <li><Link to="/blog" onClick={toggleMenu}>Blog</Link></li>
                     <li><Link to="/about" onClick={toggleMenu}>About</Link></li>
                     <li><Link to="/contact" onClick={toggleMenu}>Contact</Link></li>
                  </ul>
                  <div className="auth-buttons">
                     {isAuthenticated && user ? (
                        <>
                           <span className="user-name">Welcome, {user.firstName}</span>
                           <button onClick={handleLogout} className="btn btn-secondary">Logout</button>
                           <Link to="/dashboard" className="btn btn-secondary" onClick={toggleMenu}>Dashboard</Link>
                        </>
                     ) : (
                        <>
                           <Link to="/login" className="btn btn-secondary" onClick={toggleMenu}>Login</Link>
                           <Link to="/register" className="btn btn-primary" onClick={toggleMenu}>Register</Link>
                        </>
                     )}
                     <Link to="/book" className="btn btn-accent" onClick={toggleMenu}>Book Us</Link>
                  </div>
               </nav>
            </div>
         </header>
         {isMenuOpen && <div className="menu-overlay" onClick={toggleMenu}></div>}
      </>
   );
};

export default Header;