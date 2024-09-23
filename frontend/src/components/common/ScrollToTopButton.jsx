import {FaArrowUp} from 'react-icons/fa';
import {useEffect, useState} from "react";

/**
 * Scroll to top button
 * @returns {JSX.Element}
 * @constructor
 */

const ScrollToTopButton = () => {
   const [isVisible, setIsVisible] = useState(false);

   const toggleVisibility = () => {
      if (window.scrollY > 300) {
         setIsVisible(true);
      } else {
         setIsVisible(false);
      }
   };

   // Scroll to top smooth
   const scrollToTop = () => {
      window.scrollTo({
         top: 0,
         behavior: 'smooth'
      });
   };

   useEffect(() => {
      window.addEventListener('scroll', toggleVisibility);
      return () => {
         window.removeEventListener('scroll', toggleVisibility);
      };
   }, []);

   return (
      <>
         {isVisible && (
            <button
               onClick={scrollToTop}
               className="scroll-to-top"
               aria-label="Scroll to top"
            >
               <FaArrowUp/>
            </button>
         )}
      </>
   );
};

export default ScrollToTopButton;