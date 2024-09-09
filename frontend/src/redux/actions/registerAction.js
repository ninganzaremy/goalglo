import apiService from "../../services/apiService.js";


export const REGISTER_REQUEST = 'REGISTER_REQUEST';
export const REGISTER_SUCCESS = 'REGISTER_SUCCESS';
export const REGISTER_FAILURE = 'REGISTER_FAILURE';

/**
 * Action creator for registering a user.
 * @param {string} username - The username of the user.
 * @param {string} email - The email of the user.
 * @param {string} password - The password of the user.
 * @param {string} firstName - The first name of the user.
 * @param {string} lastName - The last name of the user.
 * @param {string} phoneNumber - The phone number of the user.
 * @param {string} address - The address of the user.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const registerUser = (username, email, password, firstName, lastName, phoneNumber, address) => {
   return async (dispatch) => {
      dispatch({ type: REGISTER_REQUEST });

      try {
         const response = await apiService.post('/users/register', {
            username,
            email,
            password,
            firstName,
            lastName,
            phoneNumber,
            address
         });

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