import React, {useEffect, useState} from "react";
import axios from "axios";
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer } from 'recharts';

const backendUrl = process.env.REACT_APP_API_URL;
console.log(backendUrl);

const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div style={{ 
          backgroundColor: 'white', 
          border: '1px solid #ccc', 
          padding: '10px', 
          borderRadius: '5px', 
          boxShadow: '0 2px 5px rgba(0,0,0,0.2)'
        }}>
          {payload.map((entry, index) => (
            <p key={`item-${index}`} style={{ margin: 0 }}>
              <span style={{ fontWeight: 'bold' }}>{entry.name}:</span> {entry.value.toLocaleString()} ₽
            </p>
          ))}
        </div>
      );
    }
  
    return null;
  };

const Dashboard = () => {
    const [data, setData] = useState(null);

    useEffect(() => {
        axios.get(`${backendUrl}/api/debt/operational-by-period`)
            .then(response => setData(response.data))
            .catch(error => console.error(error));
    }, []);

    if (!data) {
        return <div>Загрузка...</div>;
    }

    const formatter = new Intl.NumberFormat('ru-RU', {
        maximumFractionDigits: 2,
      });

    const formatDate = (dateString) => {
    // Преобразуем строку даты в объект Date
    const date = new Date(dateString);
    
    // Получаем день, месяц и год
    const day = String(date.getDate()).padStart(2, '0'); // Добавляем ведущий 0
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Месяцы начинаются с 0
    const year = date.getFullYear();
    
    // Возвращаем строку в формате dd.mm.yyyy
    return `${day}.${month}.${year}`;
    };  

    const CustomLabel = ({ x, y, name, width = 100 }) => {
        // Разбиваем текст, если он превышает заданную ширину
        const words = name.split(' ');
        const lines = [];
        let currentLine = words[0];
    
        words.slice(1).forEach((word) => {
            if ((currentLine + ' ' + word).length > width / 10) {
                lines.push(currentLine);
                currentLine = word;
            } else {
                currentLine += ' ' + word;
            }
        });
        lines.push(currentLine);
    
        return (
            <text x={x} y={y} textAnchor="middle" dominantBaseline="central" fill="#000">
                {lines.map((line, index) => (
                    <tspan x={x} dy={index === 0 ? 0 : 15} key={index}>
                        {line}
                    </tspan>
                ))}
            </text>
        );
    };

    // Преобразуем данные для диаграммы
    const chartData = [
        { name: 'Всего', value: data.debtOperationalTotal },
        { name: 'До 3-х месяцев', value: data.debtOperationalLess3m },
        { name: 'От 3-х до 7-ми месяцев', value: data.debtOperationalBetween3mAnd7m },
        { name: 'От 7-ми месяцев до 2-х лет', value: data.debtOperationalBetween7mAnd2y },
        { name: 'От 2-х до 3-х лет', value: data.debtOperationalBetween2yAnd3y },
        { name: 'Более 3-х лет', value: data.debtOperationalMore3y },
    ];

    const chartDataFiltered = [
        { name: 'До 3-х мес.', value: data.debtOperationalLess3m },
        { name: '3 мес. - 7 мес.', value: data.debtOperationalBetween3mAnd7m },
        { name: '7 мес. - 2 года', value: data.debtOperationalBetween7mAnd2y },
        { name: '2 - 3 года', value: data.debtOperationalBetween2yAnd3y },
        { name: 'Более 3-х лет', value: data.debtOperationalMore3y },
    ];

    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#FF9999'];

    return (
        <div className='flex-container_50_percents'>
            {/* Таблица */}
            <div style={{ flex: 1 }}>
                <table border="1" style={{ marginBottom: '0px', width: '100%', tableLayout: 'auto', textAlign: 'center' }}>
                    <thead style={{ backgroundColor: '#f5f5f5', fontWeight: 'bold', fontSize: '18px' }}>
                        <tr>
                            <th colSpan="2">Оперативная задолженность на {formatDate(data.reportDate)}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Период задолженности</td>
                            <td>Сумма задолженности, ₽</td>
                        </tr>
                        <tr style={{ backgroundColor: '#f9f9f9' }}>
                            <td>До 3-х месяцев</td>
                            <td>{data.debtOperationalLess3m.toLocaleString()}</td>
                        </tr>
                        <tr>
                            <td>От 3-х до 7-ми месяцев</td>
                            <td>{data.debtOperationalBetween3mAnd7m.toLocaleString()}</td>
                        </tr>
                        <tr style={{ backgroundColor: '#f9f9f9' }}>
                            <td>От 7-ми мес. до 2-х лет</td>
                            <td>{data.debtOperationalBetween7mAnd2y.toLocaleString()}</td>
                        </tr>
                        <tr>
                            <td>От 2-х до 3-х лет</td>
                            <td>{data.debtOperationalBetween2yAnd3y.toLocaleString()}</td>
                        </tr>
                        <tr tr style={{ backgroundColor: '#f9f9f9' }}>
                            <td>Более 3-х лет</td>
                            <td>{data.debtOperationalMore3y.toLocaleString()}</td>
                        </tr>
                        <tr style={{ fontWeight: 'bold', fontSize: '18px' }}>
                            <td>Всего</td>
                            <td>{data.debtOperationalTotal.toLocaleString()}</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            {/* Круговая диаграмма */}
            <div style={{ 
                flex: 1,       // Включаем Flexbox
                justifyContent: 'center', // Центрируем по горизонтали
                alignItems: 'center',    // Центрируем по вертикали
                height: '100%', 
             }}>
                <ResponsiveContainer width="100%" height={300}>
                    <PieChart>
                        <Pie
                            data={chartDataFiltered}
                            dataKey="value"
                            nameKey="name"
                            cx="54%"
                            cy="50%"
                            outerRadius="70%" 
                            fill="#8884d8"
                            label={({ name, x, y, fill }) => {
                                const offset = 5; // Смещение подписи
                                return (
                                    <text
                                        x={x + (x > 200 ? offset : -offset)} // Смещаем вправо или влево
                                        y={y + (y > 200 ? offset : -offset)}
                                        fill={fill}
                                        className="pie-label"
                                        textAnchor={x > 200 ? 'start' : 'end'} // Выравнивание текста
                                        dominantBaseline="central"
                                    >
                                        {name}
                                    </text>
                                );
                            }}
                        >
                            {chartDataFiltered.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip content={<CustomTooltip />} />
                    </PieChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
};

export default Dashboard;