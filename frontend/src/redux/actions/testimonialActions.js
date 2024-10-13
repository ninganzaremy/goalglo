import apiService from "../../services/apiService.js";

// Action Types
export const FETCH_TESTIMONIALS_REQUEST = 'FETCH_TESTIMONIALS_REQUEST';
export const FETCH_TESTIMONIALS_SUCCESS = 'FETCH_TESTIMONIALS_SUCCESS';
export const FETCH_TESTIMONIALS_FAILURE = 'FETCH_TESTIMONIALS_FAILURE';

export const CREATE_TESTIMONIAL_REQUEST = 'CREATE_TESTIMONIAL_REQUEST';
export const CREATE_TESTIMONIAL_SUCCESS = 'CREATE_TESTIMONIAL_SUCCESS';
export const CREATE_TESTIMONIAL_FAILURE = 'CREATE_TESTIMONIAL_FAILURE';

export const UPDATE_TESTIMONIAL_REQUEST = 'UPDATE_TESTIMONIAL_REQUEST';
export const UPDATE_TESTIMONIAL_SUCCESS = 'UPDATE_TESTIMONIAL_SUCCESS';
export const UPDATE_TESTIMONIAL_FAILURE = 'UPDATE_TESTIMONIAL_FAILURE';


export const fetchTestimonials = () => async (dispatch) => {
   dispatch({type: FETCH_TESTIMONIALS_REQUEST});
   try {
      const response = await apiService.get('/testimonials');
      dispatch({type: FETCH_TESTIMONIALS_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: FETCH_TESTIMONIALS_FAILURE, payload: error.message});
   }
};

export const createTestimonial = (testimonial) => async (dispatch) => {
   dispatch({type: CREATE_TESTIMONIAL_REQUEST});
   try {
      const response = await apiService.post('/testimonials', testimonial);
      dispatch({type: CREATE_TESTIMONIAL_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: CREATE_TESTIMONIAL_FAILURE, payload: error.message});
   }
};

export const updateTestimonial = (id, testimonial) => async (dispatch) => {
   dispatch({type: UPDATE_TESTIMONIAL_REQUEST});
   try {
      const response = await apiService.put(`/testimonials/${id}`, testimonial);
      dispatch({type: UPDATE_TESTIMONIAL_SUCCESS, payload: response.data});
   } catch (error) {
      dispatch({type: UPDATE_TESTIMONIAL_FAILURE, payload: error.message});
   }
};