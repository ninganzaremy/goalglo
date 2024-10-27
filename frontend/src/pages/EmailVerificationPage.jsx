import {useEffect, useRef} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {verifyEmail} from "../redux/actions/userActions";
import useConfirmationState from "../hooks/useConfirmationState.jsx";

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
   const navigate = useNavigate();

   const userState = useSelector((state) => state.user);
   const emailVerification = userState.emailVerification;
   const {loading = false, success = false, error = false, message = ""} = emailVerification;

   const verificationAttempted = useRef(false);

   useEffect(() => {
      const queryParams = new URLSearchParams(location.search);
      const token = queryParams.get("token");

      if (token && !verificationAttempted.current) {
         verificationAttempted.current = true;
         dispatch(verifyEmail(token));
      }
   }, [dispatch, location]);


   const handleTryAgain = () => {
      navigate(0);
   };
   const confirmationState = useConfirmationState(loading, success, error, {
      loadingTitle: "Email Verification in Progress",
      loadingMessage: "We're verifying your email...",
      successTitle: "Email Verification Successful!",
      successMessage: "Your email has been successfully verified. You can now log in to your account.",
      successActionText: "Go to Login",
      successActionLink: "/login",
      errorTitle: "Email Verification Failed",
      errorMessage: message || "An error occurred during email verification.",
      errorActionText: "Try Again",
      errorActionOnClick: handleTryAgain
   });

   if (confirmationState) {
      return confirmationState;
   }

   return (
      <div className="verification-container">
         <div className="verification-card">
            <h1 className="verification-title">Email Verification</h1>
            <p className="verification-message">
               Waiting for verification process to start...
            </p>
         </div>
      </div>
   );
};

export default EmailVerificationPage;