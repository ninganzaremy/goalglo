import {Link} from 'react-router-dom';

const Header = () => {
   return (
      <header>
         <h1>GoalGlo</h1>
         <nav>
            <Link to="/">Home</Link>
            <Link to="/blog">Blog</Link>
            <Link to="/dashboard">Dashboard</Link>
            <Link to="/admin">Admin Panel</Link>
            <Link to="/register">Register</Link>
            <Link to="/login">Login</Link>
            <Link to="/contact">Contact</Link>
         </nav>
      </header>
   );
}

export default Header;