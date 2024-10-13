import {useSelector} from 'react-redux';

export const usePasswordResetConfirmHook = () => {
   const {
      loading,
      success,
      error: resetError,
   } = useSelector((state) => state.auth.passwordResetConfirm);

   return {loading, success, resetError};
};