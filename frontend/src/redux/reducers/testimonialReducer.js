import {
   CREATE_TESTIMONIAL_FAILURE,
   CREATE_TESTIMONIAL_REQUEST,
   CREATE_TESTIMONIAL_SUCCESS,
   FETCH_TESTIMONIALS_FAILURE,
   FETCH_TESTIMONIALS_REQUEST,
   FETCH_TESTIMONIALS_SUCCESS,
   UPDATE_TESTIMONIAL_FAILURE,
   UPDATE_TESTIMONIAL_REQUEST,
   UPDATE_TESTIMONIAL_SUCCESS
} from '../actions/testimonialActions';
import {
   FETCH_ALL_TESTIMONIALS_FAILURE,
   FETCH_ALL_TESTIMONIALS_REQUEST,
   FETCH_ALL_TESTIMONIALS_SUCCESS,
   UPDATE_TESTIMONIAL_STATUS_FAILURE,
   UPDATE_TESTIMONIAL_STATUS_REQUEST,
   UPDATE_TESTIMONIAL_STATUS_SUCCESS
} from "../actions/adminActions.js";

const initialState = {
   testimonials: [],
   allTestimonials: [],
   loading: false,
   error: null
};

const testimonialReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_TESTIMONIALS_REQUEST:
      case CREATE_TESTIMONIAL_REQUEST:
      case UPDATE_TESTIMONIAL_REQUEST:
      case FETCH_ALL_TESTIMONIALS_REQUEST:
      case UPDATE_TESTIMONIAL_STATUS_REQUEST:
         return {
            ...state,
            loading: true,
            error: null
         };
      case FETCH_TESTIMONIALS_SUCCESS:
         return {
            ...state,
            loading: false,
            testimonials: action.payload
         };
      case CREATE_TESTIMONIAL_SUCCESS:
         return {
            ...state,
            loading: false,
            testimonials: [...state.testimonials, action.payload]
         };
      case UPDATE_TESTIMONIAL_SUCCESS:
         return {
            ...state,
            loading: false,
            testimonials: state.testimonials.map(testimonial =>
               testimonial.id === action.payload.id ? action.payload : testimonial
            )
         };
      case FETCH_ALL_TESTIMONIALS_SUCCESS:
         return {
            ...state,
            loading: false,
            allTestimonials: action.payload
         };
      case UPDATE_TESTIMONIAL_STATUS_SUCCESS:
         return {
            ...state,
            loading: false,
            allTestimonials: state.allTestimonials.map(testimonial =>
               testimonial.id === action.payload.id ? action.payload : testimonial
            ),
            testimonials: state.testimonials.map(testimonial =>
               testimonial.id === action.payload.id ? action.payload : testimonial
            )
         };
      case FETCH_TESTIMONIALS_FAILURE:
      case CREATE_TESTIMONIAL_FAILURE:
      case UPDATE_TESTIMONIAL_FAILURE:
      case FETCH_ALL_TESTIMONIALS_FAILURE:
      case UPDATE_TESTIMONIAL_STATUS_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload
         };
      default:
         return state;
   }
};

export default testimonialReducer;