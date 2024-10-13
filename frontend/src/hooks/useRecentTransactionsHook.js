import {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {fetchRecentTransactions} from '../redux/actions/recentTransactionActions';

/**
 * useRecentTransactionsHook
 * @returns {{transactions: Array, loading: boolean, error: string}}
 */
export const useRecentTransactionsHook = () => {
   const dispatch = useDispatch();
   const {transactions, loading, error} = useSelector(state => state.recentTransactions);
   const fetchedRef = useRef(false);

   useEffect(() => {
      if (!fetchedRef.current) {
         dispatch(fetchRecentTransactions());
         fetchedRef.current = true;
      }
   }, [dispatch]);

   return {transactions, loading, error};
};