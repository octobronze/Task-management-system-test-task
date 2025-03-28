FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY --from=assets pom.xml .
COPY --from=assets src ./src
RUN mvn clean package

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/Task-management-system-test-task-0.0.1-SNAPSHOT.jar .
CMD ["java", "-jar", "Task-management-system-test-task-0.0.1-SNAPSHOT.jar"]