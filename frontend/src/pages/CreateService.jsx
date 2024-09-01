import {useState} from 'react';
import {useDispatch} from 'react-redux';
import {createService} from "../redux/actions/createServiceAction.js";


const CreateService = () => {
   const dispatch = useDispatch();
   const [error, setError] = useState(null);
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
      setError(null);

      try {
         await dispatch(createService(formData));
         setFormData({name: '', description: '', price: 0, duration: 0});
      } catch (err) {
         setError(err.response?.data?.message || 'An error occurred while creating the service');
      }
   };
   return (
      <div className="max-w-md mx-auto mt-10 bg-white p-8 border border-gray-300 rounded-lg shadow-lg">
         <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Create New Service</h2>
         {error && <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">{error}</div>}
         <form onSubmit={handleSubmit} className="space-y-4">
            <div>
               <label htmlFor="name" className="block text-sm font-medium text-gray-700">Name:</label>
               <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
               />
            </div>
            <div>
               <label htmlFor="description" className="block text-sm font-medium text-gray-700">Description:</label>
               <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  required
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
                  rows="3"
               />
            </div>
            <div>
               <label htmlFor="price" className="block text-sm font-medium text-gray-700">Price:</label>
               <input
                  type="number"
                  id="price"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  min="0"
                  step="0.01"
                  required
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
               />
            </div>
            <div>
               <label htmlFor="duration" className="block text-sm font-medium text-gray-700">Duration (in
                  minutes):</label>
               <input
                  type="number"
                  id="duration"
                  name="duration"
                  value={formData.duration}
                  onChange={handleChange}
                  min="0"
                  required
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-300 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
               />
            </div>
            <button
               type="submit"
               className="w-full py-2 px-4 border border-transparent rounded-md shadow-sm text-sm font-medium text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
            >
               Create Service
            </button>
         </form>
      </div>
   );
};

export default CreateService;