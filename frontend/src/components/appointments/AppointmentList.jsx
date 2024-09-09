import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {cancelAppointment, fetchAppointments,} from "../../redux/actions/appointmentActions";
import EditAppointment from "./EditAppointment.jsx";

/**
 * AppointmentList component
 * Displays a list of appointments for the user
 * Allows users to cancel appointments
 */
const AppointmentList = () => {
   const dispatch = useDispatch();
   const { appointments, loading, error } = useSelector(
      (state) => state.appointments
   );
   const [editingAppointment, setEditingAppointment] = useState(null);

   useEffect(() => {
      dispatch(fetchAppointments());
   }, [dispatch]);

   const handleCancelAppointment = (appointmentId) => {
      dispatch(cancelAppointment(appointmentId));
   };

   if (loading) return <div className="loading">Loading appointments...</div>;
   if (error) return <div className="error">Error: {error}</div>;

   const now = new Date();
   const pastAppointments = appointments.filter(
      (app) => new Date(app.startTime) < now
   );
   const futureAppointments = appointments.filter(
      (app) => new Date(app.startTime) >= now
   );

   const renderAppointment = (appointment) => (
      <li key={appointment.id} className="appointment-item">
      <span>
         {appointment.serviceName} on{" "}
         {new Date(appointment.startTime).toLocaleString()}
      </span>
         <span>Status: {appointment.status}</span>
         {appointment.status !== "Canceled" && new Date(appointment.startTime) > now && (
            <>
               <button onClick={() => setEditingAppointment(appointment)}>
                  Edit
               </button>
               <button onClick={() => handleCancelAppointment(appointment.id)}>
                  Cancel
               </button>
            </>
         )}
      </li>
   );

   return (
      <div className="appointment-list">
         <h2>Your Appointments</h2>
         {appointments.length === 0 ? (
            <p>You have no appointments.</p>
         ) : (
            <>
               <h3>Upcoming Appointments</h3>
               {futureAppointments.length === 0 ? (
                  <p>You have no upcoming appointments.</p>
               ) : (
                  <ul>{futureAppointments.map(renderAppointment)}</ul>
               )}

               <h3>Past Appointments</h3>
               {pastAppointments.length === 0 ? (
                  <p>You have no past appointments.</p>
               ) : (
                  <ul>{pastAppointments.map(renderAppointment)}</ul>
               )}
            </>
         )}
         {editingAppointment && (
            <EditAppointment
               appointment={editingAppointment}
               onClose={() => setEditingAppointment(null)}
            />
         )}
      </div>
   );
};

export default AppointmentList;