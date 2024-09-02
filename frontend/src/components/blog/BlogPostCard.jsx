import {Link} from 'react-router-dom';

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

const BlogPostCard = ({ post }) => {
   return (
      <div className="blog-post-card">
         <img src={post.imageUrl} alt={post.title} className="post-image" />
         <div className="post-content">
            <h2>{post.title}</h2>
            <p className="post-excerpt">{post.excerpt}</p>
            <div className="post-meta">
               <span className="post-date">{new Date(post.date).toLocaleDateString()}</span>
               <span className="post-author">By {post.author}</span>
            </div>
            <Link to={`/blog/${post.id}`} className="read-more">Read More</Link>
         </div>
      </div>
   );
};

export default BlogPostCard;