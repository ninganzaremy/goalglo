import {useCallback, useEffect, useMemo, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {logoutUser} from '../redux/actions/loginAction';
import {decryptData, manageStorageEventListener, userSecuredRole} from '../security/securityConfig';

/**
 * useAuthHook
 * @returns {{user: *, isAuthenticated: boolean, loading: boolean, error: string, hasSecuredRole: boolean}}
 */
export const useAuthHook = () => {
   const dispatch = useDispatch();
   const {user, isAuthenticated, loading, error} = useSelector((state) => state.auth);
   const prevAuthState = useRef({user, isAuthenticated, loading, error});

   const handleStorageChange = useCallback((e) => {
      if (e.key === 'logout') {
         dispatch(logoutUser());
      }
   }, [dispatch]);

   useEffect(() => {
      manageStorageEventListener("add", handleStorageChange);
      return () => {
         manageStorageEventListener("remove", handleStorageChange);
      };
   }, [handleStorageChange]);

   const hasSecuredRole = useMemo(() => {
      return user && user.roles && user.roles.includes(decryptData(userSecuredRole()));
   }, [user]);

   useEffect(() => {
      const prevState = prevAuthState.current;
      if (
         prevState.isAuthenticated !== isAuthenticated ||
         prevState.loading !== loading ||
         prevState.error !== error ||
         JSON.stringify(prevState.user) !== JSON.stringify(user)
      ) {
         prevAuthState.current = {user, isAuthenticated, loading, error};
      }
   }, [user, isAuthenticated, loading, error]);

   return useMemo(() => ({
      user,
      isAuthenticated,
      loading,
      error,
      hasSecuredRole
   }), [user, isAuthenticated, loading, error, hasSecuredRole]);
};