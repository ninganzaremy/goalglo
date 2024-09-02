import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {loginUser} from "../redux/actions/loginAction.js";

/**
 * Login component
 * @returns {JSX.Element}
 * @constructor
 */
const LoginPage = () => {
   const [username, setUsername] = useState('');
   const [password, setPassword] = useState('');
   const dispatch = useDispatch();
   const {loading, error} = useSelector(state => state.login);

   const handleSubmit = (e) => {
      e.preventDefault();
      dispatch(loginUser(username, password));
   };

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
                  <button
                     className="btn-primary"
                     type="submit"
                     disabled={loading}
                  >
                     {loading ? 'Signing in...' : 'Sign In'}
                  </button>
                  <a className="forgot-password" href="#">
                     Forgot Password?
                  </a>
               </div>
            </form>
         </div>
      </div>
   );
};

export default LoginPage;