import apiService from '../../services/apiService';
/*
 * action types
 */
export const FETCH_USER_DATA_REQUEST = 'FETCH_USER_DATA_REQUEST';
export const FETCH_USER_DATA_SUCCESS = 'FETCH_USER_DATA_SUCCESS';
export const FETCH_USER_DATA_FAILURE = 'FETCH_USER_DATA_FAILURE';

/**
 * Action creator for fetching user data.
 *
 * This action creator makes an API request to fetch the user's data and dispatches
 * appropriate actions based on the result of the request.
 *
 * @returns {Function} A thunk function that dispatches actions based on API response.
 * @throws Will throw an error if the API request fails.
 */
export const fetchUserData = () => {
   return async (dispatch) => {
      dispatch({ type: FETCH_USER_DATA_REQUEST });

      try {
         const response = await apiService.get('/users/me');
         dispatch({
            type: FETCH_USER_DATA_SUCCESS,
            payload: response.data
         });
      } catch (error) {
         dispatch({
            type: FETCH_USER_DATA_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
      }
   };
};