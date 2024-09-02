import apiService from '../../services/apiService';

/*
 * Action Types
 * These constants represent the different types of actions that can be dispatched
 * in the blogActions file.
 */

/**
 * Action type for fetching blog posts request
 * This action is dispatched when a request is made to fetch blog posts
 */
export const FETCH_BLOG_POSTS_REQUEST = 'FETCH_BLOG_POSTS_REQUEST';
export const FETCH_BLOG_POSTS_SUCCESS = 'FETCH_BLOG_POSTS_SUCCESS';
export const FETCH_BLOG_POSTS_FAILURE = 'FETCH_BLOG_POSTS_FAILURE';

/**
 * Action type for creating a blog post request
 * This action is dispatched when a request is made to create a new blog post
 */
export const CREATE_BLOG_POST_REQUEST = 'CREATE_BLOG_POST_REQUEST';
export const CREATE_BLOG_POST_SUCCESS = 'CREATE_BLOG_POST_SUCCESS';
export const CREATE_BLOG_POST_FAILURE = 'CREATE_BLOG_POST_FAILURE';


/**
 * Action creator for fetching blog posts
 * @param {number} page - The page number to fetch
 * @returns {Function} - A thunk function that dispatches the appropriate actions
 */
export const fetchBlogPosts = (page = 1) => {
   return async (dispatch) => {
      dispatch({ type: FETCH_BLOG_POSTS_REQUEST });

      try {
         const response = await apiService.get(`/blog-posts?page=${page}`);
         dispatch({
            type: FETCH_BLOG_POSTS_SUCCESS,
            payload: response.data
         });
      } catch (error) {
         dispatch({
            type: FETCH_BLOG_POSTS_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
      }
   };
};

/**
 * Action creator for creating a blog post
 * @param {Object} postData - The data for the new blog post
 * @returns {Function} - A thunk function that dispatches the appropriate actions
 */
export const createBlogPost = (postData) => {
   return async (dispatch) => {
      dispatch({ type: CREATE_BLOG_POST_REQUEST });

      try {
         const response = await apiService.post('/blog-posts', postData);
         dispatch({
            type: CREATE_BLOG_POST_SUCCESS,
            payload: response.data
         });
         return response.data;
      } catch (error) {
         dispatch({
            type: CREATE_BLOG_POST_FAILURE,
            payload: error.response ? error.response.data.message : 'An unexpected error occurred'
         });
         throw error;
      }
   };
};