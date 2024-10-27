import {Link} from "react-router-dom";

/**
 * Renders the AboutUs component
 * @returns {JSX.Element} The rendered component
 * @constructor
 */
const AboutUs = () => {
   return (
      <section className="about-us">
         <div className="container">
            <h2 className="section-title">About GoalGlo</h2>
            <div className="about-content">
               <div>
                  <p>
                     At <strong>GoalGlo</strong>, we are your trusted partner in financial empowerment. Our mission is
                     to help individuals and professionals alike navigate the complexities of finance with confidence
                     and clarity. We offer tailored financial strategies, career development opportunities, and
                     educational resources designed to help you grow—whether you're securing your personal financial
                     future or building a thriving career in the financial sector.
                  </p>

                  <h3>What We Specialize In</h3>
                  <p>
                     Our expertise lies in <em>high cash value life insurance, retirement planning, estate
                     preservation,</em> and <em>professional training</em>. Our team of seasoned experts brings not only
                     years of experience but also a human touch to every service we offer. We believe in providing the
                     tools to succeed while fostering a deep understanding of how to use them effectively.
                  </p>

                  <h3>How We Help You Succeed</h3>
                  <p>
                     With a balanced approach that includes personalized consulting, professional certification
                     programs, and financial health checkups, GoalGlo is committed to helping you achieve your financial
                     goals—whether personal or professional. Let us guide you through every step of your journey, from
                     building your financial foundation to reaching new career heights.
                  </p>
               </div>

               <div className="vision-values">
                  <h3>Our Vision</h3>
                  <p>
                     To make financial empowerment accessible, personalized, and
                     achievable for everyone.
                  </p>

                  <h3>Our Values</h3>
                  <p>
                     Integrity, education, collaboration, and innovation in
                     every financial decision and service we offer.
                  </p>
               </div>

               <p>
                  Join us on the path to financial success and let GoalGlo
                  illuminate your way to a brighter financial future.
               </p>
            </div>
            <Link to="/about" className="btn btn-secondary">
               Learn More About Us
            </Link>
         </div>
      </section>
   );
};

export default AboutUs;