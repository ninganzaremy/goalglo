import {useSelector} from 'react-redux';

export const usePasswordResetRequestHook = () => {
   const {loading, success, error} = useSelector((state) => state.auth.passwordReset);

   return {loading, success, error};
};