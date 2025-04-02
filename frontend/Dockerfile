# Используем официальный Node.js образ
FROM node:22.14-alpine3.21 AS build

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем package.json и package-lock.json для установки зависимостей
COPY package*.json ./

# Устанавливаем все зависимости
RUN npm install

# Копируем весь исходный код проекта
COPY . .

# Строим приложение для продакшн
RUN npm run build

# Используем nginx для отдачи статических файлов
FROM nginx:alpine

# Копируем файлы из билд-каталога в nginx
COPY --from=build /app/build /usr/share/nginx/html

# Удаляем стандартную конфигурацию Nginx (опционально, чтобы избежать конфликтов)
RUN rm /etc/nginx/conf.d/default.conf

# Создаем свою конфигурацию Nginx, чтобы слушать порт 3000
RUN echo 'server { \
    listen 3000; \
    server_name localhost; \
    root /usr/share/nginx/html; \
    index index.html; \
    location / { \
        try_files $uri /index.html; \
    } \
}' > /etc/nginx/conf.d/default.conf

# Открываем порт для доступа
EXPOSE 3000

# Запускаем nginx
CMD ["nginx", "-g", "daemon off;"]