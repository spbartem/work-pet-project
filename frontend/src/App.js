import './App.css';
import React from 'react';
import SideMenu from './components/SideMenu';
import EntPretenseBillStat from './components/EntPretenseBillStat';
import XmlParser from './components/XmlParser';
import Dashboard from './components/DashBoard';
import ReportsPageQuarterly from './components/reports/quarterly/ReportsPage';
import RepDownload from './components/reports/weekly/Rep';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import { createTheme, ThemeProvider } from '@mui/material/styles';

const theme = createTheme({
    components: {
        MuiListItemIcon: {
            styleOverrides: {
                root: {
                    marginRight: '5px', // Устанавливаем отступ
                    minWidth: '5px',   // Задаём минимальную ширину, если нужно
                },
            },
        },
        MuiListItem: {
            styleOverrides: {
                root: {
                    cursor: 'pointer', // Курсор в виде руки
                },
            },
        },
    },

    typography: {
      fontSize: 10, // Глобальный базовый размер шрифта
      h1: { fontSize: '2rem' },
      body1: { fontSize: '14px', lineHeight: 0.8 },
  },
});

function App() {
    const drawerWidth = 20; // Ширина бокового меню
  
    return (
      <Router>
        <div style={{ display: "flex" }}>
          {/* Боковое меню */}
        <ThemeProvider theme={theme}>
            <SideMenu />
        </ThemeProvider>
  
          {/* Основной контейнер */}
          <div
            style={{
              flexGrow: 1,
              marginLeft: `${drawerWidth}px`, // Сдвиг для учёта ширины бокового меню
              padding: "16px", // Отступы для контента
            }}
          >
            <Routes>
              <Route path="/" element={<Dashboard />} />
              <Route path="/ent_pretense_bill_stat" element={<EntPretenseBillStat />} />
              <Route path="/report_quarterly_procuracy" element={<ReportsPageQuarterly />} />
              <Route path="/report_weekly_rep" element={<RepDownload />} />
              <Route path="/xml_parser" element={<XmlParser />} />
            </Routes>
          </div>
        </div>
      </Router>
    );
  }

export default App;