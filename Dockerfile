# --- STAGE 1: Build Stage (Cloud-layae JAR file create panrom) ---
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Un project files-ah cloud container kulla copy panrom
COPY pom.xml .
COPY src ./src

# Cloud-layae clean install panni .jar file-ah uruvakurom (Test-ah skip panrom fast-ah aaga)
RUN mvn clean package -DskipTests

# --- STAGE 2: Run Stage (App-ah run matum panra chinna box) ---
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# STAGE 1-la build aana .jar-ah intha run stage-ku copy panrom
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# App-ah run panrom
ENTRYPOINT ["java","-jar","app.jar"]