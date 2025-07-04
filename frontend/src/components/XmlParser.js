import React, { useState } from "react";

const App = () => {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const backendUrl = process.env.REACT_APP_API_URL;

    if (!file) {
      setMessage("Please select a file.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${backendUrl}/api/rostreetr/upload`, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        setMessage("File processed and info saved!");
      } else {
        const errorText = await response.text();
        setMessage(errorText || "Error processing file.");
      }
    } catch (error) {
      setMessage("An error occurred while uploading the file.");
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="text-center">Upload XML File</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="file">Choose XML file:</label>
          <input
            type="file"
            className="form-control-file"
            id="file"
            accept=".xml"
            onChange={handleFileChange}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Upload
        </button>
      </form>
      {message && <div className="mt-3">{message}</div>}
    </div>
  );
};

export default App;
