import {Link} from 'react-router-dom';
import {FaFacebookF, FaGithub, FaLinkedinIn, FaTwitter, FaYoutube} from 'react-icons/fa';

/**
 * Footer component
 * @returns {JSX.Element} The rendered Footer component
 * @constructor
 */
const Footer = () => {
   const currentYear = new Date().getFullYear();

   return (
      <footer className="footer">
         <div className="container">
            <div className="footer-content">
               <div className="footer-section">
                  <h3>GoalGlo</h3>
                  <ul>
                     <li><Link to="/about">About Us</Link></li>
                     <li><Link to="/services">Our Services</Link></li>
                     <li><Link to="/services">Happy Customers</Link></li>
                     <li><Link to="/blog">Our Blog</Link></li>
                  </ul>
               </div>
               <div className="footer-section">
                  <h3>Quick Links</h3>
                  <ul>
                     <li><Link to="/login">Login</Link></li>
                     <li><Link to="/register">Register</Link></li>
                     <li><Link to="/book">Book Us</Link></li>
                     <li><Link to="/contact">Contact Us</Link></li>
                  </ul>
               </div>
               <div className="footer-section">
                  <h3>Connect With Us</h3>
                  <div className="social-icons">
                     <a href="https://www.youtube.com/goalglo" target="_blank" rel="noopener noreferrer"
                        aria-label="YouTube"><FaYoutube/></a>
                     <a href="https://www.facebook.com/goalglo" target="_blank" rel="noopener noreferrer"
                        aria-label="Facebook"><FaFacebookF/></a>
                     <a href="https://www.twitter.com/goalglo" target="_blank" rel="noopener noreferrer"
                        aria-label="Twitter"><FaTwitter/></a>
                     <a href="https://www.github.com/goalglo" target="_blank" rel="noopener noreferrer"
                        aria-label="GitHub"><FaGithub/></a>
                     <a href="https://www.linkedin.com/company/goalglo" target="_blank" rel="noopener noreferrer"
                        aria-label="LinkedIn"><FaLinkedinIn/></a>
                  </div>
               </div>
            </div>
            <div className="footer-bottom">
               <p>&copy; {currentYear} GoalGlo. All rights reserved.</p>
            </div>
         </div>
      </footer>
   );
};

export default Footer;