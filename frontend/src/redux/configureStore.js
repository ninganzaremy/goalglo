import {configureStore} from '@reduxjs/toolkit';
import rootReducer from './reducers/rootReducer';
import {shouldEnableDevTools} from '../security/securityConfig';

/**
 * Configure the Redux store
 *
 * This function creates and returns a Redux store using Redux Toolkit's configureStore.
 * It uses the environment utility to determine whether to enable DevTools.
 *
 * @returns {Object} Configured Redux store
 */
const store = configureStore({
   reducer: rootReducer,
   middleware: (getDefaultMiddleware) => getDefaultMiddleware(),
   devTools: shouldEnableDevTools(),
});

export default store;