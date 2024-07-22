import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {login} from '../redux/actions/authActions';

const Login = () => {
   const [username, setUsername] = useState('');
   const [password, setPassword] = useState('');
   const dispatch = useDispatch();
   const {loading, error} = useSelector((state) => state.auth);

   const handleSubmit = (e) => {
      e.preventDefault();
      dispatch(login(username, password));
   };

   return (
      <div>
         <h2>Login</h2>
         {loading && <p>Loading...</p>}
         {error && <p>{error}</p>}
         <form onSubmit={handleSubmit}>
            <div>
               <label>Username:</label>
               <input
                  type="text"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
               />
            </div>
            <div>
               <label>Password:</label>
               <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
               />
            </div>
            <button type="submit">Login</button>
         </form>
      </div>
   );
}

export default Login;