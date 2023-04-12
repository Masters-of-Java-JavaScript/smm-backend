FROM arm64v8/openjdk:21-ea-17-slim-buster
COPY build/libs/smm-backend-0.0.1-SNAPSHOT.jar smm-backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/smm-backend-0.0.1-SNAPSHOT.jar"]
