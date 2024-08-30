import {createRoot} from 'react-dom/client';
import {BrowserRouter as Router} from 'react-router-dom';
import {Provider} from 'react-redux';
import store from './redux/configureStore';
import App from './App';

/**
 * Main entry point of the application
 *
 * This file sets up the React 18 app with the modern Redux store and renders it to the DOM.
 */

const container = document.getElementById('root');
const root = createRoot(container);

root.render(
   <Provider store={store}>
      <Router>
         <App/>
      </Router>
   </Provider>
);