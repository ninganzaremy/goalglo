import {useAdminTestimonialsHook} from "../../hooks/useAdminTestimonialsHook.js";

/**
 * AdminTestimonials component
 * This component fetches and manages the list of testimonials from the Redux store.
 * It provides a table to display the testimonials with their name, text, and status.
 * The component also includes a button to refresh the testimonials data.
 *
 * @returns {JSX.Element} The AdminTestimonials component
 * @constructor
 */
const AdminTestimonials = () => {
   const {
      allTestimonials,
      loading,
      error,
      handleStatusUpdate,
      refreshTestimonials
   } = useAdminTestimonialsHook();


   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <div className="admin-testimonials">
         <h2>Manage Testimonials</h2>
         {allTestimonials.length === 0 ? (
            <p>No testimonials to display.</p>
         ) : (
            <table>
               <thead>
               <tr>
                  <th>Name</th>
                  <th>Text</th>
                  <th>Status</th>
                  <th>Actions</th>
               </tr>
               </thead>
               <tbody>
               {allTestimonials.map(testimonial => (
                  <tr key={testimonial.id}>
                     <td>{testimonial.name}</td>
                     <td>{testimonial.text}</td>
                     <td>{testimonial.status}</td>
                     <td>
                        <select
                           value={testimonial.status}
                           onChange={(e) => handleStatusUpdate(testimonial.id, e.target.value)}
                        >
                           <option value="PENDING">Pending</option>
                           <option value="APPROVED">Approved</option>
                           <option value="HIDDEN">Hidden</option>
                        </select>
                     </td>
                  </tr>
               ))}
               </tbody>
            </table>
         )}
         <button onClick={refreshTestimonials}>Refresh Testimonials</button>
      </div>
   );
};

export default AdminTestimonials;