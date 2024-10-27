import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate, useParams} from "react-router-dom";
import {fetchServices} from "../../redux/actions/serviceActions";
import {bookAppointment, fetchAvailableTimeSlots,} from "../../redux/actions/appointmentActions";

/**
 * ServiceBooking component
 * This component allows users to book a service by selecting a service,
 * time slot, and providing additional notes. It also handles form submission
 * and updates the Redux store with the booking data.
 *
 * @returns {JSX.Element}
 */
const ServiceBooking = () => {
   const dispatch = useDispatch();
   const navigate = useNavigate();
   const {id: preSelectedServiceId} = useParams(); // Get the service ID from URL if available

   const {
      services,
      loading: servicesLoading,
      error: servicesError,
   } = useSelector((state) => state.services);
   const {
      availableTimeSlots,
      loading: slotsLoading,
      error: slotsError,
   } = useSelector((state) => state.appointments);
   const [selectedService, setSelectedService] = useState("");
   const [selectedTimeSlot, setSelectedTimeSlot] = useState("");
   const [notes, setNotes] = useState("");
   const [firstName, setFirstName] = useState("");
   const [lastName, setLastName] = useState("");
   const [email, setEmail] = useState("");
   const [phoneNumber, setPhoneNumber] = useState("");
   const [address, setAddress] = useState("");

   useEffect(() => {
      dispatch(fetchServices());
      dispatch(fetchAvailableTimeSlots());
   }, [dispatch]);

   useEffect(() => {
      if (preSelectedServiceId && services.length > 0) {
         setSelectedService(preSelectedServiceId);
      }
   }, [preSelectedServiceId, services]);

   const handleBooking = (e) => {
      e.preventDefault();
      if (selectedService && selectedTimeSlot) {
         const selectedServiceDetails = services.find(service => service.id === selectedService);
         const selectedTimeSlotDetails = availableTimeSlots.find(slot => slot.id === selectedTimeSlot);
         const bookingData = {
            serviceId: selectedService,
            serviceName: selectedServiceDetails.name,
            timeSlotId: selectedTimeSlot,
            startTime: selectedTimeSlotDetails.startTime,
            endTime: selectedTimeSlotDetails.endTime,
            notes,
            firstName,
            lastName,
            email,
            phoneNumber,
            address,
         };
         dispatch(bookAppointment(bookingData));
         navigate("/booking-confirmation");
      }
   };

   if (servicesLoading || slotsLoading)
      return <div className="loading">Loading...</div>;
   if (servicesError || slotsError)
      return <div className="error">Error: {servicesError || slotsError}</div>;

   return (
      <div className="service-booking">
         <h2>Book a Service</h2>
         <form onSubmit={handleBooking} className="booking-form">
            <select
               value={selectedService}
               onChange={(e) => setSelectedService(e.target.value)}
               required
            >
               <option value="">Select a service</option>
               {services.map((service) => (
                  <option key={service.id} value={service.id}>
                     {service.name}
                  </option>
               ))}
            </select>

            <select
               value={selectedTimeSlot}
               onChange={(e) => setSelectedTimeSlot(e.target.value)}
               required
            >
               <option value="">Select a time slot</option>
               {availableTimeSlots.map((slot) => (
                  <option key={slot.id} value={slot.id}>
                     {new Date(slot.startTime).toLocaleString()} -{" "}
                     {new Date(slot.endTime).toLocaleString()}
                  </option>
               ))}
            </select>

            <input
               type="text"
               value={firstName}
               onChange={(e) => setFirstName(e.target.value)}
               placeholder="First Name"
               required
            />
            <input
               type="text"
               value={lastName}
               onChange={(e) => setLastName(e.target.value)}
               placeholder="Last Name"
               required
            />
            <input
               type="email"
               value={email}
               onChange={(e) => setEmail(e.target.value)}
               placeholder="Email"
               required
            />
            <input
               type="tel"
               value={phoneNumber}
               onChange={(e) => setPhoneNumber(e.target.value)}
               placeholder="Phone Number"
               required
            />
            <input
               type="text"
               value={address}
               onChange={(e) => setAddress(e.target.value)}
               placeholder="Address"
               required
            />
            <textarea
               value={notes}
               onChange={(e) => setNotes(e.target.value)}
               placeholder="Notes (optional)"
            />

            <button
               type="submit"
               disabled={!selectedService || !selectedTimeSlot}
            >
               Book Now
            </button>
         </form>
      </div>
   );
};

export default ServiceBooking;