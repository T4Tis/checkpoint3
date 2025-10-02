FROM maven:3.9.1-eclipse-temurin-17

COPY . /opt/app

WORKDIR /opt/app

RUN mvn clean package -DskipTests

CMD ["java", "-jar", "target/checkpoint3-0.0.1-SNAPSHOT.jar"]