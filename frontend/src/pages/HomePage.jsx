import Hero from '../components/home/Hero';
import ServiceHighlights from '../components/home/ServiceHighlights';
import AboutUs from '../components/home/AboutUs';
import HowItWorks from '../components/home/HowItWorks';
import Testimonials from '../components/home/Testimonials';
import LatestBlogPosts from '../components/home/LatestBlogPosts';
import CallToAction from '../components/home/CallToAction';
import ScrollToTopButton from '../components/common/ScrollToTopButton';
import {useInView} from 'react-intersection-observer';

/**
 * Home page component
 * @returns {JSX.Element}
 * @constructor
 */
const HomePage = () => {
   const [heroRef, heroInView] = useInView({threshold: 0.1});
   const [servicesRef, servicesInView] = useInView({threshold: 0.1});
   const [aboutRef, aboutInView] = useInView({threshold: 0.1});
   const [howItWorksRef, howItWorksInView] = useInView({threshold: 0.1});
   const [testimonialsRef, testimonialsInView] = useInView({threshold: 0.1});
   const [blogRef, blogInView] = useInView({threshold: 0.1});
   const [ctaRef, ctaInView] = useInView({threshold: 0.1});

   return (
      <div className="home-page">
         <section ref={heroRef} className={`section hero-section ${heroInView ? 'fade-in' : ''}`}>
            <Hero/>
         </section>

         <section ref={servicesRef} className={`section services-section ${servicesInView ? 'slide-in-left' : ''}`}>
            <ServiceHighlights/>
         </section>

         <section ref={aboutRef} className={`section about-section ${aboutInView ? 'slide-in-right' : ''}`}>
            <AboutUs/>
         </section>

         <section ref={howItWorksRef} className={`section how-it-works-section ${howItWorksInView ? 'fade-in' : ''}`}>
            <HowItWorks/>
         </section>

         <section ref={testimonialsRef}
                  className={`section testimonials-section ${testimonialsInView ? 'slide-in-left' : ''}`}>
            <Testimonials/>
         </section>

         <section ref={blogRef} className={`section blog-section ${blogInView ? 'slide-in-right' : ''}`}>
            <LatestBlogPosts/>
         </section>

         <section ref={ctaRef} className={`section cta-section ${ctaInView ? 'fade-in' : ''}`}>
            <CallToAction/>
         </section>

         <ScrollToTopButton/>
      </div>
   );
};

export default HomePage;