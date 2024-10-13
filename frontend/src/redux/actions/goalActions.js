import apiService from "../../services/apiService";

export const FETCH_GOALS_REQUEST = "FETCH_GOALS_REQUEST";
export const FETCH_GOALS_SUCCESS = "FETCH_GOALS_SUCCESS";
export const FETCH_GOALS_FAILURE = "FETCH_GOALS_FAILURE";
export const ADD_GOAL_SUCCESS = "ADD_GOAL_SUCCESS";
export const ADD_GOAL_FAILURE = "ADD_GOAL_FAILURE";
export const UPDATE_GOAL_SUCCESS = "UPDATE_GOAL_SUCCESS";
export const UPDATE_GOAL_FAILURE = "UPDATE_GOAL_FAILURE";
export const DELETE_GOAL_SUCCESS = "DELETE_GOAL_SUCCESS";
export const DELETE_GOAL_FAILURE = "DELETE_GOAL_FAILURE";

/**
 * Action creator for fetching all goals.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const fetchGoals = () => async (dispatch) => {
   dispatch({ type: FETCH_GOALS_REQUEST });
   try {
      const response = await apiService.get("/goals");
      dispatch({ type: FETCH_GOALS_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: FETCH_GOALS_FAILURE, payload: error.message });
   }
};

/**
 * Action creator for adding a new goal.
 * @param {Object} goal - The goal object to be added.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const addGoal = (goal) => async (dispatch) => {
   try {
      const response = await apiService.post("/goals", goal);
      dispatch({ type: ADD_GOAL_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: ADD_GOAL_FAILURE, payload: error.message });
   }
};

/**
 * Action creator for updating an existing goal.
 * @param {Object} goal - The goal object to be updated.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const updateGoal = (id, goal) => async (dispatch) => {
   try {
      const response = await apiService.put(`/goals/${id}`, goal);
      dispatch({ type: UPDATE_GOAL_SUCCESS, payload: response.data });
   } catch (error) {
      dispatch({ type: UPDATE_GOAL_FAILURE, payload: error.message });
   }
};

/**
 * Action creator for deleting a goal.
 * @param {string} goalId - The ID of the goal to be deleted.
 * @returns {Function} A Redux thunk function that dispatches the appropriate actions.
 */
export const deleteGoal = (id) => async (dispatch) => {
   try {
      await apiService.delete(`/goals/${id}`);
      dispatch({ type: DELETE_GOAL_SUCCESS, payload: id });
   } catch (error) {
      dispatch({ type: DELETE_GOAL_FAILURE, payload: error.message });
   }
};
