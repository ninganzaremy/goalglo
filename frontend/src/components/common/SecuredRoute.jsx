import {Navigate} from 'react-router-dom';
import {useAuthContext} from "../../hooks/useAuthContext";

/**
 * SecuredRoute component
 * This component checks if the user is authenticated and has the secured role
 * If the user is not authenticated or does not have the secured role, they are redirected to the appropriate page
 * If the user is authenticated and has the secured role, the children components are rendered
 */
const SecuredRoute = ({children}) => {
   const {isAuthenticated, hasSecuredRole, loading} = useAuthContext();

   if (loading) {
      return <div>Loading...</div>;
   }

   if (!isAuthenticated) {
      console.error("User is not authenticated. Redirecting to /login")
      return <Navigate to="/login" replace/>;
   }

   if (!hasSecuredRole) {
      console.error("User does not have secured role. Redirecting to /unauthorized")
      return <Navigate to="/unauthorized" replace/>;
   }

   return children;
};

export default SecuredRoute;