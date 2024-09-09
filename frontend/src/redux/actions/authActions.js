import apiService from "../../services/apiService";

export const PASSWORD_RESET_CONFIRM_REQUEST = 'PASSWORD_RESET_CONFIRM_REQUEST';
export const PASSWORD_RESET_CONFIRM_SUCCESS = 'PASSWORD_RESET_CONFIRM_SUCCESS';
export const PASSWORD_RESET_CONFIRM_FAILURE = 'PASSWORD_RESET_CONFIRM_FAILURE';


export const PASSWORD_RESET_REQUEST = 'PASSWORD_RESET_REQUEST';
export const PASSWORD_RESET_SUCCESS = 'PASSWORD_RESET_SUCCESS';
export const PASSWORD_RESET_FAILURE = 'PASSWORD_RESET_FAILURE';

/*
 * Confirm the password reset token and set a new password.
 * @param {string} token - The password reset token.
 * @param {string} newPassword - The new password to set.
 * @returns {function} A Redux thunk action creator.
 */
export const confirmPasswordReset = (token, newPassword) => async (dispatch) => {
   dispatch({type: PASSWORD_RESET_CONFIRM_REQUEST});
   try {
      const response = await apiService.post('/password/reset/confirm', {token, newPassword});
      dispatch({
         type: PASSWORD_RESET_CONFIRM_SUCCESS,
         payload: response.data.message
      });
   } catch (error) {
      dispatch({
         type: PASSWORD_RESET_CONFIRM_FAILURE,
         payload: error.response ? error.response.data.message : 'An unexpected error occurred'
      });
   }
};


/*
 * Request a password reset email.
 * @param {string} email - The email address to send the reset link to.
 * @returns {function} A Redux thunk action creator.
 */
export const requestPasswordReset = (email) => async (dispatch) => {
   dispatch({type: PASSWORD_RESET_REQUEST});
   try {
      const response = await apiService.post('/password/reset', {email});
      dispatch({
         type: PASSWORD_RESET_SUCCESS,
         payload: response.data.message
      });
   } catch (error) {
      dispatch({
         type: PASSWORD_RESET_FAILURE,
         payload: error.response ? error.response.data.message : 'An unexpected error occurred'
      });
   }
};