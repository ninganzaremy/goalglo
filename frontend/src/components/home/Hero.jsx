import {Link} from 'react-router-dom';

/**
 * Hero component
 * @returns {JSX.Element} The rendered Hero component
 * @constructor
 */
const Hero = () => {
   return (
      <section className="hero">
         <h1>Welcome to GoalGlo</h1>
         <p>Your path to financial freedom and personal growth</p>
         <Link to="/services" className="cta-button">Explore Our Services</Link>
      </section>
   );
};

export default Hero;