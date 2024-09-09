import {Link} from "react-router-dom";

/*
BlogPostCard component
This component renders a single blog post card with its title, excerpt, date, author, and an image
It also includes a link to the full blog post page

Props:
post: An object containing the post data, including:
  id: The unique identifier for the post
  title: The title of the post
  excerpt: A short excerpt or summary of the post
  date: The date when the post was published
  author: The author of the post
  imageUrl: The URL of the image associated with the post

  */
const formatDate = (dateString) => {
   const date = new Date(dateString);
   return date instanceof Date && !isNaN(date)
      ? date.toLocaleDateString("en-US", {
         year: "numeric",
         month: "long",
         day: "numeric",
      })
      : "Date not available";
};
const BlogPostCard = ({post, isAdmin, onEditClick}) => {
   return (
      <div className="blog-post-card">
         {post.imageUrl && (
            <img
               src={post.imageUrl}
               alt={post.title}
               className="blog-post-image"
            />
         )}
         <h2>{post.title}</h2>
         <p>{post.content.substring(0, 150)}...</p>
         <p>Published on: {formatDate(post.publishedAt)}</p>
         <p>Author: {post.author ? post.author : "Unknown"}</p>
         {isAdmin && (
            <button onClick={onEditClick} className="edit-button">
               Edit
            </button>
         )}
         <Link to={`/blog/${post.id}`} className="read-more">
            Read More
         </Link>
      </div>
   );
};

export default BlogPostCard;