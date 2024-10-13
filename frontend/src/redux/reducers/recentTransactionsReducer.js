import {
   ADD_TRANSACTION,
   FETCH_RECENT_TRANSACTIONS_FAILURE,
   FETCH_RECENT_TRANSACTIONS_REQUEST,
   FETCH_RECENT_TRANSACTIONS_SUCCESS
} from '../actions/recentTransactionActions';
/*
 * This reducer handles the state for recent transactions.
 * It keeps track of the loading state, any errors, and the list of transactions.
 * The transactions are ordered by date in descending order, with a maximum of 10 transactions.
 * The reducer handles the actions related to fetching recent transactions, adding a new transaction, and handling errors.
 * The initial state is set to an empty array for transactions, loading as false, and error as null.
 */
const initialState = {
   transactions: [],
   loading: false,
   error: null
};

const recentTransactionsReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_RECENT_TRANSACTIONS_REQUEST:
         return { ...state, loading: true, error: null };
      case FETCH_RECENT_TRANSACTIONS_SUCCESS:
         return { ...state, loading: false, transactions: action.payload };
      case FETCH_RECENT_TRANSACTIONS_FAILURE:
         return { ...state, loading: false, error: action.payload };
      case ADD_TRANSACTION:
         return {
            ...state,
            transactions: [action.payload, ...state.transactions].slice(0, 10)
         };
      default:
         return state;
   }
};

export default recentTransactionsReducer;