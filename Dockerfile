FROM openjdk:8-jdk-alpine
ADD target/fabric-api-1.0.jar app.jar
ENTRYPOINT [ "sh", "-c", "java -jar /app.jar" ]
