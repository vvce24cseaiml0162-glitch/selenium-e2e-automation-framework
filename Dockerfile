# Production Docker image with Maven, Eclipse Temurin JDK 17, and Google Chrome
FROM markhobson/maven-chrome:jdk-17

# Set working directory
WORKDIR /app

# Copy pom.xml and cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy project source and test suite
COPY src ./src
COPY testng.xml .

# Volume mount for persisting ExtentReports and screenshots outside the container
VOLUME ["/app/reports"]

# Execution environment variables
ENV BROWSER=chrome
ENV HEADLESS=true

# Execution entrypoint
ENTRYPOINT ["mvn", "clean", "test", "-Dbrowser=chrome", "-Dheadless=true"]
