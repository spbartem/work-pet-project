import React from "react";

export const ProgressIndeterminate = () => {
  return (
    <div className="w-full bg-gray-200 rounded h-2 overflow-hidden relative">
      <div className="absolute h-2 bg-green-500 animate-progress"></div>
    </div>
  );
};