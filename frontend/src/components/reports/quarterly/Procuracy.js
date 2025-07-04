import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import '../../../ButtonDownload.css';

const Procuracy = ({ apiEndpoint }) => {
    const [allDates, setAllDates] = useState([]);
    const [years, setYears] = useState([]);
    const [selectedYear, setSelectedYear] = useState('');
    const [quarters, setQuarters] = useState([]);
    const [selectedQuarter, setSelectedQuarter] = useState('');
    const [filteredDates, setFilteredDates] = useState([]);
    const [selectedDate, setSelectedDate] = useState('');
    const [loading, setLoading] = useState(false);
    const [fillEnabled, setFillEnabled] = useState(false);
    const [periodForCalc, setPeriodForCalc] = useState('exce');
    const [loadingFill, setLoadingFill] = useState(false);
    const [statusMessage, setStatusMessage] = useState('');
    const intervalCheckFillStatus = useRef(null);

    const fetchDates = async () => {
            try {
                console.log(apiEndpoint);
                const response = await axios.get(`${apiEndpoint}/dates`);
                const dates = response.data;

                if (!Array.isArray(dates)) {
                    console.error('Ошибка: API вернул не массив', dates);
                    dates = []; // Даем дефолтное значение
                }
                setAllDates(dates);
    
                const uniqueYears = [...new Set(dates.map(date => date.split('-')[0]))];
                setYears(uniqueYears);
                if (uniqueYears.length > 0) {
                    setSelectedYear(uniqueYears[0]);
                }
            } catch (error) {
                console.error('Ошибка получения дат:', error);
            }
        };
    
    useEffect(() => {
        fetchDates();
    }, [apiEndpoint]);

    useEffect(() => {
        if (selectedYear) {
            const datesForYear = allDates.filter(date => date.startsWith(selectedYear));
            const uniqueQuarters = [...new Set(datesForYear.map(date => `Q${Math.ceil(parseInt(date.split('-')[1]) / 3)}`))];
            setQuarters(uniqueQuarters);
            setSelectedQuarter(uniqueQuarters[0] || '');
        }
    }, [selectedYear, allDates]);

    useEffect(() => {
        if (selectedYear && selectedQuarter) {
            const quarterNumber = parseInt(selectedQuarter.replace('Q', ''), 10);
            const datesForQuarter = allDates.filter(date => {
                const [year, month] = date.split('-');
                return year === selectedYear && Math.ceil(parseInt(month) / 3) === quarterNumber;
            });
            setFilteredDates(datesForQuarter);
            setSelectedDate(datesForQuarter.length > 0 ? datesForQuarter[0] : '');
        }
    }, [selectedYear, selectedQuarter, allDates]);

    
    const handleDownload = async () => {
        if (!selectedDate) {
            alert('Выберите дату перед скачиванием');
            return;
        }
        setLoading(true);
        try {
            const response = await axios.get(`${apiEndpoint}/download`, {
                params: { date: selectedDate },
                responseType: 'blob'
            });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `report_${selectedDate}.xlsx`);
            document.body.appendChild(link);
            link.click();
        } catch (error) {
            console.error('Error downloading report:', error);
        } finally {
            setLoading(false);
        }
    };

    //проверяем, можно ли нажать кнопку "Заполнить" (да, если нет отчёта за предыдущий относительно текущего квартал)
    useEffect(() => {
        const checkFillAvailability = async () => {
            try {
                const response = await axios.get(`${apiEndpoint}/check-fill`);
                console.log(response.data);
                setFillEnabled(response.data.res >= 0);
                setPeriodForCalc(`${response.data.report_quarter_out} кв. ${response.data.report_year_out}`)
            } catch (error) {
                console.error('Ошибка проверки возможности формирования отчёта:', error);
                setFillEnabled(false);
            }
        };
        checkFillAvailability();
    }, [loadingFill]);

    useEffect(() => {
        // Проверяем, выполняется ли заполнение после обновления страницы
        const isFilling = localStorage.getItem("isFilling");

        if (isFilling === "true") {
            startCheckingStatus(); // Запускаем проверку статуса после обновления
        }

        return () => {
            if (intervalCheckFillStatus) {
                clearInterval(intervalCheckFillStatus); // Очищаем интервал при размонтировании
            }
        };
    }, []);

    // Функция запуска проверки статуса
    const startCheckingStatus = () => {
        if (intervalCheckFillStatus.current) {
            clearInterval(intervalCheckFillStatus.current);
        }
        intervalCheckFillStatus.current = setInterval(checkFillStatus, 60000);
    };

    const handleFill = async () => {
        setStatusMessage("Заполнение отчёта данными, пожалуйста, подождите...");
        setLoadingFill(true);
        localStorage.setItem("isFilling", "true");
    
        try {

            startCheckingStatus();
            const response = await axios.post(`${apiEndpoint}/fill`);
            console.log("Fill response:", response.data);
            fetchDates();
            clearInterval(intervalCheckFillStatus);

            const durationResponse = await axios.get(`${apiEndpoint}/fill-duration`);
            console.log("Duration response:", durationResponse.data);

            if (durationResponse.data) {
                const durationMs = durationResponse.data;
                const hours = Math.floor(durationMs / 3600000);
                const minutes = Math.floor((durationMs % 3600000) / 60000);
                const seconds = Math.floor((durationMs % 60000) / 1000);
                
                setStatusMessage(`${response.data.err_mess}; длительность: ${hours} ч. ${minutes} м. ${seconds} с.`);
            }

        } catch (error) {
            console.error("Ошибка при заполнении:", error);
            if (intervalCheckFillStatus.current) {
                clearInterval(intervalCheckFillStatus.current);
                intervalCheckFillStatus.current = null;
            }
            setStatusMessage("Ошибка при заполнении данных.");
        } finally {
            setLoadingFill(false);
            localStorage.removeItem("isFilling");            
        }
    };

    const checkFillStatus = async () => {
        try {
            const response = await axios.get(`${apiEndpoint}/check-progress-status`);
            console.log(response.data);

            if (response.data.processed) {
                setLoadingFill(true);
                setStatusMessage(`Заполнение отчёта данными, пожалуйста, подождите... Продолжительность: ${response.data.duration.hours} ч. ${response.data.duration.minutes} м. ${response.data.duration.seconds} с.` );
            }
        } catch (error) {
            console.error("Ошибка при получении статуса заполнения:", error);
            
        }
    };

    useEffect(() => {
        checkFillStatus();
    }, []);

    const getFirstDayOfNextQuarter = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = today.getMonth();
        
        const currentQuarter = Math.floor(month / 3) + 1;

        let nextQuarterMonth = currentQuarter * 3;
        let nextYear = year;
    
        if (nextQuarterMonth === 12) {
            nextQuarterMonth = 0;
            nextYear += 1;
        }

        return new Date(nextYear, nextQuarterMonth, 1).toLocaleDateString();
    };
    
    const firstDayOfNextQuarter = getFirstDayOfNextQuarter();

    return (
        <div className="space-y-4">
            <div className="flex items-center space-x-4">
                <div>
                    <label>Выберите год: </label>
                    <select value={selectedYear} onChange={(e) => setSelectedYear(e.target.value)} className="border px-2 py-1 rounded-md">
                        {years.map(year => <option key={year} value={year}>{year}</option>)}
                    </select>
                </div>
                <div>
                    <label>Выберите квартал: </label>
                    <select value={selectedQuarter} onChange={(e) => setSelectedQuarter(e.target.value)} className="border px-2 py-1 rounded-md">
                        {quarters.map(q => <option key={q} value={q}>{q}</option>)}
                    </select>
                </div>
                <div>
                    <label>Выберите дату: </label>
                    <select value={selectedDate} onChange={(e) => setSelectedDate(e.target.value)} disabled={!filteredDates.length} className="border px-2 py-1 rounded-md">
                        {filteredDates.map(date => <option key={date} value={date}>{date}</option>)}
                    </select>
                </div>
            </div>
            {statusMessage && <p>{statusMessage}</p>}
            <div className="flex space-x-4">
                <button 
                    onClick={handleDownload} 
                    disabled={!selectedDate || loading} 
                    className={`px-4 py-2 rounded-md text-white ${loading ? 'bg-gray-400' : 'bg-blue-500 hover:bg-blue-600'}`}
                >
                    {loading ? 'Скачивание...' : 'Скачать отчёт'}
                </button>
                <button 
                    onClick={handleFill} 
                    disabled={!fillEnabled || loadingFill} 
                    className={`px-4 py-2 rounded-md text-white ${!fillEnabled || loadingFill ? 'bg-gray-400' : 'bg-green-500 hover:bg-green-600'}`}
                    title={!fillEnabled ? `Заполнение отчёта станет доступно с ${firstDayOfNextQuarter}` : ""}
                >
                    {!fillEnabled ? "Заполнено" : loadingFill ? "Заполняется..." : `Заполнить за ${periodForCalc}`}
                </button>
            </div>
        </div>
    );
    
};

export default Procuracy;
