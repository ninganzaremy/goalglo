import {useDispatch, useSelector} from "react-redux";
import {registerUser, resetRegistration} from "../redux/actions/registerAction.js";
import {useState} from "react";
import useConfirmationState from "../hooks/useConfirmationState";

/**
 * Register component
 * This component handles the registration process for new users.
 * It includes input fields for username, email, password, and confirm password.
 * Upon submission, it dispatches the `registerUser` action with the provided credentials.
 * The component also displays any error messages received from the server.
 */
const RegisterPage = () => {
   const [username, setUsername] = useState("");
   const [email, setEmail] = useState("");
   const [password, setPassword] = useState("");
   const [confirmPassword, setConfirmPassword] = useState("");
   const [firstName, setFirstName] = useState("");
   const [lastName, setLastName] = useState("");
   const [phoneNumber, setPhoneNumber] = useState("");
   const [address, setAddress] = useState("");
   const [formError, setFormError] = useState("");

   const dispatch = useDispatch();
   const {loading, error, success} = useSelector((state) => state.register);

   const handleTryAgain = () => {
      dispatch(resetRegistration());
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      if (password !== confirmPassword) {
         setFormError("Passwords do not match");
         return;
      }
      setFormError("");
      dispatch(
         registerUser(username, email, password, firstName, lastName, phoneNumber, address)
      );
   };
   const confirmationState = useConfirmationState(loading, success, error, {
      loadingTitle: "Registration in Progress",
      loadingMessage: "We're processing your registration...",
      successTitle: "Registration Successful!",
      successMessage: "Thank you for registering with GoalGlo. We've sent a verification email to your registered email address.",
      successActionText: "Return to Home",
      successActionLink: "/",
      errorTitle: "Registration Failed",
      errorMessage: "We couldn't complete your registration. Please try again or contact support.",
      errorActionText: "Try Again",
      errorActionOnClick: handleTryAgain
   });

   if (confirmationState) {
      return confirmationState;
   }

   return (
      <div className="page register-page">
         <div className="register-form">
            <form onSubmit={handleSubmit}>
               <h2>Create an Account</h2>

               {formError && (
                  <div className="error-message" role="alert">
                     <span>{formError}</span>
                  </div>
               )}

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
                  <label htmlFor="email">Email</label>
                  <input
                     id="email"
                     type="email"
                     placeholder="Email"
                     value={email}
                     onChange={(e) => setEmail(e.target.value)}
                     required
                  />
               </div>
               <div className="form-group">
                  <label htmlFor="firstName">First Name</label>
                  <input
                     id="firstName"
                     type="text"
                     placeholder="First Name"
                     value={firstName}
                     onChange={(e) => setFirstName(e.target.value)}
                     required
                  />
               </div>
               <div className="form-group">
                  <label htmlFor="lastName">Last Name</label>
                  <input
                     id="lastName"
                     type="text"
                     placeholder="Last Name"
                     value={lastName}
                     onChange={(e) => setLastName(e.target.value)}
                     required
                  />
               </div>
               <div className="form-group">
                  <label htmlFor="phoneNumber">Phone Number</label>
                  <input
                     id="phoneNumber"
                     type="tel"
                     placeholder="Phone Number"
                     value={phoneNumber}
                     onChange={(e) => setPhoneNumber(e.target.value)}
                     required
                  />
               </div>
               <div className="form-group">
                  <label htmlFor="address">Address (Optional)</label>
                  <input
                     id="address"
                     type="text"
                     placeholder="Address"
                     value={address}
                     onChange={(e) => setAddress(e.target.value)}
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
               <div className="form-group">
                  <label htmlFor="confirmPassword">Confirm Password</label>
                  <input
                     id="confirmPassword"
                     type="password"
                     placeholder="Confirm Password"
                     value={confirmPassword}
                     onChange={(e) => setConfirmPassword(e.target.value)}
                     required
                  />
               </div>
               <div className="form-footer">
                  <button
                     className="btn-primary"
                     type="submit"
                     disabled={loading}
                  >
                     {loading ? "Registering..." : "Register"}
                  </button>
               </div>
            </form>
         </div>
      </div>
   );
};

export default RegisterPage;