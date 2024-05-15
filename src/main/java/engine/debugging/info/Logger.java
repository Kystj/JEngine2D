/*
 Title: Logger
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debugging.info;

import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector3f;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static engine.utils.EConstants.*;

/**
 * Provides logging functionality for debugging and information tracking.
 */
public class Logger {

    // Variables

    private static final List<LogEntry> LOG_ENTRIES = new ArrayList<>();
    private static final int MAX_LOGS = 100; // Change this value according to your needs
    private static final ImBoolean isOpen = new ImBoolean(true);

    // Public methods

    /**
     * Renders the logging user interface.
     */
    public static void imgui() {
        if (isOpen.get()) {
            ImGui.begin("Log", isOpen);

            // Iterate through the logs in reverse order and display them
            for (int i = LOG_ENTRIES.size() - 1; i >= 0; i--) {
                LogEntry entry = LOG_ENTRIES.get(i);
                float r = entry.getColor().x;
                float g = entry.getColor().y;
                float b = entry.getColor().z;

                ImGui.spacing();
                ImGui.setCursorPosX(X_SPACING);

                ImGui.textColored(r, g, b, 1, entry.getLog());
            }
            ImGui.end();
        }
    }

    /**
     * Logs an informational message with a specified color and optional timestamp.
     *
     * @param message  The message to log.
     * @param logTime  Flag indicating whether to log the timestamp.
     */
    public static void info(String message, boolean... logTime) {
        log(message, new Vector3f(0.0f, 1.0f, 0.0f), logTime);
        info(message);
    }

    /**
     * Logs a warning message with a specified color and optional timestamp.
     *
     * @param message  The message to log.
     * @param logTime  Flag indicating whether to log the timestamp.
     */
    public static void warning(String message, boolean... logTime) {
        log(message, new Vector3f(1.0f, 1.0f, 0.0f), logTime);
        warning(message);
    }

    /**
     * Logs an error message with a specified color and optional timestamp.
     *
     * @param message  The message to log.
     * @param logTime  Flag indicating whether to log the timestamp.
     */
    public static void error(String message, boolean... logTime) {
        log(message, new Vector3f(1.0f, 0.0f, 0.0f), logTime);
        error(message);
    }

    /**
     * Sets the flag indicating whether the log window is open.
     *
     * @param isOpen  Flag indicating whether the log window is open.
     */
    public static void setIsOpen(boolean isOpen) {
        Logger.isOpen.set(isOpen);
    }

    // Private methods

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     */
    private static void info(String message) {
        System.out.println(GREEN + message + RESET);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log.
     */
    private static void warning(String message) {
        System.out.println(YELLOW + message + RESET);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log.
     */
    private static void error(String message) {
        System.out.println(RED + message + RESET);
    }

    /**
     * Logs a message with a specified color and optional timestamp.
     *
     * @param message   The message to log.
     * @param color     The color of the log entry.
     * @param logTime   Flag indicating whether to log the timestamp.
     */
    private static void log(String message, Vector3f color, boolean... logTime) {
        boolean shouldLogTime = logTime.length > 0 && logTime[0];
        if (shouldLogTime) {
            message = addTime(message);
        }
        LogEntry entry = new LogEntry(message, color);
        addLog(entry);
    }

    /**
     * Adds a log entry to the list.
     *
     * @param log   The log entry to add.
     */
    private static void addLog(LogEntry log) {
        LOG_ENTRIES.add(log);
        if (LOG_ENTRIES.size() > MAX_LOGS) {
            LOG_ENTRIES.remove(0);
        }
    }

    /**
     * Adds a timestamp to the message.
     *
     * @param message   The message to add the timestamp to.
     * @return          The message with the timestamp.
     */
    private static String addTime(String message) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return "[" + formattedTime + "] " + message;
    }

    // Inner class

    /**
     * Represents a log entry with a message and color.
     */
    private static class LogEntry {

        // Fields

        private final String log;
        private final Vector3f color;

        // Constructors

        /**
         * Constructs a LogEntry with a message and color.
         *
         * @param log    The log message.
         * @param color  The color of the log entry.
         */
        private LogEntry(String log, Vector3f color) {
            this.log = log;
            this.color = color;
        }

        // Private methods

        /**
         * Gets the log message.
         *
         * @return The log message.
         */
        private String getLog() {
            return log;
        }

        /**
         * Gets the color of the log entry.
         *
         * @return The color of the log entry.
         */
        private Vector3f getColor() {
            return color;
        }
    }
}/* End of Logger class */