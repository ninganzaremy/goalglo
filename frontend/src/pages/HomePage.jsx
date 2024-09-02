import Hero from '../components/home/Hero';
import ServiceHighlights from '../components/home/ServiceHighlights';
import AboutUs from '../components/home/AboutUs.jsx';
import HowItWorks from '../components/home/HowItWorks';
import Testimonials from '../components/home/Testimonials';
import LatestBlogPosts from '../components/home/LatestBlogPosts';
import CallToAction from '../components/home/CallToAction';

/**
 * HomePage page
 * @returns {JSX.Element}
 */
const HomePage = () => {
   return (
      <div className="home-page">
         <Hero/>
         <ServiceHighlights/>
         <AboutUs/>
         <HowItWorks/>
         <Testimonials/>
         <LatestBlogPosts/>
         <CallToAction/>
      </div>
   );
};

export default HomePage;