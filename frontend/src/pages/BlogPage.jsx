import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchBlogPosts} from '../redux/actions/blogActions';
import BlogPostCard from '../components/blog/BlogPostCard';
import Pagination from '../components/common/Pagination';

/**
 * Component for displaying the blog page.
 * @returns {JSX.Element}
 * @constructor
 */
const BlogPage = () => {
   const dispatch = useDispatch();
   const {posts, loading, error, totalPages} = useSelector(state => state.blog);
   const [currentPage, setCurrentPage] = useState(1);

   useEffect(() => {
      dispatch(fetchBlogPosts(currentPage));
   }, [dispatch, currentPage]);

   const handlePageChange = (pageNumber) => {
      setCurrentPage(pageNumber);
   };

   if (loading) return <div className="loading">Loading blog posts...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="page blog-page">
         <h1>GoalGlo Blog</h1>
         <p className="blog-intro">
            Explore our latest articles on financial planning, personal growth, and achieving your goals.
         </p>
         <div className="blog-grid">
            {posts.map(post => (
               <BlogPostCard key={post.id} post={post}/>
            ))}
         </div>
         <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={handlePageChange}
         />
      </div>
   );
};

export default BlogPage;