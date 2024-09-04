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
import appointmentsReducer from "./appointmentsReducer.js";
import authReducer from "./authReducer.js";

/**
 * Root reducer for the application.
 * Combines all the reducers into a single root reducer.
 */
const rootReducer = combineReducers({
   auth: authReducer,
   login: loginReducer,
   createService: createServiceReducer,
   register: registerReducer,
   user: userReducer,
   services: serviceReducer,
   blog: blogReducer,
   contact: contactReducer,
   goals: goalReducer,
   accountSummary: accountSummaryReducer,
   recentTransactions: recentTransactionsReducer,
   appointments: appointmentsReducer
});

export default rootReducer;