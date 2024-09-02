import apiService from '../../services/apiService';

export const FETCH_ACCOUNT_SUMMARY_REQUEST = 'FETCH_ACCOUNT_SUMMARY_REQUEST';
export const FETCH_ACCOUNT_SUMMARY_SUCCESS = 'FETCH_ACCOUNT_SUMMARY_SUCCESS';
export const FETCH_ACCOUNT_SUMMARY_FAILURE = 'FETCH_ACCOUNT_SUMMARY_FAILURE';
export const UPDATE_ACCOUNT_BALANCE = 'UPDATE_ACCOUNT_BALANCE';
/*
 * Fetches the account summary from the API
 * @returns {Function} A Redux thunk action creator
 */
export const fetchAccountSummary = () => async (dispatch) => {
   dispatch({ type: FETCH_ACCOUNT_SUMMARY_REQUEST });
   try {
      const response = await apiService.get('/account/summary');
      dispatch({ type: FETCH_ACCOUNT_SUMMARY_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: FETCH_ACCOUNT_SUMMARY_FAILURE, payload: error.message });
   }
};

/*
 * Updates the account balance
 * @param {Number} newBalance The new account balance
 * @returns {Object} A Redux action object
 */
export const updateAccountBalance = (newBalance) => ({
   type: UPDATE_ACCOUNT_BALANCE,
   payload: newBalance
});