FROM openjdk:11-slim
COPY /build/libs/*.jar eve.jar

CMD ["java","-jar", "/eve.jar"]
