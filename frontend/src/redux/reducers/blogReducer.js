import {
   CREATE_BLOG_POST_FAILURE,
   CREATE_BLOG_POST_REQUEST,
   CREATE_BLOG_POST_SUCCESS,
   DELETE_BLOG_POST_FAILURE,
   DELETE_BLOG_POST_REQUEST,
   DELETE_BLOG_POST_SUCCESS,
   FETCH_BLOG_POST_BY_ID_FAILURE,
   FETCH_BLOG_POST_BY_ID_REQUEST,
   FETCH_BLOG_POST_BY_ID_SUCCESS,
   FETCH_BLOG_POSTS_FAILURE,
   FETCH_BLOG_POSTS_REQUEST,
   FETCH_BLOG_POSTS_SUCCESS,
   FETCH_LATEST_BLOG_POSTS_SUCCESS,
   UPDATE_BLOG_POST_FAILURE,
   UPDATE_BLOG_POST_REQUEST,
   UPDATE_BLOG_POST_SUCCESS,
} from "../actions/blogActions";

/**
 * Reducer function for managing the state of the blog page.
 * @param {Object} state - The current state of the blog page.
 * @param {Object} action - The action object.
 * @returns {Object} - The new state of the blog page.
 */
const initialState = {
   posts: [],
   latestBlogPosts: [],
   currentPost: null,
   loading: false,
   error: null,
   totalPages: 0,
};
const blogReducer = (state = initialState, action) => {
   switch (action.type) {
      case FETCH_BLOG_POSTS_REQUEST:
      case FETCH_BLOG_POST_BY_ID_REQUEST:
      case UPDATE_BLOG_POST_REQUEST:
      case CREATE_BLOG_POST_REQUEST:
      case DELETE_BLOG_POST_REQUEST:
         return { ...state, loading: true, error: null };

      case FETCH_BLOG_POSTS_SUCCESS:
         return {
            ...state,
            loading: false,
            posts: action.payload,
            totalPages: action.payload.totalPages,
         };
      case FETCH_LATEST_BLOG_POSTS_SUCCESS:
         return {
            ...state,
            loading: false,
            latestBlogPosts: action.payload,
         };
      case FETCH_BLOG_POST_BY_ID_SUCCESS:
         return {...state, loading: false, currentPost: action.payload};

      case UPDATE_BLOG_POST_SUCCESS:
         return {
            ...state,
            loading: false,
            posts: state.posts.map((post) =>
               post.id === action.payload.id ? action.payload : post
            ),
            currentPost: action.payload,
         };


      case CREATE_BLOG_POST_SUCCESS:
         return {
            ...state,
            loading: false,
            posts: [action.payload, ...state.posts],
         };

      case DELETE_BLOG_POST_SUCCESS:
         return {
            ...state,
            loading: false,
            posts: state.posts.filter((post) => post.id !== action.payload),
            currentPost:
               state.currentPost && state.currentPost.id === action.payload
                  ? null
                  : state.currentPost,
         };

      case FETCH_BLOG_POSTS_FAILURE:
      case FETCH_BLOG_POST_BY_ID_FAILURE:
      case UPDATE_BLOG_POST_FAILURE:
      case CREATE_BLOG_POST_FAILURE:
      case DELETE_BLOG_POST_FAILURE:
         return {...state, loading: false, error: action.payload};

      default:
         return state;
   }
};

export default blogReducer;