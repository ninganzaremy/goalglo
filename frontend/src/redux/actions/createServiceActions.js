import apiService from "../../services/apiService.js";

export const CREATE_SERVICE_REQUEST = 'CREATE_SERVICE_REQUEST';
export const CREATE_SERVICE_SUCCESS = 'CREATE_SERVICE_SUCCESS';
export const CREATE_SERVICE_FAILURE = 'CREATE_SERVICE_FAILURE';

/**
 * Action creator for creating a new service.
 *
 * This action creator makes an API request to create a new service and dispatches
 * corresponding actions based on the result of the request.
 *
 * @param {Object} serviceData - The data for the service to be created.
 * @returns {Function} A thunk function that dispatches actions based on API response.
 * @throws Will throw an error if the API request fails.
 */
export const createService = (serviceData) => {
   return async (dispatch) => {
      dispatch({type: CREATE_SERVICE_REQUEST});

      try {
         const response = await apiService.post('/admin-actions/create-service', serviceData);

         dispatch({
            type: CREATE_SERVICE_SUCCESS,
            payload: response.data
         });

         return response.data;
      } catch (error) {
         dispatch({
            type: CREATE_SERVICE_FAILURE,
            payload: error.response?.data?.message || 'An error occurred while creating the service'
         });

         throw error;
      }
   };
};