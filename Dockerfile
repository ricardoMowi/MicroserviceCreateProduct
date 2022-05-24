FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/*.jar 
COPY ${JAR_FILE} create_product.jar
ENTRYPOINT ["java", "-jar", "/create_product.jar"]