import {useCallback, useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {fetchAllTestimonials, updateTestimonialStatus,} from "../redux/actions/adminActions.js";


/**
 * useAdminTestimonialsHook
 * @returns {{allTestimonials: Array, loading: boolean, error: string, handleStatusUpdate: Function, refreshTestimonials: Function}}
 */
export const useAdminTestimonialsHook = () => {
   const dispatch = useDispatch();
   const {allTestimonials, loading, error} = useSelector(
      (state) => state.testimonials
   );

   const fetchAllTestimonialsData = useCallback(() => {
      dispatch(fetchAllTestimonials());
   }, [dispatch]);

   useEffect(() => {
      fetchAllTestimonialsData();
   }, [fetchAllTestimonialsData]);

   const handleStatusUpdate = useCallback(
      (id, newStatus) => {
         dispatch(updateTestimonialStatus(id, newStatus));
      },
      [dispatch]
   );

   return {
      allTestimonials,
      loading,
      error,
      handleStatusUpdate,
      refreshTestimonials: fetchAllTestimonialsData,
   };
};