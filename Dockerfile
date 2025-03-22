FROM gradle:8.12.0-jdk21 AS build

WORKDIR /app

COPY settings.gradle .
COPY build.gradle .
COPY gradle gradle
COPY core/build.gradle ./core/
COPY core/src ./core/src
COPY public ./public
COPY api ./api
COPY mapper ./mapper
COPY public_interface ./public_interface
COPY mapper ./mapper
COPY migration ./migration
COPY profile ./profile
COPY file_storage ./file_storage
COPY notification ./notification
COPY logs ./logs
COPY oauth2_security ./oauth2_security
RUN gradle --no-daemon --project-dir core build -x test

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/core/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
