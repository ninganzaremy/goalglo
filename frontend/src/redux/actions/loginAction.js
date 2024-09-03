import {getEncryptedItem, removeEncryptedItem, setEncryptedItem,} from "../../utils/envConfig.js";
import apiService from "../../services/apiService.js";
import {fetchUserData} from "./userActions.js";

// Action Types
export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN_FAILURE";
export const LOGOUT = "LOGOUT";
export const CLEAR_LOGIN_ERROR = "CLEAR_LOGIN_ERROR";

export const loginRequest = () => ({type: LOGIN_REQUEST});
export const loginSuccess = (user) => ({type: LOGIN_SUCCESS, payload: user});
export const loginFailure = (error) => ({
   type: LOGIN_FAILURE,
   payload: error,
});
export const logout = () => ({type: LOGOUT});

/**
 * Action creator for user login
 *
 * @returns {Function} - A function that dispatches actions to the Redux store
 * @param credentials
 */
export const loginUser = (credentials) => async (dispatch) => {
   dispatch(loginRequest());
   try {
      const response = await apiService.post("/users/login", credentials);
      const {token} = response.data;
      console.log("Login successful, token received");
      if (token) {
         setEncryptedItem(token);
         // Dispatch a temporary action to indicate successful authentication
         dispatch({type: "AUTH_SUCCESS"});

         // Fetch user data and wait for it to resolve
         try {
            const userData = await dispatch(fetchUserData(token));
            // Dispatch login success with the fetched user data
            dispatch(loginSuccess(userData));
            return userData;
         } catch (fetchError) {
            console.error("Failed to fetch user data:", fetchError);
            dispatch(loginFailure("Failed to fetch user data"));
            throw fetchError;
         }
      } else {
         throw new Error("Token not received from server");
      }
   } catch (error) {
      // console.error("Login failed:", error);
      const errorMessage = error.response?.data?.message || "Login failed";
      dispatch(loginFailure(errorMessage));
      throw error;
   }
};

/**
 * Action creator for user logout
 *
 * @returns {Object} - An action object
 */
export const logoutUser = () => {
   return (dispatch) => {
      removeEncryptedItem();
      dispatch(logout());
   }
};

// Action to check if user is already authenticated (e.g., on app load)
export const checkAuthStatus = () => async (dispatch) => {
   const token = getEncryptedItem();
   if (token) {
      try {
         // Dispatch AUTH_SUCCESS to update isAuthenticated immediately
         dispatch({type: 'AUTH_SUCCESS'});

         // Use fetchUserData to get and dispatch user data
         const userData = await dispatch(fetchUserData(token));
         dispatch(loginSuccess(userData));
      } catch (error) {
         console.error("Auth check failed:", error);
         removeEncryptedItem(); // Clear invalid token
         dispatch(logout());
      }
   } else {
      dispatch(logout());
   }
};