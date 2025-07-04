import React, { useState } from 'react';
import DatePicker from 'react-datepicker';
import axios from 'axios';
import 'react-datepicker/dist/react-datepicker.css';
import '../../../ButtonDownload.css';
const backendUrl = process.env.REACT_APP_API_URL; // Задаем URL бэкенда

const RepDownload = () => {
  const [selectedDate, setSelectedDate] = useState(() => {
    const yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    return yesterday;
  });
  const [isLoading, setIsLoading] = useState(false);

  const handleDownload = async () => {
    try {
      setIsLoading(true); // если используешь загрузочный индикатор
  
      const formattedDate = selectedDate
        ? selectedDate.getFullYear() + '-' +
          String(selectedDate.getMonth() + 1).padStart(2, '0') + '-' +
          String(selectedDate.getDate()).padStart(2, '0')
        : null;
  
      const response = await axios.get(`${backendUrl}/api/reports/weekly/rep/download`, {
        params: formattedDate ? { reportDate: formattedDate } : {},
        responseType: 'blob',
      });
  
      const blob = new Blob([response.data], { type: 'application/octet-stream' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
  
      // Надёжный разбор имени файла
      const disposition = response.headers['content-disposition'] || response.headers['Content-Disposition'];
      let filename = 'report.xlsx';
      if (disposition && disposition.includes('filename=')) {
        filename = disposition.split('filename=')[1].trim().replace(/^["']|["']$/g, '');
      }
  
      link.href = url;
      link.setAttribute('download', filename); // 👈 теперь имя должно работать
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Ошибка при скачивании отчёта:', error);
      alert('Не удалось скачать файл');
    } finally {
      setIsLoading(false);
    }
  };
  

  return (
    <div className="absolute top-5 left-105">
      <div className="border rounded-lg shadow-md p-6 bg-white w-80">
        <h2 className="text-xl font-semibold mb-4">РЕП</h2>
        <div className="mb-4">
          <label className="block mb-2">Выберите дату:</label>
          <DatePicker
            selected={selectedDate}
            onChange={date => setSelectedDate(date)}
            dateFormat="dd.MM.yyyy"
            placeholderText="ДД.ММ.ГГГГ"
            isClearable
            className="w-full px-3 py-2 border rounded-md"
          />
        </div>
        <button
          onClick={handleDownload}
          disabled={isLoading}
          className={`w-full px-4 py-2 rounded-md text-white transition-colors ${
            isLoading ? 'bg-gray-400 cursor-not-allowed' : 'bg-blue-500 hover:bg-blue-600'
          }`}
        >
          {isLoading ? '⏳ Скачивание...' : '📥 Скачать отчёт'}
        </button>
      </div>
    </div>
  );
};

export default RepDownload;
