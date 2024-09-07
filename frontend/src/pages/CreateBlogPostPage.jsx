import {useRef, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {createBlogPost} from '../redux/actions/blogActions';

/**
 * Component for creating a new blog post.
 *
 * @returns {JSX.Element}
 * @constructor
 */
const CreateBlogPostPage = () => {
   const dispatch = useDispatch();
   const {creating, createError} = useSelector(state => state.blog);
   const fileInputRef = useRef(null);

   const [formData, setFormData] = useState({
      title: '',
      content: '',
      slug: ''
   });
   const [image, setImage] = useState(null);
   const [successMessage, setSuccessMessage] = useState('');

   const handleChange = (e) => {
      const {name, value} = e.target;
      setFormData(prevData => ({
         ...prevData,
         [name]: value
      }));
   };

   const handleImageChange = (e) => {
      setImage(e.target.files[0]);
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      setSuccessMessage('');

      if (!formData.title || !formData.content || !formData.slug) {
         console.error('Please fill in all required fields');
         return;
      }

      const postData = new FormData();
      postData.append('title', formData.title);
      postData.append('content', formData.content);
      postData.append('slug', formData.slug);
      if (image) {
         postData.append('image', image);
      }

      try {
         await dispatch(createBlogPost(postData));
         setFormData({title: '', content: '', slug: ''});
         setImage(null);
         if (fileInputRef.current) {
            fileInputRef.current.value = '';
         }
         setSuccessMessage('Blog post created successfully!');
      } catch (error) {
         console.error('Error creating blog post:', error);
      }
   };

   return (
      <div className="create-blog-post-page">
         <h2>Create New Blog Post</h2>
         {createError && <div className="error-message">{createError}</div>}
         {successMessage && <div className="success-message">{successMessage}</div>}
         <form onSubmit={handleSubmit}>
            <div className="form-group">
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
            <div className="form-group">
               <label htmlFor="slug">Slug:</label>
               <input
                  type="text"
                  id="slug"
                  name="slug"
                  value={formData.slug}
                  onChange={handleChange}
                  required
               />
            </div>
            <div className="form-group">
               <label htmlFor="content">Content:</label>
               <textarea
                  id="content"
                  name="content"
                  value={formData.content}
                  onChange={handleChange}
                  required
                  rows="10"
               />
            </div>
            <div className="form-group">
               <label htmlFor="image">Image:</label>
               <input
                  type="file"
                  id="image"
                  name="image"
                  multiple
                  onChange={handleImageChange}
                  accept="image/*"
                  ref={fileInputRef}
               />
               {image && <p>Selected file: {image.name}</p>}
            </div>
            <button type="submit" className="submit-button" disabled={creating}>
               {creating ? 'Creating...' : 'Create Post'}
            </button>
         </form>
      </div>
   );
};

export default CreateBlogPostPage;