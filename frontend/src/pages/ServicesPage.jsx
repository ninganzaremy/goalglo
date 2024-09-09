import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchServices} from "../redux/actions/serviceActions";
import ServiceCard from "../components/services/ServiceCard";
import EditServiceForm from "../components/services/EditServiceForm";
import {decryptData, userSecuredRole} from "../security/securityConfig.js";


/**
 * ServicesPage component
 * This component represents the services page.
 * It fetches and displays the list of services from the Redux store.
 * If the user has the necessary permissions, it provides an "Edit" button for each service.
 */
const ServicesPage = () => {
   const dispatch = useDispatch();
   const {services, loading, error} = useSelector((state) => state.services);
   const {user} = useSelector((state) => state.auth);
   const [editingServiceId, setEditingServiceId] = useState(null);

   const canManageServices =
      user && user.roles && user.roles.includes(decryptData(userSecuredRole()));

   useEffect(() => {
      dispatch(fetchServices());
   }, [dispatch]);

   const handleEditClick = (serviceId) => {
      setEditingServiceId(serviceId);
   };

   const handleEditCancel = () => {
      setEditingServiceId(null);
   };

   const handleEditSuccess = () => {
      setEditingServiceId(null);
      dispatch(fetchServices());
   };

   if (loading) return <div className="loading">Loading services...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   return (
      <div className="page services-page">
         <h1>Our Services</h1>
         <p className="services-intro">
            Explore our range of financial services designed to help you achieve
            your goals and secure your financial future.
         </p>
         <div className="services-grid">
            {services.map((service) => (
               <div key={service.id}>
                  {editingServiceId === service.id ? (
                     <EditServiceForm
                        serviceId={service.id}
                        onCancel={handleEditCancel}
                        onEditSuccess={handleEditSuccess}
                     />
                  ) : (
                     <>
                        <ServiceCard service={service}/>
                        {canManageServices && (
                           <button onClick={() => handleEditClick(service.id)}>
                              Edit
                           </button>
                        )}
                     </>
                  )}
               </div>
            ))}
         </div>
      </div>
   );
};

export default ServicesPage;