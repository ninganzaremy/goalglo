import apiService from "../../services/apiService";

/*
 * Action Types
 * These constants represent the different types of actions that can be dispatched
 * in the blogActions file.
 */

/**
 * Action type for fetching blog posts request
 * This action is dispatched when a request is made to fetch blog posts
 */
export const FETCH_BLOG_POSTS_REQUEST = "FETCH_BLOG_POSTS_REQUEST";
export const FETCH_BLOG_POSTS_SUCCESS = "FETCH_BLOG_POSTS_SUCCESS";
export const FETCH_BLOG_POSTS_FAILURE = "FETCH_BLOG_POSTS_FAILURE";

/**
 * Action type for creating a blog post request
 * This action is dispatched when a request is made to create a new blog post
 */
export const CREATE_BLOG_POST_REQUEST = "CREATE_BLOG_POST_REQUEST";
export const CREATE_BLOG_POST_SUCCESS = "CREATE_BLOG_POST_SUCCESS";
export const CREATE_BLOG_POST_FAILURE = "CREATE_BLOG_POST_FAILURE";

export const FETCH_PUBLISHED_BLOG_POSTS_REQUEST =
   "FETCH_PUBLISHED_BLOG_POSTS_REQUEST";
export const FETCH_PUBLISHED_BLOG_POSTS_SUCCESS =
   "FETCH_PUBLISHED_BLOG_POSTS_SUCCESS";
export const FETCH_PUBLISHED_BLOG_POSTS_FAILURE =
   "FETCH_PUBLISHED_BLOG_POSTS_FAILURE";

export const FETCH_LATEST_BLOG_POSTS_REQUEST =
   "FETCH_LATEST_BLOG_POSTS_REQUEST";
export const FETCH_LATEST_BLOG_POSTS_SUCCESS =
   "FETCH_LATEST_BLOG_POSTS_SUCCESS";
export const FETCH_LATEST_BLOG_POSTS_FAILURE =
   "FETCH_LATEST_BLOG_POSTS_FAILURE";

export const UPDATE_BLOG_POST_REQUEST = "UPDATE_BLOG_POST_REQUEST";
export const UPDATE_BLOG_POST_SUCCESS = "UPDATE_BLOG_POST_SUCCESS";
export const UPDATE_BLOG_POST_FAILURE = "UPDATE_BLOG_POST_FAILURE";

export const FETCH_BLOG_POST_BY_ID_REQUEST = "FETCH_BLOG_POST_BY_ID_REQUEST";
export const FETCH_BLOG_POST_BY_ID_SUCCESS = "FETCH_BLOG_POST_BY_ID_SUCCESS";
export const FETCH_BLOG_POST_BY_ID_FAILURE = "FETCH_BLOG_POST_BY_ID_FAILURE";

export const DELETE_BLOG_POST_REQUEST = "DELETE_BLOG_POST_REQUEST";
export const DELETE_BLOG_POST_SUCCESS = "DELETE_BLOG_POST_SUCCESS";
export const DELETE_BLOG_POST_FAILURE = "DELETE_BLOG_POST_FAILURE";

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
            payload: response.data,
         });
      } catch (error) {
         dispatch({
            type: FETCH_BLOG_POSTS_FAILURE,
            payload: error.response
               ? error.response.data.message
               : "An unexpected error occurred",
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
         const response = await apiService.post("/blog-posts", postData, {
            headers: {
               "Content-Type": "multipart/form-data",
            },
         });
         dispatch({
            type: CREATE_BLOG_POST_SUCCESS,
            payload: response.data,
         });
      } catch (error) {
         dispatch({
            type: CREATE_BLOG_POST_FAILURE,
            payload: error.response
               ? error.response.data.message
               : "An unexpected error occurred",
         });
      }
   };
};

export const updateBlogPost = (id, formData) => async (dispatch) => {
   dispatch({type: UPDATE_BLOG_POST_REQUEST});
   try {
      const response = await apiService.put(`/blog-posts/${id}`, formData, {
         headers: {
            "Content-Type": "multipart/form-data",
         },
      });
      dispatch({
         type: UPDATE_BLOG_POST_SUCCESS,
         payload: response.data,
      });
   } catch (error) {
      dispatch({
         type: UPDATE_BLOG_POST_FAILURE,
         payload: error.message,
      });
   }
};

export const fetchBlogPostById = (id) => async (dispatch) => {
   dispatch({type: FETCH_BLOG_POST_BY_ID_REQUEST});
   try {
      const response = await apiService.get(`/blog-posts/${id}`);
      dispatch({
         type: FETCH_BLOG_POST_BY_ID_SUCCESS,
         payload: response.data,
      });
   } catch (error) {
      dispatch({
         type: FETCH_BLOG_POST_BY_ID_FAILURE,
         payload: error.message,
      });
   }
};

export const fetchPublishedBlogPosts = () => async (dispatch) => {
   dispatch({type: FETCH_PUBLISHED_BLOG_POSTS_REQUEST});
   try {
      const response = await apiService.get("/blog-posts/published");
      dispatch({
         type: FETCH_PUBLISHED_BLOG_POSTS_SUCCESS,
         payload: response.data,
      });
   } catch (error) {
      dispatch({
         type: FETCH_PUBLISHED_BLOG_POSTS_FAILURE,
         payload: error.message,
      });
   }
};

export const fetchLatestBlogPosts = () => async (dispatch) => {
   dispatch({type: FETCH_LATEST_BLOG_POSTS_REQUEST});
   try {
      const response = await apiService.get("/blog-posts/latest");
      dispatch({
         type: FETCH_LATEST_BLOG_POSTS_SUCCESS,
         payload: response.data,
      });
   } catch (error) {
      dispatch({
         type: FETCH_LATEST_BLOG_POSTS_FAILURE,
         payload: error.message,
      });
   }
};

export const deleteBlogPost = (id) => async (dispatch) => {
   dispatch({type: DELETE_BLOG_POST_REQUEST});
   try {
      await apiService.delete(`/blog-posts/${id}`);
      dispatch({
         type: DELETE_BLOG_POST_SUCCESS,
         payload: id,
      });
   } catch (error) {
      dispatch({
         type: DELETE_BLOG_POST_FAILURE,
         payload: error.message,
      });
   }
};