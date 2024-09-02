import {useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {createService} from "../redux/actions/createServiceActions.js";

/**
 * Component for creating a new service.
 * @returns {JSX.Element}
 * @constructor
 */
const CreateServicePage = () => {
   const dispatch = useDispatch();
   const {loading, error} = useSelector(state => state.createService);
   const [formData, setFormData] = useState({
      name: '',
      description: '',
      price: 0,
      duration: 0
   });

   const handleChange = (e) => {
      const {name, value} = e.target;
      setFormData(prevData => ({
         ...prevData,
         [name]: name === 'price' || name === 'duration' ? Number(value) : value
      }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
         await dispatch(createService(formData));
         setFormData({name: '', description: '', price: 0, duration: 0});
   };

   return (
      <div className="create-service-page">
         <h2>Create New Service</h2>
         {error && <div className="error-message">{error}</div>}
         <form onSubmit={handleSubmit}>
            <div className="form-group">
               <label htmlFor="name">Name:</label>
               <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
               />
            </div>
            <div className="form-group">
               <label htmlFor="description">Description:</label>
               <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  required
                  rows="3"
               />
            </div>
            <div className="form-group">
               <label htmlFor="price">Price:</label>
               <input
                  type="number"
                  id="price"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
               />
            </div>
            <div className="form-group">
               <label htmlFor="duration">Duration (in minutes):</label>
               <input
                  type="number"
                  id="duration"
                  name="duration"
                  value={formData.duration}
                  onChange={handleChange}
                  min="0"
                  required
               />
            </div>
            <button type="submit" className="submit-button" disabled={loading}>
               {loading ? 'Creating...' : 'Create Service'}
            </button>
         </form>
      </div>
   );
};

export default CreateServicePage;