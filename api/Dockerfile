FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/couponnote-api.jar app.jar
ENV TZ=Asia/Seoul
EXPOSE 8081
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=50", "-jar", "app.jar"]
