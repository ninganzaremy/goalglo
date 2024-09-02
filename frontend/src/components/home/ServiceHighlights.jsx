/**
 * ServiceHighlights component
 * @returns {JSX.Element} The rendered ServiceHighlights component
 * @constructor
 */
const ServiceHighlights = () => {
   //todo implement API to dynamically fetch ServiceHighlights
   const services = [
      { id: 1, title: 'Financial Planning', description: 'Plan your financial future with expert guidance' },
      { id: 2, title: 'Investment Advice', description: 'Get personalized investment strategies' },
      { id: 3, title: 'Retirement Planning', description: 'Secure your retirement with our expert advice' },
   ];

   return (
      <section className="service-highlights">
         <div className="container">
            <h2>Our Services</h2>
            <div className="service-list">
               {services.map(service => (
                  <div key={service.id} className="service-item">
                     <h3>{service.title}</h3>
                     <p>{service.description}</p>
                  </div>
               ))}
            </div>
         </div>
      </section>
   );
};

export default ServiceHighlights;