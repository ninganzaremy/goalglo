import {
   SEND_CONTACT_MESSAGE_FAILURE,
   SEND_CONTACT_MESSAGE_REQUEST,
   SEND_CONTACT_MESSAGE_SUCCESS
} from '../actions/contactActions';

/*

   This reducer is used to handle the state of the contact form.
   It has three possible states: loading, success, and error.
   The initial state is set to loading: false, error: null, and success: false.

   The reducer takes three actions:
   - SEND_CONTACT_MESSAGE_REQUEST: sets loading to true, error to null, and success to false
   - SEND_CONTACT_MESSAGE_SUCCESS: sets loading to false, error to null, and success to true
   - SEND_CONTACT_MESSAGE_FAILURE: sets loading to false, error to the error message, and success to false

   The default case returns the current state.

   This reducer is used in the contact component to handle the state of the contact form.

*/

const initialState = {
   loading: false,
   error: null,
   success: false
};

const contactReducer = (state = initialState, action) => {
   switch (action.type) {
      case SEND_CONTACT_MESSAGE_REQUEST:
         return {
            ...state,
            loading: true,
            error: null,
            success: false
         };
      case SEND_CONTACT_MESSAGE_SUCCESS:
         return {
            ...state,
            loading: false,
            error: null,
            success: true
         };
      case SEND_CONTACT_MESSAGE_FAILURE:
         return {
            ...state,
            loading: false,
            error: action.payload,
            success: false
         };
      default:
         return state;
   }
};

export default contactReducer;