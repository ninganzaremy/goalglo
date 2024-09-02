import {Link} from 'react-router-dom';

/**
 * LatestBlogPosts component
 * @returns {JSX.Element} The rendered LatestBlogPosts component
 * @constructor
 */
const LatestBlogPosts = () => {
   //todo implement API to dynamically fetch LatestBlogPosts
   const blogPosts = [
      { id: 1, title: '10 Tips for Saving Money', excerpt: 'Learn how to save money effectively...' },
      { id: 2, title: 'Understanding Investment Risks', excerpt: 'Before investing, its important to...' },
   ];

   return (
      <section className="latest-blog-posts">
         <h2>Latest from Our Blog</h2>
         <div className="blog-post-list">
            {blogPosts.map(post => (
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