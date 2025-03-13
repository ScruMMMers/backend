FROM gradle:8.12.0-jdk21 AS build

WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY core/build.gradle ./core/
COPY core/src ./core/src
COPY public ./public
COPY api ./api
COPY deanery ./deanery
COPY student ./student
COPY mapper ./mapper
COPY public_interface ./public_interface
COPY mapper ./mapper
COPY migration ./migration
RUN gradle --no-daemon --project-dir core build

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/core/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
