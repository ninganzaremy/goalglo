import apiService from "../../services/apiService.js";


export const REGISTER_REQUEST = 'REGISTER_REQUEST';
export const REGISTER_SUCCESS = 'REGISTER_SUCCESS';
export const REGISTER_FAILURE = 'REGISTER_FAILURE';

/**
 * Action creator for registering a new user.
 *
 * This action creator makes an API request to register a new user and dispatches
 * appropriate actions based on the result of the request.
 *
 * @param {string} username - The user's username
 * @param {string} email - The user's email
 * @param {string} password - The user's password
 * @returns {Function} A thunk function that dispatches actions based on API response.
 * @throws Will throw an error if the API request fails.
 */
export const registerUser = (username, email, password) => {
   return async (dispatch) => {
      dispatch({ type: REGISTER_REQUEST });

      try {
         const response = await apiService.post('/users/register', { username, email, password });

         dispatch({
            type: REGISTER_SUCCESS,
            payload: response.data
         });
      } catch (error) {
         dispatch({
            type: REGISTER_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
      }
   };
};