/*
 * Pagination component
 * @param {number} currentPage - The current page number
 * @param {number} totalPages - The total number of pages
 * @param {function} onPageChange - A callback function to handle page change
 * @returns {JSX.Element} - The rendered Pagination component
 * @example
 * <Pagination currentPage={currentPage} totalPages={totalPages} onPageChange={handlePageChange} />
 */

const Pagination = ({ currentPage, totalPages, onPageChange }) => {
   const pageNumbers = [];

   for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(i);
   }

   return (
      <nav className="pagination">
         <ul>
            {pageNumbers.map(number => (
               <li key={number} className={number === currentPage ? 'active' : ''}>
                  <button onClick={() => onPageChange(number)}>{number}</button>
               </li>
            ))}
         </ul>
      </nav>
   );
};

export default Pagination;