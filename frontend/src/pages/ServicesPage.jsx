import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchServices} from '../redux/actions/serviceActions';
import ServiceCard from '../components/services/ServiceCard';

/**
 * Services page component
 * @returns {JSX.Element}
 * @constructor
 */
const ServicesPage = () => {
  const dispatch = useDispatch();
  const { services, loading, error } = useSelector(state => state.services);

  useEffect(() => {
    dispatch(fetchServices());
  }, [dispatch]);

  if (loading) return <div className="loading">Loading services...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="page services-page">
      <h1>Our Services</h1>
      <p className="services-intro">
        Explore our range of financial services designed to help you achieve your goals and secure your financial future.
      </p>
      <div className="services-grid">
        {services.map(service => (
          <ServiceCard key={service.id} service={service} />
        ))}
      </div>
    </div>
  );
};

export default ServicesPage;