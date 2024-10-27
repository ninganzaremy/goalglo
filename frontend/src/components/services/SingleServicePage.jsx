import {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
import DOMPurify from 'dompurify';

import {fetchServiceById} from "../../redux/actions/serviceActions";


/**
 * SingleServicePage component
 * Displays a single service based on the provided ID
 */
const SingleServicePage = () => {
   const {id} = useParams();
   const dispatch = useDispatch();
   const {currentService, loading, error} = useSelector(state => state.services);

   useEffect(() => {
      dispatch(fetchServiceById(id));
   }, [dispatch, id]);

   if (loading) return <div className="loading">Loading...</div>;
   if (error) return <div className="error">Error: {error}</div>;
   if (!currentService) return <div className="not-found">Service not found</div>;

   const sanitizedDescription = DOMPurify.sanitize(currentService.description);

   return (
      <div className="single-service-page">
         <div className="container">
            <h1>{currentService.name}</h1>
            <p className="service-category">{currentService.category}</p>
            <img className="service-image"
                 src='https://ninganzaremy.com/static/media/blog-post-1.aaa2efa604073cd518e1.jpg'
                 alt={currentService.name}/>
            <div className="service-content">
               <div dangerouslySetInnerHTML={{__html: sanitizedDescription}}/>
               <div className="service-meta">
                  <p><strong>Duration:</strong> {currentService.duration} minutes</p>
                  <p><strong>Price:</strong> ${currentService.price}</p>
               </div>
               <Link to={`/book-service/${currentService.id}`} className="btn-primary">
                  Book Now
               </Link>
            </div>
         </div>
      </div>
   );
};

export default SingleServicePage;