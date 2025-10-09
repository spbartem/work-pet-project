export const Button = ({ children, ...props }) => (
  <button className="px-3 py-1 rounded bg-blue-600 text-white hover:bg-blue-700" {...props}>
    {children}
  </button>
);