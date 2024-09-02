import {useDispatch, useSelector} from "react-redux";
import {sendContactMessage} from "../redux/actions/contactActions.js";
import ContactForm from "../components/contact/ContactForm.jsx";
import ContactInfo from "../components/contact/ContactInfo.jsx";

/**
 * Contact page component
 * @returns {JSX.Element}
 * @constructor
 */
const ContactPage = () => {
   const dispatch = useDispatch();
   const {loading, error, success} = useSelector(state => state.contact);

   const handleSubmit = (formData) => {
      dispatch(sendContactMessage(formData));
   };

   return (
      <div className="page contact-page">
         <h1>Contact Us</h1>
         <div className="contact-container">
            <ContactForm onSubmit={handleSubmit} loading={loading}/>
            <ContactInfo/>
         </div>
         {error && <div className="error-message">{error}</div>}
         {success && <div className="success-message">Your message has been sent successfully!</div>}
      </div>
   );
};

export default ContactPage;