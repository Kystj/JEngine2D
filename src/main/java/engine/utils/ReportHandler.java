package engine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.diagnostic.DebugReport;
import engine.serialization.ReportAdapter;
import imgui.ImGui;
import imgui.flag.ImGuiCol;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static engine.settings.EConstants.X_SPACING;

public class ReportHandler {

    private static final List<DebugReport> bugReports = new ArrayList<>();
    private static final String BUG_DIRECTORY_PATH = "bugs/";
    private static final DebugReport selectedReport = new DebugReport("Error", "No file found");

    private static boolean showReport = false;

    public static void displayReport() {
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.text(selectedReport.getBugDescription());

        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 1.0f, 0.0f, 0.5f); // Yellow color
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("-")) {
            showReport = false;
        }
        ImGui.popStyleColor();

        ImGui.sameLine();
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 0.5f);
        if (ImGui.button("x")) {
            String fileName = BUG_DIRECTORY_PATH + selectedReport.getBugID();
            fileName = fileName.replace(" ", "") + ".json";
            ResourceHandler.deleteFile(fileName);
            // Remove the selected report
            bugReports.remove(selectedReport);

            // Refresh the report list
            bugReports.clear();
            loadReports();

            // Reset the selected report
            selectedReport.setBugID("Error");
            selectedReport.setBugDescription("No file found");

            showReport = false;
        }
        ImGui.popStyleColor();
    }

    public static void saveReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = createReport(bugName, bugDescription);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DebugReport.class, new ReportAdapter())
                .create();

        String fileName = bugName.replace(" ", "") + ".json";
        String pathName = BUG_DIRECTORY_PATH + fileName;

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newDebugReport));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private static DebugReport createReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = new DebugReport(bugName, bugDescription);
        bugReports.add(newDebugReport); // Add the new report to the list
        return newDebugReport;
    }

    public static void loadReports() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(BUG_DIRECTORY_PATH))) {
            for (Path filePath : directoryStream) {
                processReportFile(filePath);
            }
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private static void processReportFile(Path filePath) throws IOException {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DebugReport.class, new ReportAdapter())
                .create();
        String data = new String(Files.readAllBytes(filePath));
        DebugReport debugReport = gson.fromJson(data, DebugReport.class);
        bugReports.add(debugReport); // Add the loaded report to the list
    }

    public static void displayReportList() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {
            for (DebugReport report : bugReports) {
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

    private static void handleIOException(IOException e) {
        // Handle or log the exception
        e.printStackTrace();
    }

    public static List<DebugReport> getReports() {
        return bugReports;
    }

    public static boolean isShowReport() {
        return showReport;
    }
}