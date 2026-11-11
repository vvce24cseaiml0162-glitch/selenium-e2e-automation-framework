package com.qa.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties in classpath");
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
            logger.info("Configuration properties loaded successfully");
        } catch (IOException ex) {
            logger.error("Failed to load configuration properties", ex);
            throw new RuntimeException("Error loading config.properties", ex);
        }
    }

    public static String getProperty(String key) {
        // Priority 1: System Property (passed via CLI -Dkey=value)
        String systemProp = System.getProperty(key);
        if (systemProp != null && !systemProp.trim().isEmpty()) {
            return systemProp.trim();
        }

        // Priority 2: System Environment Variable (converted dots to uppercase underscores e.g. APP_URL)
        String envKey = key.replace('.', '_').toUpperCase();
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        // Priority 3: config.properties file
        String fileValue = properties.getProperty(key);
        return fileValue != null ? fileValue.trim() : null;
    }

    public static String getProperty(String key, String defaultValue) {
        String val = getProperty(key);
        return val != null ? val : defaultValue;
    }

    public static int getIntProperty(String key, int defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key '{}': '{}', falling back to {}", key, val, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String val = getProperty(key);
        if (val == null) return defaultValue;
        return Boolean.parseBoolean(val);
    }
}

