import React from "react";

interface PaginationInterface {
  currentPage: number;
  totalPages: number;
  pagination: (page: number) => void;
}

const Pagination: React.FC<PaginationInterface> = (props) => {
  const listPage: number[] = [];

  // Ensure totalPages is greater than 0
  if (props.totalPages === 0) return null;

  // Create page number list to display
  if (props.currentPage === 1) {
    listPage.push(props.currentPage);
    if (props.totalPages >= props.currentPage + 1) {
      listPage.push(props.currentPage + 1);
    }
    if (props.totalPages >= props.currentPage + 2) {
      listPage.push(props.currentPage + 2);
    }
  } else if (props.currentPage > 1) {
    if (props.currentPage >= 3) {
      listPage.push(props.currentPage - 2);
    }
    if (props.currentPage >= 2) {
      listPage.push(props.currentPage - 1);
    }
    listPage.push(props.currentPage);
    if (props.totalPages >= props.currentPage + 1) {
      listPage.push(props.currentPage + 1);
    }
    if (props.totalPages >= props.currentPage + 2) {
      listPage.push(props.currentPage + 2);
    }
  }

  return (
    <nav aria-label="Page navigation example">
      <ul className="pagination">
        {/* Previous button */}
        <li className={`page-item ${props.currentPage === 1 ? 'disabled' : ''}`}>
          <button 
            className="page-link" 
            disabled={props.currentPage === 1} 
            onClick={() => props.currentPage > 1 && props.pagination(props.currentPage - 1)}
          >
            Previous
          </button>
        </li>

        {/* Page numbers */}
        {listPage.map((list) => (
          <li className={`page-item ${props.currentPage === list ? 'active' : ''}`} key={list}>
            {props.currentPage === list ? (
              <span className="page-link">
                {list}
                <span className="sr-only">(current)</span>
              </span>
            ) : (
              <button className="page-link" onClick={() => props.pagination(list)}>
                {list}
              </button>
            )}
          </li>
        ))}

        {/* Next button */}
        <li className={`page-item ${props.currentPage === props.totalPages ? 'disabled' : ''}`}>
          <button 
            className="page-link" 
            disabled={props.currentPage === props.totalPages} 
            onClick={() => props.currentPage < props.totalPages && props.pagination(props.currentPage + 1)}
          >
            Next
          </button>
        </li>
      </ul>
    </nav>
  );
};

export default Pagination;
