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
    const failedAttempts = useRef(0);

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

    const handleFill = async () => {
        setStatusMessage("Заполнение отчёта данными, пожалуйста, подождите...");
        setLoadingFill(true);
        localStorage.setItem("isFilling", "true");

        try {
                const response = await axios.post(`${apiEndpoint}/fill`, null, {
                timeout: 30000
            });
            console.log("Fill response:", response.data);

            startCheckingStatus(); // теперь тут, после успешного запуска
            fetchDates(); // обновляем список дат

            } catch (error) {
                console.error("Ошибка при заполнении:", error);

                // ЕСЛИ это просто таймаут — всё ещё запускаем проверку статуса!
                if (error.code === "ECONNABORTED") {
                    setStatusMessage("Ответ от сервера не получен сразу, пробуем отслеживать процесс...");
                    startCheckingStatus(); // запуск мониторинга
                } else {
                    setStatusMessage("Ошибка при заполнении данных.");
                    localStorage.removeItem("isFilling");
                    setLoadingFill(false);
                }
            } finally {
            setLoadingFill(false);
        }
    };

    const checkFillStatus = async () => {
        try {
            const response = await axios.get(`${apiEndpoint}/check-progress-status`, {
                timeout: 10000, // 10 сек
            });

            console.log("Статус прогресса:", response.data);
            failedAttempts.current = 0; // сбрасываем при успехе

            if (response.data.processed) {
                const { hours, minutes, seconds } = response.data.duration;
                setStatusMessage(`Заполнение отчёта данными, пожалуйста, подождите... Продолжительность: ${hours} ч. ${minutes} м. ${seconds} с.`);

                if (response.data.finished) {
                    clearInterval(intervalCheckFillStatus.current);
                    intervalCheckFillStatus.current = null;
                    setLoadingFill(false);
                    localStorage.removeItem("isFilling");
                    fetchDates(); // Обновляем список дат
                    setStatusMessage("Заполнение завершено.");
                }

            } else {
                console.log("Процесс ещё не начался или не активен.");
            }

        } catch (error) {
            if (error.code === 'ECONNABORTED') {
                failedAttempts.current += 1;
                setStatusMessage(`Сервер не отвечает. Повторная попытка... (${failedAttempts.current}/5)`);

                if (failedAttempts.current >= 5) {
                    clearInterval(intervalCheckFillStatus.current);
                    intervalCheckFillStatus.current = null;
                    setLoadingFill(false);
                    localStorage.removeItem("isFilling");
                    setStatusMessage("Ошибка: сервер не отвечает. Проверка остановлена.");
                }

            } else {
                console.error("Ошибка при получении статуса:", error);
                setStatusMessage("Ошибка при проверке статуса заполнения.");

                // ⛔ Останавливаем цикл опроса
                clearInterval(intervalCheckFillStatus.current);
                intervalCheckFillStatus.current = null;
                setLoadingFill(false);
                localStorage.removeItem("isFilling");
            }
        }
    };

    const startCheckingStatus = () => {
        if (intervalCheckFillStatus.current) {
            clearInterval(intervalCheckFillStatus.current);
        }

        // Ждём 2 секунды перед первым запросом, чтобы сервер успел начать обработку
        setTimeout(() => {
            checkFillStatus(); // первый запрос
            intervalCheckFillStatus.current = setInterval(checkFillStatus, 10000);
        }, 2000);
    };

    // useEffect: при загрузке страницы — продолжить опрос
    useEffect(() => {
        const isFilling = localStorage.getItem("isFilling");
        if (isFilling === "true") {
            startCheckingStatus();
        }

        return () => {
            if (intervalCheckFillStatus.current) {
                clearInterval(intervalCheckFillStatus.current);
                intervalCheckFillStatus.current = null;
            }
        };
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
