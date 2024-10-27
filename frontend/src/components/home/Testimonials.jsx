import {useAuthContext} from "../../hooks/useAuthContext";
import {useTestimonialsHook} from "../../hooks/useTestimonialsHook.js";

/**
 * Testimonials component
 * This component fetches and displays the list of testimonials from the Redux store.
 * If the user is authenticated, it provides a form to add or edit a testimonial.
 * The component handles loading and error states.
 */
const Testimonials = () => {

   const {user} = useAuthContext();

   const {
      testimonials,
      loading,
      error,
      editingId,
      newTestimonial,
      handleEdit,
      handleCancelEdit,
      handleTestimonialChange,
      handleSubmit
   } = useTestimonialsHook();

   if (loading) return <div>Loading...</div>;
   if (error) return <div>Error: {error}</div>;

   return (
      <section className="testimonials">
         <h2>What Our Clients Say</h2>
         <div className="testimonial-list">
            {testimonials.map(testimonial => (
               <div key={testimonial.id} className="testimonial-item">
                  <p>{testimonial.text}</p>
                  <p className="testimonial-author">- {testimonial.name}</p>
                  {user && user.userId === testimonial.userId && testimonial.status === 'APPROVED' && (
                     <button onClick={() => handleEdit(testimonial)}>Edit</button>
                  )}
               </div>
            ))}
         </div>
         {user && (
            <form onSubmit={handleSubmit} className="testimonial-form">
               <textarea
                  value={newTestimonial.text}
                  onChange={handleTestimonialChange}
                  placeholder="Share your experience..."
               />
               <button type="submit">{editingId ? 'Update' : 'Submit'} Testimonial</button>
               {editingId && (
                  <button type="button" onClick={handleCancelEdit}>Cancel Edit</button>
               )}
            </form>
         )}
      </section>
   );
};


export default Testimonials;