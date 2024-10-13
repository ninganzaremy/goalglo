import {Link} from 'react-router-dom';

/**
 * Hero component
 * @returns {JSX.Element} The rendered Hero component
 * @constructor
 */
const Hero = () => {
   return (
      <section className="hero">
         <div className="hero__content">
            <h1 className="hero__title">Welcome to GoalGlo</h1>
            <p className="hero__subtitle">Your path to financial freedom and personal growth</p>
            <Link to="/services" className="hero__cta-button">Explore Our Services</Link>
         </div>
      </section>
   );
};

export default Hero;