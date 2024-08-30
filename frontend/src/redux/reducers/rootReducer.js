import {combineReducers} from '@reduxjs/toolkit';
import authReducer from './authReducer';

/**
 * Root Reducer
 *
 * Combines all reducers into a single reducer function.
 * Add additional reducers here as your application grows.
 */
const rootReducer = combineReducers({
   auth: authReducer,
});

export default rootReducer;