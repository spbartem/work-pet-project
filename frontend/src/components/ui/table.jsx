export const Table = ({ children }) => <table className="w-full border">{children}</table>;
export const TableHead = ({ children }) => <thead className="bg-gray-100">{children}</thead>;
export const TableBody = ({ children }) => <tbody>{children}</tbody>;
export const TableRow = ({ children }) => <tr className="border-t">{children}</tr>;
export const TableCell = ({ children }) => <td className="px-2 py-1 border">{children}</td>;