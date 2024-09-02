/*
Testimonials component
This component fetches and displays a list of testimonials from a simulated API.
The testimonials are hardcoded for demonstration purposes, but in a real-world scenario,
they could be dynamically fetched from an API.

*/
const Testimonials = () => {
   //todo implement API to dynamically fetch Testimonials
   const testimonials = [
      { id: 1, name: 'John Doe', text: 'GoalGlo helped me achieve financial freedom!' },
      { id: 2, name: 'Jane Smith', text: 'The personalized advice was invaluable for my retirement planning.' },
   ];

   return (
      <section className="testimonials">
         <h2>What Our Clients Say</h2>
         <div className="testimonial-list">
            {testimonials.map(testimonial => (
               <div key={testimonial.id} className="testimonial-item">
                  <p>"{testimonial.text}"</p>
                  <p className="testimonial-author">- {testimonial.name}</p>
               </div>
            ))}
         </div>
      </section>
   );
};

export default Testimonials;