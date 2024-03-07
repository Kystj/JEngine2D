/*
 Title: Logger
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debug.logger;

import imgui.ImGui;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static engine.settings.EConstants.X_SPACING;

public class Logger {
    private static final List<LogEntry> logs = new ArrayList<>();
    private static final int MAX_LOGS = 100; // Change this value according to your needs

    private static void addLog(LogEntry log) {
        logs.add(log);
        if (logs.size() > MAX_LOGS) {
            logs.remove(0);
        }
    }

    public static void imgui() {
        ImGui.begin("Log");

        // Iterate through the logs and display them
        List<LogEntry> currentLogs = new ArrayList<>(logs); // Create a copy of logs to prevent modification during iteration
        for (LogEntry entry : currentLogs) {
            float r = entry.getColor().x;
            float g = entry.getColor().y;
            float b = entry.getColor().z;

            ImGui.spacing();
            ImGui.setCursorPosX(X_SPACING);

            ImGui.textColored(r,g,b,1, entry.getLog());
            // ImGui.text(entry.getLog());
        }

        ImGui.end();
    }

    public static void info(String message) {
        LogEntry entry = new LogEntry(message, new Vector3f(0.0f, 1.0f, 0.0f));
        addLog(entry);
    }

    public static void warning(String message) {
        LogEntry entry = new LogEntry(message, new Vector3f(1.0f, 1.0f, 0.0f));
        addLog(entry);
    }

    public static void error(String message) {
        LogEntry entry = new LogEntry(message, new Vector3f(1.0f, 0.0f, 0.0f));
        addLog(entry);
    }

    private static class LogEntry {
        private final String log;
        private final Vector3f color;

        private LogEntry(String log, Vector3f color) {
            this.log = log;
            this.color = color;
        }

        private String getLog() {
            return log;
        }

        private Vector3f getColor() {
            return color;
        }
    }
}