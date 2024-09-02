import {FETCH_USER_DATA_FAILURE, FETCH_USER_DATA_REQUEST, FETCH_USER_DATA_SUCCESS} from '../actions/userActions.js';

/**
 * Reducer function for managing user data state.
 * @param {Object} state - The current state.
 * @param {Object} action - The action object.
 * @returns {Object} The new state.
 */
const initialState = {
   user: null,
   loading: false,
   error: null
};

const userReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_USER_DATA_REQUEST:
         return {
            ...state,
            loading: true,
            error: null
         };
      case FETCH_USER_DATA_SUCCESS:
         return {
            ...state,
            loading: false,
            user: action.payload,
            error: null
         };
      case FETCH_USER_DATA_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload
         };
      default:
         return state;
   }
};

export default userReducer;