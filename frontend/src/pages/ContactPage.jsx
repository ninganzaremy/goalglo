import ContactForm from "../components/contact/ContactForm.jsx";
import ContactInfo from "../components/contact/ContactInfo.jsx";

/**
 * Contact page component
 * @returns {JSX.Element}
 * @constructor
 */
const ContactPage = () => {
   return (
      <div className="contact-page">
         <h1>Contact Us</h1>
         <div className="contact-container">
            <ContactForm/>
            <ContactInfo/>
         </div>
      </div>
   );
};

export default ContactPage;