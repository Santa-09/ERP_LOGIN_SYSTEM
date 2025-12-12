package com.erp.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerUtil {
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static boolean debugMode = true;

    /**
     * Logs info message
     */
    public static void info(String className, String message) {
        log("INFO", className, message);
    }

    /**
     * Logs error message
     */
    public static void error(String className, String message) {
        log("ERROR", className, message);
    }

    /**
     * Logs error message with exception
     */
    public static void error(String className, String message, Exception e) {
        log("ERROR", className, message + " - " + e.getMessage());
        if (debugMode) {
            e.printStackTrace();
        }
    }

    /**
     * Logs warning message
     */
    public static void warning(String className, String message) {
        log("WARNING", className, message);
    }

    /**
     * Logs debug message
     */
    public static void debug(String className, String message) {
        if (debugMode) {
            log("DEBUG", className, message);
        }
    }

    /**
     * Core logging method
     */
    private static void log(String level, String className, String message) {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String logMessage = String.format("[%s] [%s] [%s] - %s", 
            timestamp, level, className, message);
        System.out.println(logMessage);
    }

    /**
     * Sets debug mode
     */
    public static void setDebugMode(boolean debug) {
        debugMode = debug;
    }

    /**
     * Gets debug mode status
     */
    public static boolean isDebugMode() {
        return debugMode;
    }

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }
}