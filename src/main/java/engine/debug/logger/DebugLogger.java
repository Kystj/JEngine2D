/*
 Title: Logger
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debug.logger;

import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static engine.settings.EConstants.X_SPACING;

public class DebugLogger {

    private static final List<LogEntry> logs = new ArrayList<>();
    private static final int MAX_LOGS = 100; // Change this value according to your needs
    private static ImBoolean bIsOpen = new ImBoolean(true);

    public static void imgui() {
        if (bIsOpen.get()) {
            ImGui.begin("Log", bIsOpen);

            // Iterate through the logs in reverse order and display them
            for (int i = logs.size() - 1; i >= 0; i--) {
                LogEntry entry = logs.get(i);
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

    private static void addLog(LogEntry log) {
        logs.add(log);
        if (logs.size() > MAX_LOGS) {
            logs.remove(0);
        }
    }

    public static void setIsOpen(ImBoolean isOpen) {
        DebugLogger.bIsOpen = isOpen;
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