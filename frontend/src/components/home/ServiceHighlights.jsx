import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {Link} from 'react-router-dom';

import {fetchServices} from "../../redux/actions/serviceActions";

/*
ServiceHighlights component
This component fetches and displays the services from the Redux store
It uses the `fetchServices` action creator to retrieve the services
*/

const ServiceHighlights = () => {
   const dispatch = useDispatch();
   const {services, loading, error} = useSelector((state) => state.services);

   useEffect(() => {
      dispatch(fetchServices());
   }, [dispatch]);

   if (loading) return <div>Loading services...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <section className="service-highlights">
         <div className="container">
            <h2>Our Services</h2>
            <div className="service-list">
               {services.slice(0, 4).map((service) => (
                  <div key={service.id} className="service-item">
                     <h3>{service.name}</h3>
                     <p>{service.description}</p>
                     <Link to={`/services/${service.id}`} className="learn-more">
                        Learn More
                     </Link>
                  </div>
               ))}
            </div>

            <Link to="/services" className="view-all-link">
               View All Services
            </Link>
         </div>
      </section>
   );
};

export default ServiceHighlights;