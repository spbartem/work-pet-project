export const Pagination = ({ page, totalPages, onPageChange }) => (
  <div className="flex justify-center mt-4 gap-2">
    {Array.from({ length: totalPages }).map((_, idx) => (
      <button
        key={idx}
        onClick={() => onPageChange(idx)}
        className={`px-2 py-1 rounded ${idx === page ? 'bg-blue-600 text-white' : 'bg-gray-200'}`}
      >
        {idx + 1}
      </button>
    ))}
  </div>
);