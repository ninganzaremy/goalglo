import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {createBlogPost} from '../redux/actions/blogActions';

/**
 * Component for creating a new blog post.
 * @returns {JSX.Element}
 * @constructor
 */
const CreateBlogPostPage = () => {
   const dispatch = useDispatch();
   const {creating, createError} = useSelector(state => state.blog);

   const [formData, setFormData] = useState({
      title: '',
      content: '',
      imageUrl: ''
   });

   const handleChange = (e) => {
      const {name, value} = e.target;
      setFormData(prevData => ({
         ...prevData,
         [name]: value
      }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();

      await dispatch(createBlogPost(formData));
      setFormData({title: '', content: '', imageUrl: ''});
   };

   return (
      <div className="create-blog-post-page">
         <h2>Create New Blog Post</h2>
         {createError && <div className="error-message">{createError}</div>}
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
               <label htmlFor="imageUrl">Image URL:</label>
               <input
                  type="url"
                  id="imageUrl"
                  name="imageUrl"
                  value={formData.imageUrl}
                  onChange={handleChange}
               />
            </div>
            <button type="submit" className="submit-button" disabled={creating}>
               {creating ? 'Creating...' : 'Create Post'}
            </button>
         </form>
      </div>
   );
};

export default CreateBlogPostPage;