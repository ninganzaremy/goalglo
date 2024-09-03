import {memo, useMemo} from "react";
import {Link, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {logoutUser} from "../../redux/actions/loginAction";
import logo from "../../assets/images/vite.svg";

/**
 * Header component
 * @returns {JSX.Element}
 */
const Header = () => {
   const dispatch = useDispatch();
   const auth = useSelector(state => state.auth);
   const {userData, loading: userLoading} = useSelector(state => state.user);
   const navigate = useNavigate();

   const {isAuthenticated, user, loading} = useMemo(() => ({
      isAuthenticated: auth.isAuthenticated,
      user: userData,
      loading: auth.loading || userLoading
   }), [auth.isAuthenticated, auth.loading, userData, userLoading]);

   // console.log('Header render, auth state:', {isAuthenticated, user, loading});

   const handleLogout = () => {
      dispatch(logoutUser());
      navigate('/');
   };

   return (
      <header className="header">
         <div className="container">
            <Link to="/" className="logo">
               <img src={logo} alt="GoalGlo Logo"/>
            </Link>
            <nav>
               <ul className="nav-links">
                  <li>
                     <Link to="/">Home</Link>
                  </li>
                  <li>
                     <Link to="/services">Services</Link>
                  </li>
                  <li>
                     <Link to="/blog">Blog</Link>
                  </li>
                  <li>
                     <Link to="/about">About</Link>
                  </li>
                  <li>
                     <Link to="/contact">Contact</Link>
                  </li>
               </ul>
            </nav>
            <div className="auth-buttons">
               {isAuthenticated && user ? (
                  <>
                     <span className="user-name">Welcome, {user.username}</span>
                     <button
                        onClick={handleLogout}
                        className="btn btn-secondary"
                     >
                        Logout
                     </button>
                     <Link to="/dashboard" className="btn btn-secondary">
                        Dashboard
                     </Link>
                  </>
               ) : (
                  <>
                     <Link to="/login" className="btn btn-secondary">
                        Login
                     </Link>
                     <Link to="/register" className="btn btn-primary">
                        Register
                     </Link>
                  </>
               )}
               <Link to="/book" className="btn btn-accent">
                  Book Us
               </Link>
            </div>
         </div>
      </header>
   );
};

export default memo(Header);