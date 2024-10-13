import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchPublishedBlogPosts} from '../../redux/actions/blogActions';

/**
 * BlogPostList component
 * This component fetches and displays a list of published blog posts.
 * It uses Redux to manage the state and dispatch actions to fetch the blog posts.
 */
const BlogPostList = () => {
   const dispatch = useDispatch();
   const {blogPosts, loading, error} = useSelector((state) => state.blog);

   useEffect(() => {
      dispatch(fetchPublishedBlogPosts());
   }, [dispatch]);

   if (loading) return <div>Loading blog posts...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <div className="blog-post-list">
         <h2>Blog Posts</h2>
         {blogPosts.map((post) => (
            <div key={post.id} className="blog-post">
               <h3>{post.title}</h3>
               <p>{post.content.substring(0, 200)}...</p>
               <p>Published on: {new Date(post.createdAt).toLocaleDateString()}</p>
            </div>
         ))}
      </div>
   );
};

export default BlogPostList;