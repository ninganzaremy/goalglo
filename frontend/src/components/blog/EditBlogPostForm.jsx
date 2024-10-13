import {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchBlogPostById, updateBlogPost} from '../../redux/actions/blogActions';

/**
 * EditBlogPostForm component
 * @param {number} postId - The ID of the blog post to edit
 * @param {function} onCancel - Callback function to handle cancellation
 * @param {function} onEditSuccess - Callback function to handle successful edit
 */
const EditBlogPostForm = ({postId, onCancel, onEditSuccess}) => {
   const dispatch = useDispatch();
   const {currentPost, loading, error} = useSelector(state => state.blog);
   const [formData, setFormData] = useState({
      title: '',
      content: '',
      published: false,
   });
   const [image, setImage] = useState(null);

   useEffect(() => {
      if (!currentPost || currentPost.id !== postId) {
         dispatch(fetchBlogPostById(postId));
      } else {
         setFormData({
            title: currentPost.title,
            content: currentPost.content,
            published: currentPost.published,
         });
      }
   }, [dispatch, postId, currentPost]);

   const handleChange = (e) => {
      const {name, value, type, checked} = e.target;
      setFormData(prevData => ({
         ...prevData,
         [name]: type === 'checkbox' ? checked : value,
      }));
   };

   const handleImageChange = (e) => {
      setImage(e.target.files[0]);
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      const formDataToSend = new FormData();
      formDataToSend.append('title', formData.title);
      formDataToSend.append('content', formData.content);
      formDataToSend.append('published', formData.published);
      if (image) {
         formDataToSend.append('image', image);
      }
      await dispatch(updateBlogPost(postId, formDataToSend));
      onEditSuccess();
   };

   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <form onSubmit={handleSubmit}>
         <div>
            <label htmlFor="title">Title:</label>
            <input
               type="text"
               id="title"
               name="title"
               value={formData.title}
               onChange={handleChange}
               required
            />
         </div>
         <div>
            <label htmlFor="content">Content:</label>
            <textarea
               id="content"
               name="content"
               value={formData.content}
               onChange={handleChange}
               required
            />
         </div>
         <div>
            <label htmlFor="published">Published:</label>
            <input
               type="checkbox"
               id="published"
               name="published"
               checked={formData.published}
               onChange={handleChange}
            />
         </div>
         <div>
            <label htmlFor="image">Image:</label>
            <input
               type="file"
               id="image"
               name="image"
               onChange={handleImageChange}
            />
         </div>
         <button type="submit">Update Blog Post</button>
         <button type="button" onClick={onCancel}>Cancel</button>
      </form>
   );
};

export default EditBlogPostForm;