import {Link} from 'react-router-dom';

/**
 * Footer component
 * @returns {JSX.Element} The rendered Footer component
 * @constructor
 */
const Footer = () => {
   return (
      <footer className="footer">
         <div className="container">
            <div className="footer-content">
               <div className="footer-section">
                  <h3>About GoalGlo</h3>
                  <p>GoalGlo is your partner in achieving financial freedom and personal growth.</p>
               </div>
               <div className="footer-section">
                  <h3>Quick Links</h3>
                  <ul>
                     <li><Link to="/about">About Us</Link></li>
                     <li><Link to="/services">Our Services</Link></li>
                     <li><Link to="/blog">Blog</Link></li>
                     <li><Link to="/contact">Contact Us</Link></li>
                  </ul>
               </div>
               <div className="footer-section">
                  <h3>Connect With Us</h3>
                  <div className="social-icons">
                     {/* Add social media icons here */}
                  </div>
               </div>
            </div>
            <div className="footer-bottom">
               <p>&copy; 2024 GoalGlo. All rights reserved.</p>
            </div>
         </div>
      </footer>
   );
};

export default Footer;