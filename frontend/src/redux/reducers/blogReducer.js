import {
   CREATE_BLOG_POST_FAILURE,
   CREATE_BLOG_POST_REQUEST,
   CREATE_BLOG_POST_SUCCESS,
   FETCH_BLOG_POSTS_FAILURE,
   FETCH_BLOG_POSTS_REQUEST,
   FETCH_BLOG_POSTS_SUCCESS
} from '../actions/blogActions';

/**
 * Reducer function for managing the state of the blog page.
 * @param {Object} state - The current state of the blog page.
 * @param {Object} action - The action object.
 * @returns {Object} - The new state of the blog page.
 */
const initialState = {
   posts: [],
   loading: false,
   error: null,
   creating: false,
   createError: null
};


const blogReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_BLOG_POSTS_REQUEST:
         return { ...state, loading: true, error: null };
      case FETCH_BLOG_POSTS_SUCCESS:
         return { ...state, loading: false, posts: action.payload, error: null };
      case FETCH_BLOG_POSTS_FAILURE:
         return { ...state, loading: false, error: action.payload };
      case CREATE_BLOG_POST_REQUEST:
         return { ...state, creating: true, createError: null };
      case CREATE_BLOG_POST_SUCCESS:
         return {
            ...state,
            creating: false,
            posts: [action.payload, ...state.posts],
            createError: null
         };
      case CREATE_BLOG_POST_FAILURE:
         return { ...state, creating: false, createError: action.payload };
      default:
         return state;
   }
};

export default blogReducer;