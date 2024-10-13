import apiService from '../../services/apiService';


export const SEND_CONTACT_MESSAGE_REQUEST = 'SEND_CONTACT_MESSAGE_REQUEST';
export const SEND_CONTACT_MESSAGE_SUCCESS = 'SEND_CONTACT_MESSAGE_SUCCESS';
export const SEND_CONTACT_MESSAGE_FAILURE = 'SEND_CONTACT_MESSAGE_FAILURE';

/**
 * Action creator for sending a contact message.
 *
 * @param {Object} formData - The form data containing the message details.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const sendContactMessage = (formData) => {
   return async (dispatch) => {
      dispatch({ type: SEND_CONTACT_MESSAGE_REQUEST });

      try {
         const response = await apiService.post('/contact-messages', formData);
         dispatch({
            type: SEND_CONTACT_MESSAGE_SUCCESS,
            payload: response.data
         });
      } catch (error) {
         dispatch({
            type: SEND_CONTACT_MESSAGE_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
      }
   };
};