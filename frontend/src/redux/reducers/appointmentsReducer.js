import {
   BOOK_APPOINTMENT_FAILURE,
   BOOK_APPOINTMENT_REQUEST,
   BOOK_APPOINTMENT_SUCCESS,
   CANCEL_APPOINTMENT_FAILURE,
   CANCEL_APPOINTMENT_REQUEST,
   CANCEL_APPOINTMENT_SUCCESS,
   FETCH_APPOINTMENTS_FAILURE,
   FETCH_APPOINTMENTS_REQUEST,
   FETCH_APPOINTMENTS_SUCCESS,
   FETCH_AVAILABLE_SLOTS_FAILURE,
   FETCH_AVAILABLE_SLOTS_REQUEST,
   FETCH_AVAILABLE_SLOTS_SUCCESS,
} from '../actions/appointmentActions';


const initialState = {
   appointments: [],
   availableTimeSlots: [],
   loading: false,
   error: null,
};

/**
 * Reducer function to handle appointment-related actions.
 *
 * @param {Object} state - The current state of the reducer.
 * @param {Object} action - The action object containing the type and payload.
 * @returns {Object} The new state after applying the action.
 */
const appointmentsReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_APPOINTMENTS_REQUEST:
      case FETCH_AVAILABLE_SLOTS_REQUEST:
      case BOOK_APPOINTMENT_REQUEST:
      case CANCEL_APPOINTMENT_REQUEST:
         return {
            ...state,
            loading: true,
            error: null,
         };

      case FETCH_APPOINTMENTS_SUCCESS:
         return {
            ...state,
            appointments: action.payload,
            loading: false,
         };

      case FETCH_AVAILABLE_SLOTS_SUCCESS:
         return {
            ...state,
            availableTimeSlots: action.payload,
            loading: false,
         };

      case BOOK_APPOINTMENT_SUCCESS:
         return {
            ...state,
            appointments: [...state.appointments, action.payload],
            loading: false,
         };

      case CANCEL_APPOINTMENT_SUCCESS:
         return {
            ...state,
            appointments: state.appointments.filter(
               (appointment) => appointment.id !== action.payload
            ),
            loading: false,
         };

      case FETCH_APPOINTMENTS_FAILURE:
      case FETCH_AVAILABLE_SLOTS_FAILURE:
      case BOOK_APPOINTMENT_FAILURE:
      case CANCEL_APPOINTMENT_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload,
         };

      default:
         return state;
   }
};

export default appointmentsReducer;