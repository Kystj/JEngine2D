/*
 Title: BugReportManager
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.DebugReport;
import engine.serialization.BugAdapter;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static engine.settings.EConstants.X_SPACING;

public class ReportManager {

    private static final Map<DebugReport, Boolean> bugReports = new HashMap<>();
    private static final String bugDirectoryPath = "bugs/";
    private static final DebugReport SELECTED_DEBUG_REPORT = new DebugReport("Error", "No file found");

    public static boolean displayReport(String ID, String description) {
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        SELECTED_DEBUG_REPORT.setBugID(ID);
        SELECTED_DEBUG_REPORT.setBugDescription(description);

        ImGui.setCursorPosX(X_SPACING);
        ImGui.text(SELECTED_DEBUG_REPORT.getBugDescription());
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        if (ImGui.button("close")) {
            return false;
        }

        ImGui.sameLine();
        if (ImGui.button("delete")) {
            String fileName = bugDirectoryPath + SELECTED_DEBUG_REPORT.getBugID() + ".json";
            ResourceManager.deleteFile(fileName);
            bugReports.entrySet().removeIf(entry -> entry.getKey().getBugID().equals(SELECTED_DEBUG_REPORT.getBugID()));
            return false;
        }
        return true;
    }

    public static void saveReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = createReport(bugName, bugDescription);
        // Save bug reports using GSON
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DebugReport.class, new BugAdapter())
                .create();

        String fileName = bugName.replace(" ", "");
        String pathName = "bugs/" + fileName + ".json"; // Construct the filename

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newDebugReport));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadReports() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(bugDirectoryPath))) {
            for (Path filePath : directoryStream) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(DebugReport.class, new BugAdapter())
                        .create();
                String data = "";
                try {
                    String path = filePath.toString();
                    data = new String(Files.readAllBytes(Paths.get(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DebugReport debugReport = gson.fromJson(data, DebugReport.class);
                Boolean resolved = debugReport.isResolved();
                bugReports.put(debugReport, resolved);
            }
        } catch (IOException e) {
            System.out.println("No report file found.");
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
}
/*End of BugReportManager class*/
