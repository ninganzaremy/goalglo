
/*
 * ContactInfo component
 * This component displays contact information, including address, phone number, and email.
 * It also includes social media links for the user to connect with them.
 * The component is styled using the CSS module 'contact-info.module.css'.
 */

const ContactInfo = () => {
   return (
      <div className="contact-info">
         <h2>Get in Touch</h2>
         <p>Were here to help and answer any question you might have. We look forward to hearing from you!</p>
         <ul>
            <li>
               <i className="fas fa-map-marker-alt"></i>
               <span>123 Financial Street, Money City, 12345</span>
            </li>
            <li>
               <i className="fas fa-phone"></i>
               <span>(123) 456-7890</span>
            </li>
            <li>
               <i className="fas fa-envelope"></i>
               <span>support@goalglo.com</span>
            </li>
         </ul>
         <div className="social-links">
            <a href="#" target="_blank" rel="noopener noreferrer"><i className="fab fa-facebook"></i></a>
            <a href="#" target="_blank" rel="noopener noreferrer"><i className="fab fa-twitter"></i></a>
            <a href="#" target="_blank" rel="noopener noreferrer"><i className="fab fa-linkedin"></i></a>
         </div>
      </div>
   );
};

export default ContactInfo;