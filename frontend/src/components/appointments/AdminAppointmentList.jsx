import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchAllAppointments, updateAppointmentStatus} from '../../redux/actions/appointmentActions';

const AdminAppointmentList = () => {
   const dispatch = useDispatch();
   const {appointments, loading, error} = useSelector((state) => state.appointments);

   useEffect(() => {
      dispatch(fetchAllAppointments());
   }, [dispatch]);

   const handleStatusChange = (appointmentId, newStatus) => {
      dispatch(updateAppointmentStatus(appointmentId, newStatus));
   };

   if (loading) return <div>Loading appointments...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <div className="admin-appointment-list">
         <h2>All Appointments</h2>
         <table>
            <thead>
            <tr>
               <th>User</th>
               <th>Service</th>
               <th>Date</th>
               <th>Status</th>
               <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            {appointments.map((appointment) => (
               <tr key={appointment.id}>
                  <td>{appointment.userName}</td>
                  <td>{appointment.serviceName}</td>
                  <td>{new Date(appointment.startTime).toLocaleString()}</td>
                  <td>{appointment.status}</td>
                  <td>
                     <select
                        value={appointment.status}
                        onChange={(e) => handleStatusChange(appointment.id, e.target.value)}
                     >
                        <option value="Pending">Pending</option>
                        <option value="Accepted">Accepted</option>
                        <option value="Denied">Denied</option>
                        <option value="Canceled">Canceled</option>
                     </select>
                  </td>
               </tr>
            ))}
            </tbody>
         </table>
      </div>
   );
};

export default AdminAppointmentList;