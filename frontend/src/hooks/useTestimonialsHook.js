import {useCallback, useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {createTestimonial, fetchTestimonials, updateTestimonial} from "../redux/actions/testimonialActions.js";


/**
 * Custom hook for managing testimonials
 * @returns {Object} An object containing testimonial data, loading state, and error state
 */
export const useTestimonialsHook = () => {
   const dispatch = useDispatch();
   const {testimonials, loading, error} = useSelector(state => state.testimonials);

   const fetchTestimonialsData = useCallback(() => {
      dispatch(fetchTestimonials());
   }, [dispatch]);

   useEffect(() => {
      fetchTestimonialsData();
   }, [fetchTestimonialsData]);

   const [editingId, setEditingId] = useState(null);
   const [newTestimonial, setNewTestimonial] = useState({text: ''});

   const handleEdit = useCallback((testimonial) => {
      setNewTestimonial({text: testimonial.text});
      setEditingId(testimonial.id);
   }, []);

   const handleCancelEdit = useCallback(() => {
      setNewTestimonial({text: ''});
      setEditingId(null);
   }, []);

   const handleTestimonialChange = useCallback((e) => {
      setNewTestimonial(prev => ({...prev, text: e.target.value}));
   }, []);

   const handleSubmit = useCallback((e) => {
      e.preventDefault();
      if (editingId) {
         if (window.confirm('Your edited testimonial will be hidden until an admin approves it. Do you want to proceed?')) {
            dispatch(updateTestimonial(editingId, newTestimonial));
         }
      } else {
         dispatch(createTestimonial(newTestimonial));
      }
      setNewTestimonial({text: ''});
      setEditingId(null);
   }, [dispatch, editingId, newTestimonial]);

   return {
      testimonials,
      loading,
      error,
      editingId,
      newTestimonial,
      handleEdit,
      handleCancelEdit,
      handleTestimonialChange,
      handleSubmit,
      setNewTestimonial,
      setEditingId
   };
};