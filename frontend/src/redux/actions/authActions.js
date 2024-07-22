import axios from 'axios';

export const loginRequest = () => ({
   type: 'LOGIN_REQUEST',
});

export const loginSuccess = (user) => ({
   type: 'LOGIN_SUCCESS',
   payload: user,
});

export const loginFailure = (error) => ({
   type: 'LOGIN_FAILURE',
   payload: error,
});

export const login = (username, password) => async (dispatch) => {
   dispatch(loginRequest());
   try {
      const response = await axios.post('/api/login', {username, password});
      dispatch(loginSuccess(response.data));
   } catch (error) {
      dispatch(loginFailure(error.response.data));
   }
};

export const logout = () => ({
   type: 'LOGOUT',
});