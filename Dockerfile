FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN mvn -q -e -DskipTests package

CMD ["java", "-jar", "target/*.jar"]

EXPOSE 8080
