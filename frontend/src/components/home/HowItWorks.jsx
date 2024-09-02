/*
 * @fileoverview This file contains the HowItWorks component, which displays the "How It Works" section of the application.
 * The component imports the necessary dependencies and defines the HowItWorks functional component.
 * It fetches the steps data from a local array and maps over it to render individual step components.
 * Each step component displays the step number, title, and description.
 * The component is styled using CSS modules and rendered within a section element with the class 'how-it-works'.
 */
const HowItWorks = () => {
   //todo implement API to dynamically fetch steps
   const steps = [
      { id: 1, title: 'Book a Consultation', description: 'Schedule a free consultation with one of our expert advisors.' },
      { id: 2, title: 'Personalized Plan', description: 'Receive a tailored financial plan based on your goals and circumstances.' },
      { id: 3, title: 'Implementation', description: 'Put your plan into action with our ongoing support and guidance.' },
      { id: 4, title: 'Regular Check-ins', description: 'Stay on track with regular check-ins and plan adjustments as needed.' },
   ];

   return (
      <section className="how-it-works">
         <div className="container">
            <h2>How It Works</h2>
            <div className="steps">
               {steps.map(step => (
                  <div key={step.id} className="step">
                     <div className="step-number">{step.id}</div>
                     <h3>{step.title}</h3>
                     <p>{step.description}</p>
                  </div>
               ))}
            </div>
         </div>
      </section>
   );
};

export default HowItWorks;