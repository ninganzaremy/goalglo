import {Link} from "react-router-dom";

const Home = () => {
   return (
      <div>
         <h2>Welcome to GoalGlo</h2>
         <p>Helping you plan your financial future with ease.</p>
         <Link to="/dashboard">
            <button>Go to Dashboard</button>
         </Link>
         <Link to="/admin">
            <button>Go to Admin Panel</button>
         </Link>
      </div>
   );
}

export default Home;