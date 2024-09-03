import apiService from "../../services/apiService";


// Action Types
export const FETCH_APPOINTMENTS_REQUEST = "FETCH_APPOINTMENTS_REQUEST";
export const FETCH_APPOINTMENTS_SUCCESS = "FETCH_APPOINTMENTS_SUCCESS";
export const FETCH_APPOINTMENTS_FAILURE = "FETCH_APPOINTMENTS_FAILURE";

export const FETCH_AVAILABLE_SLOTS_REQUEST = "FETCH_AVAILABLE_SLOTS_REQUEST";
export const FETCH_AVAILABLE_SLOTS_SUCCESS = "FETCH_AVAILABLE_SLOTS_SUCCESS";
export const FETCH_AVAILABLE_SLOTS_FAILURE = "FETCH_AVAILABLE_SLOTS_FAILURE";

export const BOOK_APPOINTMENT_REQUEST = "BOOK_APPOINTMENT_REQUEST";
export const BOOK_APPOINTMENT_SUCCESS = "BOOK_APPOINTMENT_SUCCESS";
export const BOOK_APPOINTMENT_FAILURE = "BOOK_APPOINTMENT_FAILURE";

export const CANCEL_APPOINTMENT_REQUEST = "CANCEL_APPOINTMENT_REQUEST";
export const CANCEL_APPOINTMENT_SUCCESS = "CANCEL_APPOINTMENT_SUCCESS";
export const CANCEL_APPOINTMENT_FAILURE = "CANCEL_APPOINTMENT_FAILURE";

/**
 * Action creator for fetching all appointments.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const fetchAppointments = () => async (dispatch) => {
   dispatch({ type: FETCH_APPOINTMENTS_REQUEST });
   try {
      const response = await apiService.get("/appointments");
      dispatch({ type: FETCH_APPOINTMENTS_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: FETCH_APPOINTMENTS_FAILURE, payload: error.message });
   }
};


/**
 * Action creator for fetching available time slots.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const fetchAvailableTimeSlots = () => async (dispatch) => {
   dispatch({ type: FETCH_AVAILABLE_SLOTS_REQUEST });
   try {
      const response = await apiService.get(
         "/appointments/slots/available"
      );
      dispatch({ type: FETCH_AVAILABLE_SLOTS_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: FETCH_AVAILABLE_SLOTS_FAILURE, payload: error.message });
   }
};

/**
 * Action creator for booking an appointment.
 * @param {Object} bookingData - The data required for booking an appointment.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const bookAppointment = (bookingData) => async (dispatch) => {
   dispatch({ type: BOOK_APPOINTMENT_REQUEST });
   try {
      const response = await apiService.post(`/appointments/book-appointment/${bookingData.timeSlotId}`, bookingData);
      dispatch({ type: BOOK_APPOINTMENT_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: BOOK_APPOINTMENT_FAILURE, payload: error.message });
   }
};

/**
 * Action creator for canceling an appointment.
 * @param {string} appointmentId - The ID of the appointment to be canceled.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const cancelAppointment = (appointmentId) => async (dispatch) => {
   dispatch({ type: CANCEL_APPOINTMENT_REQUEST });
   try {
      await apiService.delete(`/appointments/${appointmentId}`);
      dispatch({ type: CANCEL_APPOINTMENT_SUCCESS, payload: appointmentId });
   } catch (error) {
      dispatch({ type: CANCEL_APPOINTMENT_FAILURE, payload: error.message });
   }
};