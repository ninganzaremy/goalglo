import {useState} from "react";
import {useDispatch} from "react-redux";
import {updateAppointment} from "../../redux/actions/appointmentActions";

const EditAppointment = ({appointment, onClose}) => {
   const [updatedAppointment, setUpdatedAppointment] = useState(appointment);
   const dispatch = useDispatch();

   const handleChange = (e) => {
      setUpdatedAppointment({
         ...updatedAppointment,
         [e.target.name]: e.target.value,
      });
   };

   const handleSubmit = (e) => {
      e.preventDefault();
      dispatch(updateAppointment(appointment.id, updatedAppointment));
      onClose();
   };

   return (
      <form onSubmit={handleSubmit}>
         <input
            type="datetime-local"
            name="startTime"
            value={updatedAppointment.startTime}
            onChange={handleChange}
         />
         <textarea
            name="notes"
            value={updatedAppointment.notes}
            onChange={handleChange}
         />
         <button type="submit">Update Appointment</button>
         <button type="button" onClick={onClose}>
            Cancel
         </button>
      </form>
   );
};

export default EditAppointment;