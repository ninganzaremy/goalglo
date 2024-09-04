import {
   decodeJwtToken,
   getEncryptedItem,
   isTokenExpired,
   manageStorageEventListener,
   removeEncryptedItem,
   setEncryptedItem,
} from "../../security/securityConfig";
import apiService from "../../services/apiService";


// Action Types
export const LOGIN_REQUEST = "LOGIN_REQUEST";
export const LOGIN_SUCCESS = "LOGIN_SUCCESS";
export const LOGIN_FAILURE = "LOGIN_FAILURE";
export const LOGOUT = "LOGOUT";
export const CLEAR_LOGIN_ERROR = "CLEAR_LOGIN_ERROR";
export const CHECK_TOKEN_EXPIRATION = "CHECK_TOKEN_EXPIRATION";


export const loginRequest = () => ({type: LOGIN_REQUEST});
export const loginSuccess = (user) => ({type: LOGIN_SUCCESS, payload: user});
export const loginFailure = (error) => ({
   type: LOGIN_FAILURE,
   payload: error,
});
export const logout = () => ({type: LOGOUT});

export const checkTokenExpiration = () => ({type: CHECK_TOKEN_EXPIRATION});

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
      if (token) {
         setEncryptedItem(token);
         const userData = decodeJwtToken(token);
         // Token expiration handling
         if (isTokenExpired(token)) {
            dispatch(checkTokenExpiration());
            dispatch(logoutUser());
            throw new Error("Token has expired");
         }
         dispatch(loginSuccess(userData));
         return userData;
      } else {
         throw new Error("Token not received from server");
      }
   } catch (error) {
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
      manageStorageEventListener("set", null, "logout", Date.now())
   };
};


/**
 * Action creator for checking authentication status
 *
 * @returns {Function} - A function that dispatches actions to the Redux store
 */
export const checkAuthStatus = () => (dispatch) => {
   const token = getEncryptedItem();
   if (token) {
      try {
         if (!isTokenExpired(token)) {
            const userData = decodeJwtToken(token);
            dispatch(loginSuccess(userData));
         } else {
            dispatch(checkTokenExpiration());
            dispatch(logoutUser());
         }
      } catch (error) {
         dispatch(logoutUser());
      }
   } else {
      dispatch(logoutUser());
   }
};