import {LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS} from '../actions/loginAction.js';

/**
 * @typedef {Object} AuthState
 * @property {Object|null} user - The currently authenticated user or null if not authenticated
 * @property {boolean} loading - Indicates whether a login request is in progress
 * @property {string|null} error - Any error message related to authentication, or null if no error
 */

/** @type {AuthState} */
const initialState = {
   user: null,
   loading: false,
   error: null
};

/**
 * Auth Reducer
 *
 * Manages the authentication state of the application.
 *
 * @param {AuthState} state - The current state
 * @param {Object} action - The action object
 * @returns {AuthState} The new state
 */
const loginReducer = (state = initialState, action) => {
   switch (action.type) {
      case LOGIN_REQUEST:
         return {...state, loading: true, error: null};
      case LOGIN_SUCCESS:
         return {...state, loading: false, user: action.payload, error: null};
      case LOGIN_FAILURE:
         return {...state, loading: false, user: null, error: action.payload};
      case 'LOGOUT':
         return {...initialState};
      default:
         return state;
   }
};

export default loginReducer;