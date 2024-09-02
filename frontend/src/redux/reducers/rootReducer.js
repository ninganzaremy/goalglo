import {combineReducers} from '@reduxjs/toolkit';
import loginReducer from './loginReducer.js';
import createServiceReducer from './createServiceReducer.js';
import registerReducer from './registerReducer';
import userReducer from "./userReducer.js";
import serviceReducer from "./serviceReducer.js";
import blogReducer from "./blogReducer.js";
import contactReducer from "./contactReducer.js";
import goalReducer from "./goalReducer.js";
import accountSummaryReducer from "./accountSummaryReducer.js";
import recentTransactionsReducer from "./recentTransactionsReducer.js";

/**
 * Root Reducer
 *
 * Combines all reducers into a single reducer function.
 * Add additional reducers here as your application grows.
 */
const rootReducer = combineReducers({
   login: loginReducer,
   createService: createServiceReducer,
   register: registerReducer,
   user: userReducer,
   services: serviceReducer,
   blog: blogReducer,
   contact: contactReducer,
   goals: goalReducer,
   accountSummary: accountSummaryReducer,
   recentTransactions: recentTransactionsReducer


});

export default rootReducer;