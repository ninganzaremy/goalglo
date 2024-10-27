import {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchAccountSummary} from '../redux/actions/accountSummaryActions';

/**
 * useAccountSummaryHook
 * @returns {{balance: number, income: number, expenses: number, loading: boolean, error: string}}
 */
export const useAccountSummaryHook = () => {
   const dispatch = useDispatch();
   const {balance, income, expenses, loading, error} = useSelector(
      (state) => state.accountSummary || {}
   );
   const fetchedRef = useRef(false);

   useEffect(() => {
      if (!fetchedRef.current) {
         dispatch(fetchAccountSummary());
         fetchedRef.current = true;
      }
   }, [dispatch]);

   return {balance, income, expenses, loading, error};
};