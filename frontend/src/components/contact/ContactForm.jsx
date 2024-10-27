import {useState} from "react";
import {useDispatch} from "react-redux";
import toast, {Toaster} from 'react-hot-toast';
import {sendContactMessage} from "../../redux/actions/contactActions";

/**
 * ContactForm component
 * @param {function} onSubmit - Callback function to handle form submission
 * @param {boolean} loading - Flag indicating if the form is being submitted
 * @returns {JSX.Element} - Rendered ContactForm component
 */
const ContactForm = () => {
   const dispatch = useDispatch();
   const [loading, setLoading] = useState(false);
   const [formData, setFormData] = useState({
      name: '',
      email: '',
      subject: '',
      message: ''
   });

   const handleChange = (e) => {
      const {name, value} = e.target;
      setFormData(prevData => ({
         ...prevData,
         [name]: value
      }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);

      toast.promise(
         new Promise((resolve, reject) => {
            dispatch(sendContactMessage(formData))
               .then(() => resolve())
               .catch(() => reject());
         }),
         {
            loading: 'Sending your message...',
            success: 'Your message has been sent successfully!',
            error: 'Failed to send message. Please try again.',
         }
      )
         .then(() => {
            setLoading(false);
            // Reset the form
            setFormData({
               name: '',
               email: '',
               subject: '',
               message: ''
            });
         })
         .catch(() => {
            setLoading(false);
         });
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
         <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
               type="email"
               id="email"
               name="email"
               value={formData.email}
               onChange={handleChange}
               required
            />
         </div>
         <div className="form-group">
            <label htmlFor="subject">Subject</label>
            <input
               type="text"
               id="subject"
               name="subject"
               value={formData.subject}
               onChange={handleChange}
               required
            />
         </div>
         <div className="form-group message-field">
            <label htmlFor="message">Message</label>
            <textarea
               id="message"
               name="message"
               value={formData.message}
               onChange={handleChange}
               required
            />
         </div>
         <button type="submit" disabled={loading}>
            {loading ? 'Sending...' : 'Send Message'}
         </button>
         <Toaster position="top-left"/>
      </form>
   );
};

export default ContactForm;