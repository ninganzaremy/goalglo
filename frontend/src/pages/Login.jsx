import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {loginUser} from "../redux/actions/authActions.js";

/**
 * Login Component
 *
 * This component renders a login form and handles user authentication.
 * It uses Redux for state management and dispatches actions for login functionality.
 *
 * @component
 * @example
 * return (
 *   <Login />
 * )
 */
const Login = () => {
   const [username, setUsername] = useState('');
   const [password, setPassword] = useState('');
   const dispatch = useDispatch();
   const {loading, error} = useSelector(state => state.auth);

   /**
    * Handles form submission
    * @param {React.FormEvent<HTMLFormElement>} e - The form event
    */
   const handleSubmit = (e) => {
      e.preventDefault();
      dispatch(loginUser(username, password));
   };

   return (
      <div className="flex items-center justify-center min-h-screen bg-gray-100">
         <div className="w-full max-w-md">
            <form onSubmit={handleSubmit} className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
               <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Login</h2>

               {error && (
                  <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-4"
                       role="alert">
                     <span className="block sm:inline">{error}</span>
                  </div>
               )}

               <div className="mb-4">
                  <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">
                     Username
                  </label>
                  <input
                     className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                     id="username"
                     type="text"
                     autoComplete={username}
                     placeholder="Username"
                     value={username}
                     onChange={(e) => setUsername(e.target.value)}
                     required
                  />
               </div>
               <div className="mb-6">
                  <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
                     Password
                  </label>
                  <input
                     className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
                     id="password"
                     autoComplete={password}
                     type="password"
                     placeholder="******************"
                     value={password}
                     onChange={(e) => setPassword(e.target.value)}
                     required
                  />
               </div>
               <div className="flex items-center justify-between">
                  <button
                     className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                     type="submit"
                     disabled={loading}
                  >
                     {loading ? 'Signing in...' : 'Sign In'}
                  </button>
                  <a className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                     href="#">
                     Forgot Password?
                  </a>
               </div>
            </form>
         </div>
      </div>
   );
};

export default Login;