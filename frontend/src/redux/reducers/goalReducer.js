import {
   ADD_GOAL_FAILURE,
   ADD_GOAL_SUCCESS,
   DELETE_GOAL_FAILURE,
   DELETE_GOAL_SUCCESS,
   FETCH_GOALS_FAILURE,
   FETCH_GOALS_REQUEST,
   FETCH_GOALS_SUCCESS,
   UPDATE_GOAL_FAILURE,
   UPDATE_GOAL_SUCCESS
} from '../actions/goalActions';

/**
 * Initial state for the goals reducer.
 */
const initialState = {
   goals: [],
   loading: false,
   error: null
};

/**
 * Reducer function for managing the state of goals.
 * @param {Object} state - The current state of goals.
 * @param {Object} action - The action object containing the type and payload.
 * @returns {Object} The new state of goals.
 */
const goalReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_GOALS_REQUEST:
         return { ...state, loading: true, error: null };
      case FETCH_GOALS_SUCCESS:
         return { ...state, loading: false, goals: action.payload, error: null };
      case FETCH_GOALS_FAILURE:
         return { ...state, loading: false, error: action.payload };
      case ADD_GOAL_SUCCESS:
         return { ...state, goals: [...state.goals, action.payload], error: null };
      case ADD_GOAL_FAILURE:
         return { ...state, error: action.payload };
      case UPDATE_GOAL_SUCCESS:
         return {
            ...state,
            goals: state.goals.map(goal => goal.id === action.payload.id ? action.payload : goal),
            error: null
         };
      case UPDATE_GOAL_FAILURE:
         return { ...state, error: action.payload };
      case DELETE_GOAL_SUCCESS:
         return {
            ...state,
            goals: state.goals.filter(goal => goal.id !== action.payload),
            error: null
         };
      case DELETE_GOAL_FAILURE:
         return { ...state, error: action.payload };
      default:
         return state;
   }
};

export default goalReducer;