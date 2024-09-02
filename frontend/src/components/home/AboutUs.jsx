import {Link} from 'react-router-dom';

/**
 * Renders the AboutUs component
 * @returns {JSX.Element} The rendered component
 * @constructor
 */
const AboutUs = () => {
  return (
    <section className="about-us">
      <div className="container">
        <h2>About GoalGlo</h2>
        <p>At GoalGlo, we're passionate about helping you achieve your financial goals and personal growth. Our team of expert advisors is dedicated to providing personalized strategies tailored to your unique situation.</p>
        <Link to="/about" className="btn btn-secondary">Learn More About Us</Link>
      </div>
    </section>
  );
};

export default AboutUs;