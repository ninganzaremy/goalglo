// src/redux/reducers/createServiceReducer.js
import {FETCH_SERVICES_FAILURE, FETCH_SERVICES_REQUEST, FETCH_SERVICES_SUCCESS} from '../actions/serviceActions';

const initialState = {
  services: [],
  loading: false,
  error: null
};
/*
 * Reducer for managing service-related state in the Redux store.
 * Handles actions related to fetching services.
 */
const serviceReducer = (state = initialState, action) => {
  switch (action.type) {
    case FETCH_SERVICES_REQUEST:
      return {
        ...state,
        loading: true,
        error: null
      };
    case FETCH_SERVICES_SUCCESS:
      return {
        ...state,
        loading: false,
        services: action.payload,
        error: null
      };
    case FETCH_SERVICES_FAILURE:
      return {
        ...state,
        loading: false,
        error: action.payload
      };
    default:
      return state;
  }
};

export default serviceReducer;