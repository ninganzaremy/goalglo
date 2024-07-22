const initialState = {
   isAuthenticated: false,
   user: null,
   loading: false,
   error: null,
};

const authReducer = (state = initialState, action) => {
   switch (action.type) {
      case 'LOGIN_REQUEST':
         return {...state, loading: true, error: null};
      case 'LOGIN_SUCCESS':
         return {...state, isAuthenticated: true, user: action.payload, loading: false};
      case 'LOGIN_FAILURE':
         return {...state, isAuthenticated: false, user: null, loading: false, error: action.payload};
      case 'LOGOUT':
         return {...state, isAuthenticated: false, user: null, loading: false, error: null};
      default:
         return state;
   }
};

export default authReducer;