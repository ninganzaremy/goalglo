import {
   FETCH_USER_DATA_FAILURE,
   FETCH_USER_DATA_REQUEST,
   FETCH_USER_DATA_SUCCESS,
   VERIFY_EMAIL_FAILURE,
   VERIFY_EMAIL_REQUEST,
   VERIFY_EMAIL_SUCCESS,
} from '../actions/userActions';

const initialState = {
   userData: null,
   loading: false,
   error: null,
};

const userReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_USER_DATA_REQUEST:
         return {...state, loading: true, error: null};
      case FETCH_USER_DATA_SUCCESS:
         return {...state, loading: false, userData: action.payload, error: null};
      case FETCH_USER_DATA_FAILURE:
         return {...state, loading: false, error: action.payload};

      case VERIFY_EMAIL_REQUEST:
         return {
            ...state,
            emailVerification: {
               loading: true,
               success: false,
               message: 'Verifying your email...',
            },
         };
      case VERIFY_EMAIL_SUCCESS:
         return {
            ...state,
            emailVerification: {
               loading: false,
               success: true,
               message: action.payload,
            },
         };
      case VERIFY_EMAIL_FAILURE:
         return {
            ...state,
            emailVerification: {
               loading: false,
               success: false,
               message: action.payload,
            },
         };
      default:
         return state;
   }
};


export default userReducer;