import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {requestPasswordReset} from '../redux/actions/authActions';

/**
 * PasswordResetRequestPage component
 * This component renders a form for requesting a password reset.
 * It includes input fields for the user's email, a submit button, and
 * displays success or error messages based on the state of the password reset request.
 */
const PasswordResetRequestPage = () => {
   const [email, setEmail] = useState('');
   const dispatch = useDispatch();
   const {loading, success, error} = useSelector(state => state.auth.passwordReset);

   const handleSubmit = (e) => {
      e.preventDefault();
      dispatch(requestPasswordReset(email));
   };

   return (
      <div className="password-reset-request-page">
         <form className="password-reset-form" onSubmit={handleSubmit}>
            <h2>Reset Password</h2>
            {success && <p className="success-message">Password reset email sent. Please check your inbox.</p>}
            {error && <p className="error-message">{error}</p>}
            <div className="form-group">
               <label htmlFor="email">Email</label>
               <input
                  type="email"
                  id="email"
                  name="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
               />
            </div>
            <button type="submit" className="btn-primary" disabled={loading}>
               {loading ? 'Sending...' : 'Reset Password'}
            </button>
         </form>
      </div>
   );
};

export default PasswordResetRequestPage;