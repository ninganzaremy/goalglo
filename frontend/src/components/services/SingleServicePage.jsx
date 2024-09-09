import {useEffect} from 'react';
import {Link, useParams} from 'react-router-dom';
import {useDispatch, useSelector} from 'react-redux';
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

   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;
   if (!currentService) return <div>Service not found</div>;

   return (
      <div className="single-service">
         <h1>{currentService.name}</h1>
         <p>{currentService.description}</p>
         <p>Price: ${currentService.price}</p>
         <Link to={`/book-service/${currentService.id}`} className="book-now">
            Book Now
         </Link>
      </div>
   );
};

export default SingleServicePage;