FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/*SNAPSHOTd.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/static/menu/ /image/menu/
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080
