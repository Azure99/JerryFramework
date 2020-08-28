package com.rainng.jerry.util;

import java.util.logging.Level;

public class Logger {
    private static final java.util.logging.Logger innerLogger = java.util.logging.Logger.getGlobal();

    private Logger() {

    }

    public static void error(String message) {
        innerLogger.log(Level.SEVERE, message);
    }

    public static void ex(Throwable throwable) {
        innerLogger.log(Level.SEVERE, throwable.getMessage(), throwable);
    }

    public static void ex(String message, Throwable throwable) {
        innerLogger.log(Level.SEVERE, message, throwable);
    }

    public static void warn(String message) {
        innerLogger.log(Level.WARNING, message);
    }

    public static void warn(String message, Throwable throwable) {
        innerLogger.log(Level.WARNING, message, throwable);
    }

    public static void warn(Throwable throwable) {
        innerLogger.log(Level.WARNING, throwable.getMessage(), throwable);
    }

    public static void log(String message) {
        innerLogger.log(Level.INFO, message);
    }
}
