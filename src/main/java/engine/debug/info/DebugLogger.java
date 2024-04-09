/*
 Title: Logger
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debug.info;

import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector3f;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static engine.utils.EConstants.X_SPACING;

public class DebugLogger {

    private static final List<LogEntry> Logs = new ArrayList<>();
    private static final int MAX_LOGS = 100; // Change this value according to your needs
    private static ImBoolean Is_Open = new ImBoolean(true);

    public static void imgui() {


        if (Is_Open.get()) {
            ImGui.begin("Log", Is_Open);


            // Iterate through the logs in reverse order and display them
            for (int i = Logs.size() - 1; i >= 0; i--) {
                LogEntry entry = Logs.get(i);
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

    public static void info(String message, boolean... logTime) {
        log(message, new Vector3f(0.0f, 1.0f, 0.0f), logTime);
    }

    public static void warning(String message, boolean... logTime) {
        log(message, new Vector3f(1.0f, 1.0f, 0.0f), logTime);
    }

    public static void error(String message, boolean... logTime) {
        log(message, new Vector3f(1.0f, 0.0f, 0.0f), logTime);
    }

    private static void log(String message, Vector3f color, boolean... logTime) {
        boolean shouldLogTime = logTime.length > 0 && logTime[0];
        if (shouldLogTime) {
            message = addTime(message);
        }
        LogEntry entry = new LogEntry(message, color);
        addLog(entry);
    }

    private static String addTime(String message) {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return "[" + formattedTime + "] " + message;
    }

    private static void addLog(LogEntry log) {
        Logs.add(log);
        if (Logs.size() > MAX_LOGS) {
            Logs.remove(0);
        }
    }

    public static void setIsOpen(boolean isOpen) {
        Is_Open.set(isOpen);
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