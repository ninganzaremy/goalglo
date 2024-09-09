import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchServiceById, updateService,} from "../../redux/actions/serviceActions";

/**
 * EditServiceForm component
 * This component displays a form to edit a service.
 * It fetches the service data based on the provided serviceId and allows the user to update the service details.
 */
const EditServiceForm = ({serviceId, onCancel, onEditSuccess}) => {
   const dispatch = useDispatch();
   const {currentService, loading, error} = useSelector(
      (state) => state.services
   );
   const [formData, setFormData] = useState({
      name: "",
      description: "",
      price: 0,
      duration: 0,
   });

   useEffect(() => {
      if (!currentService || currentService.id !== serviceId) {
         dispatch(fetchServiceById(serviceId));
      } else {
         setFormData({
            name: currentService.name,
            description: currentService.description,
            price: currentService.price,
            duration: currentService.duration,
         });
      }
   }, [dispatch, serviceId, currentService]);

   const handleChange = (e) => {
      const {name, value} = e.target;
      setFormData((prevData) => ({
         ...prevData,
         [name]:
            name === "price" || name === "duration" ? Number(value) : value,
      }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      await dispatch(updateService(serviceId, formData));
      onEditSuccess();
   };

   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <form onSubmit={handleSubmit}>
         <div>
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
         <div>
            <label htmlFor="description">Description:</label>
            <textarea
               id="description"
               name="description"
               value={formData.description}
               onChange={handleChange}
               required
            />
         </div>
         <div>
            <label htmlFor="price">Price:</label>
            <input
               type="number"
               id="price"
               name="price"
               value={formData.price}
               onChange={handleChange}
               required
            />
         </div>
         <div>
            <label htmlFor="duration">Duration (minutes):</label>
            <input
               type="number"
               id="duration"
               name="duration"
               value={formData.duration}
               onChange={handleChange}
               required
            />
         </div>
         <button type="submit">Update Service</button>
         <button type="button" onClick={onCancel}>
            Cancel
         </button>
      </form>
   );
};

export default EditServiceForm;