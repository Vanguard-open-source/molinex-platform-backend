FROM eclipse-temurin:27-jdk AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B -ntp dependency:go-offline

COPY src src
RUN ./mvnw -B -ntp clean package -DskipTests

FROM eclipse-temurin:27-jre

WORKDIR /app

COPY --from=build /workspace/target/molinex-platform-backend-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

USER 1001:1001

ENTRYPOINT ["java", "-jar", "app.jar"]
