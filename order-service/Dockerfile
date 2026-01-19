#
# Build stage
#
FROM maven:3.9.12-amazoncorretto-21-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM amazoncorretto:21-alpine-jdk
ENV APP_PORT=8080
COPY --from=build /home/app/target/order-service-0.0.1-SNAPSHOT.jar /usr/local/lib/order-service.jar
EXPOSE ${APP_PORT}
ENTRYPOINT ["java","-jar","/usr/local/lib/order-service.jar"]