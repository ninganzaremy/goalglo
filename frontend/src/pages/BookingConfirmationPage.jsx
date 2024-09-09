import {useSelector} from "react-redux";
import {Link} from "react-router-dom";

/**
 * BookingConfirmationPage component
 * This component displays the booking confirmation page with the details of the last booked appointment.
 * If no booking information is available, it redirects the user to the services page.
 */
const BookingConfirmationPage = () => {
   const {lastBooking} = useSelector((state) => state.appointments);

   if (!lastBooking) {
      return (
         <div className="booking-confirmation">
            <h2>No booking information available</h2>
            <Link to="/services" className="btn-primary">
               Book a Service
            </Link>
         </div>
      );
   }

   return (
      <div className="booking-confirmation">
         <h2>Thank You for Your Booking!</h2>
         <p>Your appointment has been successfully scheduled.</p>
         <p>
            An email with the booking details has been sent to your email
            address. Please check your inbox.
         </p>
         <div className="booking-details">
            <h3>Booking Details:</h3>
            <p>
               <strong>Service:</strong> {lastBooking.serviceName}
            </p>
            <p>
               <strong>Date:</strong>{" "}
               {new Date(lastBooking.startTime).toLocaleDateString()}
            </p>
            <p>
               <strong>Time:</strong>{" "}
               {new Date(lastBooking.startTime).toLocaleTimeString()} -{" "}
               {new Date(lastBooking.endTime).toLocaleTimeString()}
            </p>
            <p>
               <strong>Name:</strong> {lastBooking.firstName}{" "}
               {lastBooking.lastName}
            </p>
            <p>
               <strong>Email:</strong> {lastBooking.email}
            </p>
            <p>
               <strong>Phone:</strong> {lastBooking.phoneNumber}
            </p>
            {lastBooking.address && (
               <p>
                  <strong>Address:</strong> {lastBooking.address}
               </p>
            )}
            {lastBooking.notes && (
               <p>
                  <strong>Notes:</strong> {lastBooking.notes}
               </p>
            )}
         </div>
         <div className="account-creation-prompt">
            <h3>Manage Your Bookings and Explore More Benefits</h3>
            <p>
               Create an account with GoalGlo to manage your appointments, view
               your booking history, and access exclusive benefits and features
               we offer to our registered users.
            </p>
            <Link to="/register" className="btn-primary">
               Create an Account
            </Link>
         </div>
         <div className="additional-actions">
            <Link to="/services" className="btn-secondary">
               Book Another Service
            </Link>
            <Link to="/" className="btn-secondary">
               Return to Home
            </Link>
         </div>
      </div>
   );
};

export default BookingConfirmationPage;