import axios from 'axios';
import {getApiUrl} from "../../utils/envConfig.js";


export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILURE = 'LOGIN_FAILURE';

/**
 * Action creator for user login
 *
 * @param {string} username - The user's username
 * @param {string} password - The user's password
 * @returns {Function} - A function that dispatches actions to the Redux store
 */
export const loginUser = (username, password) => {
   return async (dispatch) => {
      dispatch({type: LOGIN_REQUEST});

      try {
         const response = await axios.post(`${getApiUrl()}/users/login`, {username, password});

         // get returned token
         const {token, user} = response.data;

         // Store the token in localStorage
         localStorage.setItem('token', token);

         dispatch({
            type: LOGIN_SUCCESS,
            payload: user
         });
      } catch (error) {
         dispatch({
            type: LOGIN_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
      }
   };
};

/**
 * Action creator for user logout
 *
 * @returns {Object} - An action object
 */
export const logoutUser = () => {
   localStorage.removeItem('token');
   return {type: 'LOGOUT'};
};