FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/ROOT.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.war"]