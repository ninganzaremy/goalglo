import {REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS, RESET_REGISTRATION} from '../actions/registerAction';

/**
 * Reducer function for handling user registration state.
 *
 * @param {Object} state - The current state of the registration process.
 * @param {Object} action - The action object containing the type and payload.
 * @returns {Object} The new state based on the action type.
 */
const initialState = {
   loading: false,
   error: null,
   success: false
};

/**
 * Reducer function for handling user registration state.
 *
 * @param {Object} state - The current state of the registration process.
 * @param {Object} action - The action object containing the type and payload.
 * @returns {Object} The new state based on the action type.
 */
const registerReducer = (state = initialState, action) => {
   switch (action.type) {
      case REGISTER_REQUEST:
         return {
            ...state,
            loading: true,
            error: null,
            success: false
         };
      case REGISTER_SUCCESS:
         return {
            ...state,
            loading: false,
            error: null,
            success: true
         };
      case REGISTER_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload,
            success: false
         };
      case RESET_REGISTRATION:
         return {
            ...state,
            loading: false,
            success: false,
            error: null
         };
      default:
         return state;
   }
};

export default registerReducer;