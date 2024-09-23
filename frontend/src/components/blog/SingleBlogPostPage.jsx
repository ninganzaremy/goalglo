import {useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import DOMPurify from 'dompurify';

import {fetchBlogPostById} from "../../redux/actions/blogActions";

/**
 * SingleBlogPostPage component
 * Displays a single blog post based on the provided ID
 */
const SingleBlogPostPage = () => {
   const {id} = useParams();
   const dispatch = useDispatch();
   const {currentPost, loading, error} = useSelector(state => state.blog);

   useEffect(() => {
      dispatch(fetchBlogPostById(id));
   }, [dispatch, id]);

   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;
   if (!currentPost) return <div>Post not found</div>;

   return (
      <div className="single-blog-post">
         <div className="container">
            <h1>{currentPost.title}</h1>
            <img src={currentPost.imageUrl} alt={currentPost.title} className="blog-post-image"/>
            <div
               className="blog-post-content"
               dangerouslySetInnerHTML={{__html: DOMPurify.sanitize(currentPost.content)}}
            />
            <div className="blog-post-meta">
               <p>Author: {currentPost.author ? currentPost.author : 'Unknown'}</p>
               <p>Published on: {new Date(currentPost.publishedAt).toLocaleDateString()}</p>
            </div>
         </div>
      </div>
   );
};

export default SingleBlogPostPage;