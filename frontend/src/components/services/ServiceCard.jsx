import {Link} from 'react-router-dom';
/*
 * ServiceCard component
 * This component represents a single service card that displays information about a service.
 * It takes a `service` object as a prop, which should contain the following properties:
 * - `id`: The unique identifier for the service.
 * - `name`: The name of the service.
 * - `description`: A brief description of the service.
 * - `imageUrl`: The URL of the image to be displayed for the service.
 */
const ServiceCard = ({ service }) => {
  return (
    <div className="service-card">
      <img src={service.imageUrl} alt={service.name} className="service-image" />
      <h2>{service.name}</h2>
      <p>{service.description}</p>
       <Link to={`/services/${service.id}`} className="learn-more">
          Learn More
       </Link>
       <Link to={`/book-service/${service.id}`} className="book-now">
          Book Now
       </Link>
    </div>
  );
};

export default ServiceCard;