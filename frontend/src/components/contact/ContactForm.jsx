import {useState} from "react";

/**
 * ContactForm component
 * @param {function} onSubmit - Callback function to handle form submission
 * @param {boolean} loading - Flag indicating if the form is being submitted
 * @returns {JSX.Element} - Rendered ContactForm component
 */
const ContactForm = ({ onSubmit, loading }) => {
   // State to manage form data
   const [formData, setFormData] = useState({
      name: '',
      email: '',
      subject: '',
      message: ''
   });

   // Handler for input changes
   const handleChange = (e) => {
      setFormData({ ...formData, [e.target.name]: e.target.value });
   };

   // Handler for form submission
   const handleSubmit = (e) => {
      e.preventDefault();
      onSubmit(formData);
   };

   return (
      <form className="contact-form" onSubmit={handleSubmit}>
         <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
               type="text"
               id="name"
               name="name"
               value={formData.name}
               onChange={handleChange}
               required
            />
         </div>
         {/* Similar form groups for email, subject, and message */}
         <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Sending...' : 'Send Message'}
         </button>
      </form>
   );
};

export default ContactForm;