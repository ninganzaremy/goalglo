import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useLocation, useNavigate} from "react-router-dom";
import {confirmPasswordReset} from "../redux/actions/authActions";

/**
 * PasswordResetConfirmationPage component
 * Handles the password reset confirmation process
 */
const PasswordResetConfirmationPage = () => {
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");
   const [error, setError] = useState("");

   const dispatch = useDispatch();
   const navigate = useNavigate();

   const token = new URLSearchParams(useLocation().search).get('token');

   const {
      loading,
      success,
      error: resetError,
   } = useSelector((state) => state.auth.passwordResetConfirm);

   useEffect(() => {
      if (success) {
         navigate("/login");
      }
   }, [success, history]);

   const handleSubmit = (e) => {
      e.preventDefault();
      if (password !== confirmPassword) {
         setError("Passwords do not match");
      } else {
         setError("");
         dispatch(confirmPasswordReset(token, password));
      }
   };

   return (
      <div className="password-reset-confirmation-page">
         <form className="password-reset-form" onSubmit={handleSubmit}>
            <h2>Set New Password</h2>
            {error && <p className="error-message">{error}</p>}
            {resetError && <p className="error-message">{resetError}</p>}
            <div className="form-group">
               <label htmlFor="password">New Password</label>
               <input
                  type="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
               />
            </div>
            <div className="form-group">
               <label htmlFor="confirmPassword">Confirm New Password</label>
               <input
                  type="password"
                  id="confirmPassword"
                  value={confirmPassword}
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  required
               />
            </div>
            <button type="submit" className="btn-primary" disabled={loading}>
               {loading ? "Resetting..." : "Reset Password"}
            </button>
         </form>
      </div>
   );
};

export default PasswordResetConfirmationPage;