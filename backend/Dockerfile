# Этап сборки: используем образ Maven
FROM maven:3.9.9-amazoncorretto-17 AS build

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем файлы проекта (включая pom.xml)
COPY . .

# Собираем проект и упаковываем в JAR
RUN mvn clean package -DskipTests

# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-alpine

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем собранный JAR из предыдущего этапа
COPY --from=build /app/target/work-pet-project-0.0.1-SNAPSHOT.jar /app/my-app.jar

# Открываем порт, на котором будет работать бэкенд
EXPOSE 5000

# Запускаем JAR файл
CMD ["java", "-jar", "my-app.jar"]