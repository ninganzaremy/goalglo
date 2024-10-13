import {FaDirections, FaEnvelope, FaMapMarkerAlt, FaPhone} from "react-icons/fa";

/**
 * ContactInfo component
 * @returns {JSX.Element}
 * @constructor
 */

const ContactInfo = () => {
   const address = "1515 W Deer Valley Rd, Phoenix, AZ 85027";
   const encodedAddress = encodeURIComponent(address);
   const directionsUrl = `https://www.google.com/maps/dir/?api=1&destination=${encodedAddress}`;

   return (
      <div className="contact-info">
         <h2>Get in Touch</h2>
         <p>We're here to help and answer any question you might have. We look forward to hearing from you!</p>
         <div className="contact-details">
            <div>
               <FaPhone/>
               <span>(602) 805-6944</span>
            </div>
            <div>
               <FaEnvelope/>
               <span>support@goalglo.com</span>
            </div>
            <div>
               <FaMapMarkerAlt/>
               <span>Deer Valley Business Center<br/>1515 W Deer Valley Rd<br/>Phoenix, AZ 85027</span>
            </div>
         </div>
         <div className="google-map">
            <iframe
               src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3321.308453138682!2d-112.10118068479!3d33.68511048071276!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x872b6886a78bf925%3A0x48f9e76e48a9f8c3!2s1515%20W%20Deer%20Valley%20Rd%2C%20Phoenix%2C%20AZ%2085027!5e0!3m2!1sen!2sus!4v1619735563619!5m2!1sen!2sus"
               width="100%"
               height="250"
               style={{border: 0}}
               allowFullScreen=""
               loading="lazy"
            ></iframe>
            <a href={directionsUrl} target="_blank" rel="noopener noreferrer" className="directions-link">
               <FaDirections/> Get Directions
            </a>
         </div>
      </div>
   );
};

export default ContactInfo;