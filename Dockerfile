FROM eclipse-temurin:17

LABEL mentainer="maulanamasiqbal@gmail.com"

WORKDIR /app

COPY target/technical-test-0.0.1-SNAPSHOT.jar /app/technical-test.jar

ENTRYPOINT ["java", "-jar", "technical-test.jar"]