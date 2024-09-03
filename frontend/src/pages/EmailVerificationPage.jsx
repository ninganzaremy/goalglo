import {useEffect} from "react";
import {useLocation} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {verifyEmail} from "../redux/actions/userActions";

/**
 * EmailVerificationPage component
 * This component handles the email verification process.
 * It retrieves the verification token from the URL query parameters
 * and dispatches the verifyEmail action with the token.
 * The component displays a loading message while verifying the email
 * and a success or error message based on the verification result.
 *
 * @returns {JSX.Element}
 */
const EmailVerificationPage = () => {
   const dispatch = useDispatch();
   const location = useLocation();
   const userState = useSelector((state) => state.user);
   const emailVerification = userState.emailVerification || {};
   const { loading = false, success = false, message = "" } = emailVerification;

   useEffect(() => {
      const queryParams = new URLSearchParams(location.search);
      const token = queryParams.get("token");

      if (token) {
         dispatch(verifyEmail(token));
      }
   }, [dispatch, location]);

   return (
      <div className="verification-container">
         <div className="verification-card">
            <h1 className="verification-title">Email Verification</h1>
            {loading ? (
               <p className="verification-message">Verifying your email...</p>
            ) : (
               <p
                  className={`verification-message ${
                     success ? "success" : "error"
                  }`}
               >
                  {message || "An error occurred during email verification."}
               </p>
            )}
         </div>
      </div>
   );
};

export default EmailVerificationPage;