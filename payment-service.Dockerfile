#
# Build stage
#
FROM maven:3.9.12-amazoncorretto-21-alpine AS build
WORKDIR /home/app/

COPY ./pom.xml ./pom.xml
COPY ./common/pom.xml ./common/pom.xml
COPY ./service/pom.xml ./service/pom.xml
COPY ./service/analytics-service/pom.xml ./service/analytics-service/pom.xml
COPY ./service/execution-service/pom.xml ./service/execution-service/pom.xml
COPY ./service/order-service/pom.xml ./service/order-service/pom.xml
COPY ./service/payment-service/pom.xml ./service/payment-service/pom.xml

COPY ./common/src ./common/src
COPY ./service/payment-service/src ./service/payment-service/src

RUN mvn clean package --projects :common,:payment-service -DskipTests

#
# Package stage
#
FROM amazoncorretto:21-alpine-jdk
ENV APP_PORT=8080
COPY --from=build /home/app/service/payment-service/target/payment-service*.jar /usr/local/lib/payment-service.jar
EXPOSE ${APP_PORT}
ENTRYPOINT ["java","-jar","/usr/local/lib/payment-service.jar"]