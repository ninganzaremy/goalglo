import apiService from '../../services/apiService';

export const FETCH_RECENT_TRANSACTIONS_REQUEST = 'FETCH_RECENT_TRANSACTIONS_REQUEST';
export const FETCH_RECENT_TRANSACTIONS_SUCCESS = 'FETCH_RECENT_TRANSACTIONS_SUCCESS';
export const FETCH_RECENT_TRANSACTIONS_FAILURE = 'FETCH_RECENT_TRANSACTIONS_FAILURE';
export const ADD_TRANSACTION = 'ADD_TRANSACTION';

/*
 * Fetches the recent transactions from the API
 * @returns {Function} A Redux thunk action creator
 */
export const fetchRecentTransactions = () => async (dispatch) => {
   dispatch({ type: FETCH_RECENT_TRANSACTIONS_REQUEST });
   try {
      const response = await apiService.get('/transactions/recent');
      dispatch({ type: FETCH_RECENT_TRANSACTIONS_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: FETCH_RECENT_TRANSACTIONS_FAILURE, payload: error.message });
   }
};

/*
 * Adds a new transaction to the list of recent transactions
 * @param {Object} transaction The transaction to be added
 * @returns {Object} A Redux action object
 */
export const addTransaction = (transaction) => ({
   type: ADD_TRANSACTION,
   payload: transaction
});