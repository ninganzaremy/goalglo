import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Link} from 'react-router-dom';

import {fetchLatestBlogPosts} from '../../redux/actions/blogActions';

/*
LatestBlogPosts component
This component fetches and displays the latest blog posts from the Redux store
It uses the `fetchLatestBlogPosts` action creator to retrieve the latest posts
and displays them in a grid layout

Props: None
*/

const LatestBlogPosts = () => {
   const dispatch = useDispatch();
   const {latestBlogPosts, loading, error} = useSelector((state) => state.blog);

   useEffect(() => {
      dispatch(fetchLatestBlogPosts());
   }, [dispatch]);

   if (loading) return <div>Loading latest blog posts...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <section className="latest-blog-posts">
         <h2>Latest from Our Blog</h2>
         <div className="blog-post-list">
            {latestBlogPosts.map(post => (
               <div key={post.id} className="blog-post-item">
                  <h3>{post.title}</h3>
                  <p>{post.excerpt}</p>
                  <Link to={`/blog/${post.id}`}>Read More</Link>
               </div>
            ))}
         </div>
         <Link to="/blog" className="view-all-link">View All Posts</Link>
      </section>
   );
};

export default LatestBlogPosts;