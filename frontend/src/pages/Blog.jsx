import {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

import axios from 'axios';

const Blog = () => {
   const [posts, setPosts] = useState([]);

   useEffect(() => {
      axios.get('/api/blogs')
         .then(response => setPosts(response.data))
         .catch(error => console.error('Error fetching blog posts:', error));
   }, []);

   return (
      <div>
         <h2>Blog</h2>
         <ul>
            {posts.map(post => (
               <li key={post.id}>
                  <Link to={`/blog/${post.id}`}>{post.title}</Link>
               </li>
            ))}
         </ul>
      </div>
   );
}

export default Blog;