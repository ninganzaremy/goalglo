import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchBlogPostById, fetchBlogPosts} from "../redux/actions/blogActions";
import BlogPostCard from "../components/blog/BlogPostCard";
import EditBlogPostForm from "../components/blog/EditBlogPostForm";
import Pagination from "../components/common/Pagination";
import {decryptData, userSecuredRole} from "../security/securityConfig.js";

const BlogPage = () => {
   const dispatch = useDispatch();
   const {posts, loading, error, totalPages} = useSelector((state) => state.blog);
   const {user} = useSelector((state) => state.auth);
   const [currentPage, setCurrentPage] = useState(1);
   const [editingPostId, setEditingPostId] = useState(null);

   const canManagePosts = user && user.roles && user.roles.includes(decryptData(userSecuredRole()));

   useEffect(() => {
      dispatch(fetchBlogPosts(currentPage));
   }, [dispatch, currentPage]);

   const handlePageChange = (pageNumber) => {
      setCurrentPage(pageNumber);
   };

   const handleEditClick = (postId) => {
      dispatch(fetchBlogPostById(postId));
      setEditingPostId(postId);
   };

   const handleEditCancel = () => {
      setEditingPostId(null);
   };

   const handleEditSuccess = () => {
      setEditingPostId(null);
      dispatch(fetchBlogPosts(currentPage)); // Refresh the posts after edit
   };

   if (loading) return <div className="loading">Loading blog posts...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="page blog-page">
         <h1>GoalGlo Blog</h1>
         <p className="blog-intro">
            Explore our latest articles on financial planning, personal growth,
            and achieving your goals.
         </p>
         <div className="blog-grid">
            {posts && posts.length > 0 ? (
               posts.map((post) => (
                  <div key={post.id}>
                     {editingPostId === post.id ? (
                        <EditBlogPostForm
                           postId={post.id}
                           onCancel={handleEditCancel}
                           onEditSuccess={handleEditSuccess}
                        />
                     ) : (
                        <>
                           <BlogPostCard post={post}/>
                           {canManagePosts && (
                              <button onClick={() => handleEditClick(post.id)}>
                                 Edit
                              </button>
                           )}
                        </>
                     )}
                  </div>
               ))
            ) : (
               <p>No blog posts available.</p>
            )}
         </div>
         {totalPages > 1 && (
            <Pagination
               currentPage={currentPage}
               totalPages={totalPages}
               onPageChange={handlePageChange}
            />
         )}
      </div>
   );
};

export default BlogPage;