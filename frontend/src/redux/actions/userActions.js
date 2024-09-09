import apiService from "../../services/apiService.js";

export const FETCH_USER_DATA_REQUEST = 'FETCH_USER_DATA_REQUEST';
export const FETCH_USER_DATA_SUCCESS = 'FETCH_USER_DATA_SUCCESS';
export const FETCH_USER_DATA_FAILURE = 'FETCH_USER_DATA_FAILURE';


export const VERIFY_EMAIL_REQUEST = 'VERIFY_EMAIL_REQUEST';
export const VERIFY_EMAIL_SUCCESS = 'VERIFY_EMAIL_SUCCESS';
export const VERIFY_EMAIL_FAILURE = 'VERIFY_EMAIL_FAILURE';


/**
 * Action creator for verifying email.
 * @param {string} token - The email verification token.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const verifyEmail = (token) => async (dispatch) => {
   dispatch({type: VERIFY_EMAIL_REQUEST});
   try {
      const response = await apiService.post('/users/verify-email', {token});
      dispatch({
         type: VERIFY_EMAIL_SUCCESS,
         payload: response.data
      });
   } catch (error) {
      dispatch({
         type: VERIFY_EMAIL_FAILURE,
         payload: 'Email verification failed. Please try again or contact support.'
      });
   }
};