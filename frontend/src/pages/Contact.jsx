import {useState} from 'react';
import axios from 'axios';

const Contact = () => {
   const [name, setName] = useState('');
   const [email, setEmail] = useState('');
   const [message, setMessage] = useState('');

   const handleSubmit = (e) => {
      e.preventDefault();
      axios.post('/api/contact', {name, email, message})
         .then(response => console.log('Message sent:', response))
         .catch(error => console.error('Error sending message:', error));
   };

   return (
      <div>
         <h2>Contact Us</h2>
         <form onSubmit={handleSubmit}>
            <div>
               <label>Name:</label>
               <input
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
               />
            </div>
            <div>
               <label>Email:</label>
               <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
               />
            </div>
            <div>
               <label>Message:</label>
               <textarea
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
               />
            </div>
            <button type="submit">Send Message</button>
         </form>
      </div>
   );
}

export default Contact;