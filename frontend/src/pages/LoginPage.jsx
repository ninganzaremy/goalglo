import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {loginUser} from "../redux/actions/loginAction.js";
import {Link, useNavigate} from "react-router-dom";

/**
 * Login component
 * @returns {JSX.Element}
 * @constructor
 */
const LoginPage = () => {
   const [username, setUsername] = useState('');
   const [password, setPassword] = useState('');
   const dispatch = useDispatch();
   const navigate = useNavigate();
   const {loading, error, isAuthenticated} = useSelector(state => state.auth);

   const handleSubmit = async (e) => {
      e.preventDefault();
      try {
         await dispatch(loginUser({username, password}));
         // If login is successful, navigate to dashboard
         navigate('/dashboard');
      } catch (err) {
         console.error('Login error:', err);
      }
   };

   useEffect(() => {
      if (isAuthenticated) {
         navigate('/dashboard');
      }
   }, [isAuthenticated, navigate]);

   return (
      <div className="page login-page">
         <div className="login-form">
            <form onSubmit={handleSubmit}>
               <h2>Login</h2>

               {error && (
                  <div className="error-message" role="alert">
                     <span>{error}</span>
                  </div>
               )}

               <div className="form-group">
                  <label htmlFor="username">Username</label>
                  <input
                     id="username"
                     type="text"
                     placeholder="Username"
                     value={username}
                     onChange={(e) => setUsername(e.target.value)}
                     required
                  />
               </div>
               <div className="form-group">
                  <label htmlFor="password">Password</label>
                  <input
                     id="password"
                     type="password"
                     placeholder="Password"
                     value={password}
                     onChange={(e) => setPassword(e.target.value)}
                     required
                  />
               </div>
               <div className="form-footer">
                  <button className="btn-primary" type="submit" disabled={loading}>
                     {loading ? 'Signing in...' : 'Sign In'}
                  </button>
                  <Link to="/password-reset-request" className="forgot-password">
                     Forgot Password?
                  </Link>
               </div>
            </form>
         </div>
      </div>
   );
};

export default LoginPage;