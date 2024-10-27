import {
   FETCH_SERVICE_BY_ID_FAILURE,
   FETCH_SERVICE_BY_ID_REQUEST,
   FETCH_SERVICE_BY_ID_SUCCESS,
   FETCH_SERVICES_FAILURE,
   FETCH_SERVICES_REQUEST,
   FETCH_SERVICES_SUCCESS,
   UPDATE_SERVICE_FAILURE,
   UPDATE_SERVICE_REQUEST,
   UPDATE_SERVICE_SUCCESS,
} from "../actions/serviceActions";

const initialState = {
   services: [],
   currentService: null,
   loading: false,
   error: null,
};

/*
*
 * Reducer function for managing service state
 * @param {Object} state - The current state
 * @param {Object} action - The action object
 * @returns {Object} The new state
 */
const serviceReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_SERVICES_REQUEST:
      case FETCH_SERVICE_BY_ID_REQUEST:
      case UPDATE_SERVICE_REQUEST:
         return {
            ...state,
            loading: true,
            error: null,
         };
      case FETCH_SERVICES_SUCCESS:
         return {
            ...state,
            loading: false,
            services: action.payload,
            error: null,
         };
      case FETCH_SERVICE_BY_ID_SUCCESS:
         return {
            ...state,
            loading: false,
            currentService: action.payload,
            error: null,
         };
      case UPDATE_SERVICE_SUCCESS:
         return {
            ...state,
            loading: false,
            services: state.services.map((service) =>
               service.id === action.payload.id ? action.payload : service
            ),
            currentService: action.payload,
            error: null,
         };
      case FETCH_SERVICES_FAILURE:
      case FETCH_SERVICE_BY_ID_FAILURE:
      case UPDATE_SERVICE_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload,
         };
      default:
         return state;
   }
};

export default serviceReducer;