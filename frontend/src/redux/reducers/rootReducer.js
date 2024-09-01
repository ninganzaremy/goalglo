import {combineReducers} from '@reduxjs/toolkit';
import authReducer from './authReducer';
import serviceReducer from './serviceReducer'; // Import the service reducer

/**
 * Root Reducer
 *
 * Combines all reducers into a single reducer function.
 * Add additional reducers here as your application grows.
 */
const rootReducer = combineReducers({
   auth: authReducer,
   service: serviceReducer
});

export default rootReducer;