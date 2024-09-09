// src/redux/actions/serviceActions.js
import apiService from "../../services/apiService";

export const FETCH_SERVICES_REQUEST = "FETCH_SERVICES_REQUEST";
export const FETCH_SERVICES_SUCCESS = "FETCH_SERVICES_SUCCESS";
export const FETCH_SERVICES_FAILURE = "FETCH_SERVICES_FAILURE";
export const BOOK_SERVICE_SUCCESS = "BOOK_SERVICE_SUCCESS";
export const BOOK_SERVICE_FAILURE = "BOOK_SERVICE_FAILURE";

export const FETCH_SERVICE_BY_ID_REQUEST = "FETCH_SERVICE_BY_ID_REQUEST";
export const FETCH_SERVICE_BY_ID_SUCCESS = "FETCH_SERVICE_BY_ID_SUCCESS";
export const FETCH_SERVICE_BY_ID_FAILURE = "FETCH_SERVICE_BY_ID_FAILURE";

export const UPDATE_SERVICE_REQUEST = "UPDATE_SERVICE_REQUEST";
export const UPDATE_SERVICE_SUCCESS = "UPDATE_SERVICE_SUCCESS";
export const UPDATE_SERVICE_FAILURE = "UPDATE_SERVICE_FAILURE";

/**
 * Action creator for fetching services.
 *
 * This action creator makes an API request to fetch all services and dispatches
 * appropriate actions based on the response.
 */
export const fetchServices = () => async (dispatch) => {
   dispatch({type: FETCH_SERVICES_REQUEST});
   try {
      const response = await apiService.get("/services");
      dispatch({type: FETCH_SERVICES_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: FETCH_SERVICES_FAILURE, payload: error.message});
   }
};

export const fetchServiceById = (id) => async (dispatch) => {
   dispatch({type: FETCH_SERVICE_BY_ID_REQUEST});
   try {
      const response = await apiService.get(`/services/${id}`);
      dispatch({type: FETCH_SERVICE_BY_ID_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: FETCH_SERVICE_BY_ID_FAILURE, payload: error.message});
   }
};

export const updateService = (id, serviceData) => async (dispatch) => {
   dispatch({type: UPDATE_SERVICE_REQUEST});
   try {
      const response = await apiService.put(`/admin-actions/update-service/${id}`, serviceData);
      dispatch({type: UPDATE_SERVICE_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: UPDATE_SERVICE_FAILURE, payload: error.message});
   }
};