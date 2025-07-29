import React, { useState } from 'react';
import axios from 'axios';
import {
  Container,
  TextField,
  Button,
  Paper,
  Typography,
  Box,
} from '@mui/material';

const LoginPage = ({ onLogin }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(`${backendUrl}/api/login`, { username, password });
      console.log('Ответ от сервера:', res.data);

      const { token, role } = res.data;
      if (token && role) {
        localStorage.setItem('token', token);
        localStorage.setItem('role', role);
        onLogin(); // переход на следующую страницу
      } else {
        alert("Сервер не вернул ожидаемые данные");
      }
    } catch (err) {
      if (err.response && err.response.status === 401) {
        alert("Неверный логин или пароль");
      } else {
        console.error("Ошибка запроса:", err);
        alert("Ошибка входа. Попробуйте позже.");
      }
    }
  };

  return (
    <Container maxWidth="sm">
      <Paper elevation={3} sx={{ padding: 4, mt: 10 }}>
        <Typography variant="h5" align="center" gutterBottom>
          Вход
        </Typography>
        <Box component="form" onSubmit={handleSubmit} sx={{ mt: 2 }}>
          <TextField
            label="Логин"
            fullWidth
            variant="outlined"
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="Пароль"
            type="password"
            fullWidth
            variant="outlined"
            margin="normal"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 2 }}
          >
            Войти
          </Button>
        </Box>
      </Paper>
    </Container>
  );
};

export default LoginPage;
