import apiService from "../../services/apiService.js";

export const FETCH_USER_DATA_REQUEST = 'FETCH_USER_DATA_REQUEST';
export const FETCH_USER_DATA_SUCCESS = 'FETCH_USER_DATA_SUCCESS';
export const FETCH_USER_DATA_FAILURE = 'FETCH_USER_DATA_FAILURE';

export const fetchUserDataRequest = () => ({type: FETCH_USER_DATA_REQUEST});
export const fetchUserDataSuccess = (userData) => ({type: FETCH_USER_DATA_SUCCESS, payload: userData});
export const fetchUserDataFailure = (error) => ({type: FETCH_USER_DATA_FAILURE, payload: error});

export const VERIFY_EMAIL_REQUEST = 'VERIFY_EMAIL_REQUEST';
export const VERIFY_EMAIL_SUCCESS = 'VERIFY_EMAIL_SUCCESS';
export const VERIFY_EMAIL_FAILURE = 'VERIFY_EMAIL_FAILURE';

/**
 * Action creator for fetching user data.
 * @param {string} token - The authentication token.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const fetchUserData = (token) => async (dispatch) => {
   dispatch(fetchUserDataRequest());
   try {
      const response = await apiService.get('/users/profile', {
         headers: {Authorization: `Bearer ${token}`}
      });
      const userData = response.data;
      dispatch(fetchUserDataSuccess(userData));
      return userData;
   } catch (error) {
      console.error('Failed to fetch user data:', error);
      dispatch(fetchUserDataFailure(error.message));
      throw error;
   }
};

/**
 * Action creator for verifying email.
 * @param {string} token - The email verification token.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const verifyEmail = (token) => async (dispatch) => {
   dispatch({type: VERIFY_EMAIL_REQUEST});
   try {
      const response = await apiService.get(`/users/verify-email?token=${token}`);
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