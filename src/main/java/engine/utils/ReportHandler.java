package engine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.diagnostic.BugReport;
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

import static engine.settings.EConstants.*;
import static engine.settings.EConstants.RED_BUTTON;

public class ReportHandler {

    private static final List<BugReport> bugReports = new ArrayList<>();
    private static final String BUG_DIRECTORY_PATH = "bugs/";
    private static final BugReport selectedReport = new BugReport("Error", "No file found");

    private static boolean showReport = false;

    public static void displayReport() {
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.textWrapped(selectedReport.getBugDescription());

        ImGui.pushStyleColor(ImGuiCol.Button, YELLOW_BUTTON.x, YELLOW_BUTTON.y, YELLOW_BUTTON.z, YELLOW_BUTTON.w); // Yellow color
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("-")) {
            showReport = false;
        }
        ImGui.popStyleColor();

        ImGui.sameLine();
        ImGui.pushStyleColor(ImGuiCol.Button, RED_BUTTON.x, RED_BUTTON.y, RED_BUTTON.z, RED_BUTTON.w);
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
        BugReport newBugReport = createReport(bugName, bugDescription);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BugReport.class, new ReportAdapter())
                .create();

        String fileName = bugName.replace(" ", "") + ".json";
        String pathName = BUG_DIRECTORY_PATH + fileName;

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newBugReport));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private static BugReport createReport(String bugName, String bugDescription) {
        BugReport newBugReport = new BugReport(bugName, bugDescription);
        bugReports.add(newBugReport); // Add the new report to the list
        return newBugReport;
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
                .registerTypeAdapter(BugReport.class, new ReportAdapter())
                .create();
        String data = new String(Files.readAllBytes(filePath));
        BugReport bugReport = gson.fromJson(data, BugReport.class);
        bugReports.add(bugReport); // Add the loaded report to the list
    }

    public static void displayReportList() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {
            for (BugReport report : bugReports) {
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

    public static List<BugReport> getReports() {
        return bugReports;
    }

    public static boolean isShowReport() {
        return showReport;
    }
}