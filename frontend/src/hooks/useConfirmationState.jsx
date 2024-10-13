import ConfirmationPage from '../components/common/ConfirmationPage';

/**
 * Custom hook to handle different confirmation states
 * @param {boolean} loading - Indicates if the confirmation is in a loading state
 * @param {boolean} success - Indicates if the confirmation is successful
 * @param {boolean} error - Indicates if there was an error during the confirmation
 * @param {Object} messages - An object containing message properties for different states
 * @returns {JSX.Element|null} - Returns the appropriate confirmation component or null
 */
const useConfirmationState = (loading, success, error, messages) => {
   if (loading) {
      return (
         <ConfirmationPage title={messages.loadingTitle || "Please Wait"}
                           message={messages.loadingMessage || "We're processing your request..."}
                           status="loading"
         />
      );
   }

   if (success) {
      return (
         <ConfirmationPage
            title={messages.successTitle || "Success!"}
            message={messages.successMessage}
            actionText={messages.successActionText}
            actionLink={messages.successActionLink}
            status="success"
         />
      );
   }

   if (error) {
      return (
         <ConfirmationPage
            title={messages.errorTitle || "Error"}
            message={messages.errorMessage || error}
            actionText={messages.errorActionText}
            actionOnClick={messages.errorActionOnClick}
            status="error"
         />
      );
   }

   return null;
};

export default useConfirmationState;