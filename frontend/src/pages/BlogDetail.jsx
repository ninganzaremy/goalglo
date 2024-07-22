import {useEffect, useState} from 'react';
import axios from 'axios';
import {Link, useParams} from 'react-router-dom';

const BlogDetail = () => {
   const {id} = useParams();
   const [post, setPost] = useState(null);

   useEffect(() => {
      axios.get(`/api/blogs/${id}`)
         .then(response => setPost(response.data))
         .catch(error => console.error('Error fetching blog post:', error));
   }, [id]);

   if (!post) return <div>Loading...</div>;

   return (
      <div>
         <h2>{post.title}</h2>
         <p>{post.content}</p>
         <Link to="/schedule">
            <button>Schedule a Consultation</button>
         </Link>
      </div>
   );
}

export default BlogDetail;