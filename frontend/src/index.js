import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom'; // <-- добавляем
import { ThemeProvider, createTheme } from '@mui/material/styles';
import App from './App';
import reportWebVitals from './reportWebVitals';

const theme = createTheme({
    palette: {
        primary: {
            main: '#4caf50',
        },
        secondary: {
            main: '#ff5722',
        },
    },
    typography: {
        fontFamily: 'Roboto, Arial',
        h1: {
            fontSize: '4.5rem',
        },
    },
});

ReactDOM.render(
    <BrowserRouter> {/* <-- обязательно здесь! */}
        <ThemeProvider theme={theme}>
            <App />
        </ThemeProvider>
    </BrowserRouter>,
    document.getElementById('root')
);

reportWebVitals();