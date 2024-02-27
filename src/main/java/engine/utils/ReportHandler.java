package engine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.DebugReport;
import engine.serialization.BugAdapter;
import imgui.ImGui;
import imgui.flag.ImGuiCol;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static engine.ui.settings.EConstants.X_SPACING;

public class ReportHandler {

    private static final Map<DebugReport, Boolean> bugReports = new HashMap<>();
    private static final String BUG_DIRECTORY_PATH = "bugs/";
    private static final DebugReport selectedReport = new DebugReport("Error", "No file found");

    private static boolean showReport = false;

    public static void displayReport() {
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.text(selectedReport.getBugDescription());

        // Set the color of the button background
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 1.0f, 0.0f, 0.5f); // Yellow color
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("-")) {
            showReport = false;
        }
        ImGui.popStyleColor();

        ImGui.sameLine();
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 0.5f);
        if (ImGui.button("x")) {
            String fileName = BUG_DIRECTORY_PATH + selectedReport.getBugID() + ".json";
            ResourceHandler.deleteFile(fileName);
            bugReports.remove(selectedReport);
            showReport = false;
        }
        ImGui.popStyleColor();
    }

    public static void saveReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = createReport(bugName, bugDescription);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DebugReport.class, new BugAdapter())
                .create();

        String fileName = bugName.replace(" ", "") + ".json";
        String pathName = BUG_DIRECTORY_PATH + fileName;

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newDebugReport));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadReports() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(BUG_DIRECTORY_PATH))) {
            for (Path filePath : directoryStream) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(DebugReport.class, new BugAdapter())
                        .create();
                String data;
                try {
                    data = new String(Files.readAllBytes(filePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                DebugReport debugReport = gson.fromJson(data, DebugReport.class);
                Boolean resolved = debugReport.isResolved();
                bugReports.put(debugReport, resolved);
            }
        } catch (IOException e) {
            System.out.println("No report file found.");
        }
    }

    public static void viewBugs() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {
            for (Map.Entry<DebugReport, Boolean> entry : bugReports.entrySet()) {
                DebugReport report = entry.getKey();
                if (ImGui.selectable(report.getBugID())) {
                    showReport = true;
                    selectedReport.setBugDescription(report.getBugDescription());
                    selectedReport.setBugID(report.getBugID());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip(report.getBugDescription());
                    ImGui.endTooltip();
                }
            }
            ImGui.endCombo();
        }
    }

    private static DebugReport createReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = new DebugReport(bugName, bugDescription);
        bugReports.put(newDebugReport, false);
        return newDebugReport;
    }

    public static Map<DebugReport, Boolean> getReports() {
        return bugReports;
    }

    public static boolean isShowReport() {
        return showReport;
    }
}