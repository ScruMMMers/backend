FROM gradle:8.12.0-jdk21 AS build

WORKDIR /app

ARG POSTGRES_USER
ARG POSTGRES_PASSWORD

ENV POSTGRES_USER=$POSTGRES_USER
ENV POSTGRES_PASSWORD=$POSTGRES_PASSWORD

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
COPY marks ./marks
COPY locker ./locker
COPY meeting ./meeting
COPY company ./company
COPY statistic ./statistic
COPY tags ./tags
COPY tags_query ./tags_query
COPY position ./position
COPY file ./file
COPY oauth2_security ./oauth2_security
COPY websocket_common ./websocket_common
COPY students ./students
COPY announcement ./announcement
COPY document ./document
COPY employees ./employees
RUN gradle --no-daemon --project-dir core build -x test -Dorg.gradle.jvmargs=-Xmx4096m

FROM amazoncorretto:21-alpine
WORKDIR /app
COPY --from=build /app/core/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]
