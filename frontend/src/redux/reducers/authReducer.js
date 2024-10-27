import {
   CHECK_TOKEN_EXPIRATION,
   CLEAR_LOGIN_ERROR,
   LOGIN_FAILURE,
   LOGIN_REQUEST,
   LOGIN_SUCCESS,
   LOGOUT
} from '../actions/loginAction';
import {
   PASSWORD_RESET_CONFIRM_FAILURE,
   PASSWORD_RESET_CONFIRM_REQUEST,
   PASSWORD_RESET_CONFIRM_SUCCESS,
   PASSWORD_RESET_FAILURE,
   PASSWORD_RESET_REQUEST,
   PASSWORD_RESET_SUCCESS
} from "../actions/authActions.js";

const initialState = {
   isAuthenticated: false,
   user: null,
   loading: false,
   error: null,
   passwordResetConfirm: {
      loading: false,
      success: false,
      error: null
   },
   passwordReset: {
      loading: false,
      success: false,
      error: null
   }
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
         return {...state, loading: true, error: null};
      case LOGIN_SUCCESS:
         return {
            ...state,
            isAuthenticated: true,
            user: action.payload,
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
      case PASSWORD_RESET_CONFIRM_REQUEST:
         return {
            ...state,
            passwordResetConfirm: {
               loading: true,
               success: false,
               error: null,
            },
         };
      case PASSWORD_RESET_CONFIRM_SUCCESS:
         return {
            ...state,
            passwordResetConfirm: {
               loading: false,
               success: true,
               error: null,
            },
         };
      case PASSWORD_RESET_CONFIRM_FAILURE:
         return {
            ...state,
            passwordResetConfirm: {
               loading: false,
               success: false,
               error: action.payload,
            },
         };
      case PASSWORD_RESET_REQUEST:
         return {
            ...state,
            passwordReset: {
               loading: true,
               success: false,
               error: null
            }
         };
      case PASSWORD_RESET_SUCCESS:
         return {
            ...state,
            passwordReset: {
               loading: false,
               success: true,
               error: null
            }
         };

      case CHECK_TOKEN_EXPIRATION:
         if (state.user && state.user.exp * 1000 < Date.now()) {
            return {
               ...initialState,
               isAuthenticated: false,
               user: null
            };
         }
         return state;

      case PASSWORD_RESET_FAILURE:
         return {
            ...state,
            passwordReset: {
               loading: false,
               success: false,
               error: action.payload
            }
         };
      default:
         return state;
   }
};
export default authReducer;