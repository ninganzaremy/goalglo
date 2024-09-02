import {
   CREATE_SERVICE_FAILURE,
   CREATE_SERVICE_REQUEST,
   CREATE_SERVICE_SUCCESS
} from '../actions/createServiceActions.js';

/**
 * Initial state for the service reducer.
 *
 * @type {Object}
 * @property {Array} services - The list of services.
 * @property {boolean} loading - Indicates if the service creation is in progress.
 * @property {string|null} error - Holds the error message if service creation fails.
 */
const initialState = {
   services: [],
   loading: false,
   error: null
};

/**
 * Service reducer to manage the state of services.
 *
 * @param {Object} state - The current state.
 * @param {Object} action - The dispatched action.
 * @returns {Object} The updated state.
 */
const createServiceReducer = (state = initialState, action) => {
   switch (action.type) {
      case CREATE_SERVICE_REQUEST:
         return {
            ...state,
            loading: true,
            error: null
         };
      case CREATE_SERVICE_SUCCESS:
         return {
            ...state,
            loading: false,
            services: [...state.services, action.payload],
            error: null
         };
      case CREATE_SERVICE_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload
         };
      default:
         return state;
   }
};

export default createServiceReducer;