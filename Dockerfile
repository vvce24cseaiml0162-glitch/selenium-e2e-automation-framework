# Base image with Maven 3.9 and Eclipse Temurin JDK 17
FROM maven:3.9.6-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Install prerequisites and Google Chrome Stable
RUN apt-get update && apt-get install -y --no-install-recommends \
    wget \
    curl \
    ca-certificates \
    fonts-liberation \
    && wget -q https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb \
    && apt-get install -y --no-install-recommends ./google-chrome-stable_current_amd64.deb \
    && rm -f google-chrome-stable_current_amd64.deb \
    && rm -rf /var/lib/apt/lists/*

# Copy pom.xml and pre-fetch dependencies for Docker cache optimization
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and test suite
COPY src ./src
COPY testng.xml .

# Volume mounts for persistence of reports and screenshots outside container
VOLUME ["/app/reports"]

# Default environment configurations
ENV BROWSER=chrome
ENV HEADLESS=true

# Execution entrypoint
ENTRYPOINT ["mvn", "clean", "test", "-Dbrowser=chrome", "-Dheadless=true"]
