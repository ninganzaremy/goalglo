import {
   FETCH_ACCOUNT_SUMMARY_FAILURE,
   FETCH_ACCOUNT_SUMMARY_REQUEST,
   FETCH_ACCOUNT_SUMMARY_SUCCESS,
   UPDATE_ACCOUNT_BALANCE
} from '../actions/accountSummaryActions';

const initialState = {
   balance: 0,
   income: 0,
   expenses: 0,
   loading: false,
   error: null
};

const accountSummaryReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_ACCOUNT_SUMMARY_REQUEST:
         return { ...state, loading: true, error: null };
      case FETCH_ACCOUNT_SUMMARY_SUCCESS:
         return {
            ...state,
            loading: false,
            balance: action.payload.balance,
            income: action.payload.income,
            expenses: action.payload.expenses
         };
      case FETCH_ACCOUNT_SUMMARY_FAILURE:
         return { ...state, loading: false, error: action.payload };
      case UPDATE_ACCOUNT_BALANCE:
         return { ...state, balance: action.payload };
      default:
         return state;
   }
};

export default accountSummaryReducer;