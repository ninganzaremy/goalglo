import {Link} from 'react-router-dom';

/**
 * ConfirmationPage component
 * @param {string} title - The title of the confirmation page
 * @param {string} message - The message to be displayed
 * @param {string} actionText - The text for the action button
 * @param {string} actionLink - The link for the action button (if applicable)
 * @param {function} actionOnClick - The function to be called when the action button is clicked
 * @param {string} status - The status of the confirmation page (e.g., 'success', 'error')
 * @returns {JSX.Element} The ConfirmationPage component
 */
const ConfirmationPage = ({title, message, actionText, actionLink, actionOnClick, status = 'success'}) => {
   return (
      <div className="confirmation-page">
         <div className={`confirmation-card ${status}`}>
            <h2>{title}</h2>
            <p>{message}</p>
            {actionText && (actionLink || actionOnClick) && (
               actionLink ? (
                  <Link to={actionLink} className="btn-primary">
                     {actionText}
                  </Link>
               ) : (
                  <button onClick={actionOnClick} className="btn-primary">
                     {actionText}
                  </button>
               )
            )}
         </div>
      </div>
   );
};

export default ConfirmationPage;