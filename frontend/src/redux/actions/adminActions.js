import apiService from "../../services/apiService.js";

export const FETCH_ALL_TESTIMONIALS_REQUEST = 'FETCH_ALL_TESTIMONIALS_REQUEST';
export const FETCH_ALL_TESTIMONIALS_SUCCESS = 'FETCH_ALL_TESTIMONIALS_SUCCESS';
export const FETCH_ALL_TESTIMONIALS_FAILURE = 'FETCH_ALL_TESTIMONIALS_FAILURE';

export const UPDATE_TESTIMONIAL_STATUS_REQUEST = 'UPDATE_TESTIMONIAL_STATUS_REQUEST';
export const UPDATE_TESTIMONIAL_STATUS_SUCCESS = 'UPDATE_TESTIMONIAL_STATUS_SUCCESS';
export const UPDATE_TESTIMONIAL_STATUS_FAILURE = 'UPDATE_TESTIMONIAL_STATUS_FAILURE';


export const fetchAllTestimonials = () => async (dispatch) => {
   dispatch({type: FETCH_ALL_TESTIMONIALS_REQUEST});
   try {
      const response = await apiService.get('/admin/testimonials/all');
      dispatch({type: FETCH_ALL_TESTIMONIALS_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: FETCH_ALL_TESTIMONIALS_FAILURE, payload: error.message});
   }
};

export const updateTestimonialStatus = (id, status) => async (dispatch) => {
   dispatch({type: UPDATE_TESTIMONIAL_STATUS_REQUEST});
   try {
      const response = await apiService.put(`/admin/testimonials/${id}/status`, {status});
      dispatch({type: UPDATE_TESTIMONIAL_STATUS_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: UPDATE_TESTIMONIAL_STATUS_FAILURE, payload: error.message});
   }
};