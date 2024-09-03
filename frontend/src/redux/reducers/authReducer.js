import {CLEAR_LOGIN_ERROR, LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT} from '../actions/loginAction';

const initialState = {
   isAuthenticated: false,
   user: null,
   loading: false,
   error: null,
};

/**
 * Reducer for handling authentication state
 *
 * @param {Object} state - The current state
 * @param {Object} action - The action object
 * @returns {Object} - The new state
 */
const authReducer = (state = initialState, action) => {
   switch (action.type) {
      case LOGIN_REQUEST:
         return { ...state, loading: true, error: null };
      case LOGIN_SUCCESS:
         return {
            ...state,
            isAuthenticated: true,
            loading: false,
            error: null,
         };
      case LOGIN_FAILURE:
         return {
            ...state,
            isAuthenticated: false,
            user: null,
            loading: false,
            error: action.payload,
         };
      case LOGOUT:
         return {
            ...state,
            isAuthenticated: false,
            user: null,
            loading: false,
            error: null,
         };
      case CLEAR_LOGIN_ERROR:
         return {
            ...state,
            error: null,
         };
      default:
         return state;
   }
};

export default authReducer;