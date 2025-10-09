export const Progress = ({ value }) => (
  <div className="w-full bg-gray-200 rounded h-2 overflow-hidden">
    <div className="bg-green-500 h-2" style={{ width: `${value}%` }}></div>
  </div>
);