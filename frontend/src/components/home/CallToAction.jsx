import {Link} from 'react-router-dom';

/*
 * CallToAction component
 * This component renders a section with a call-to-action message and a button to book a free consultation.
 * The section is styled with a background color, padding, and text alignment.
 * The call-to-action message is displayed in a heading and a paragraph.
 * The button is a link to the "/book" route, with the text "Book Your Free Consultation".
 * The button is styled with a primary color and added padding for better visibility.
 * The component is exported as the default export.
 * @returns {JSX.Element} The rendered CallToAction component
 * @constructor
 */
const CallToAction = () => {
   return (
      <section className="call-to-action">
         <div className="container">
            <h2>Ready to Start Your Journey?</h2>
            <p>Take the first step towards financial freedom and personal growth with GoalGlo.</p>
            <Link to="/book" className="btn btn-primary">Book Your Free Consultation</Link>
         </div>
      </section>
   );
};

export default CallToAction;