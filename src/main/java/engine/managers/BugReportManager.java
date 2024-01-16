/*
 Title: BugReportManager
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.BugReport;
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

public class BugReportManager {

    private static final Map<BugReport, Boolean> bugReports = new HashMap<>();
    private static final String bugDirectoryPath = "bugs/";
    private static final BugReport selectedReport = new BugReport("Error", "No file found");

    public static boolean showSelectedReport = false;

    public static void displayBugReports() {

        for (Map.Entry<BugReport, Boolean> entry : bugReports.entrySet()) {
            ImGui.spacing();
            ImGui.setCursorPosX(X_SPACING);
            if (ImGui.button(entry.getKey().getBugID())) {
                selectedReport.setBugID(entry.getKey().getBugID());
                selectedReport.setBugDescription(entry.getKey().getBugDescription());
                showSelectedReport = true;
            }
        }
        if (showSelectedReport) {
            ImGui.setCursorPosX(X_SPACING);
            ImGui.text(selectedReport.getBugDescription());
            ImGui.spacing();
            ImGui.setCursorPosX(X_SPACING);
            if (ImGui.button("close")) {
                showSelectedReport = false;
            }
            ImGui.sameLine();
            if (ImGui.button("delete")) {
                String fileName = bugDirectoryPath + selectedReport.getBugID() + ".json";
                System.out.println(fileName);
                ResourceManager.deleteFile(fileName);
                bugReports.entrySet().removeIf(entry -> entry.getKey().getBugID().equals(selectedReport.getBugID()));
                showSelectedReport = false;
            }
        }
    }

    public static void saveBugReport(String bugName, String bugDescription) {
        BugReport newReport = createNewReport(bugName, bugDescription);
        // Save bug reports using GSON
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BugReport.class, new BugAdapter())
                .create();

        String fileName = bugName.replace(" ", "");
        String pathName = "bugs/" + fileName + ".json"; // Construct the filename

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newReport));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadBugReports() {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(bugDirectoryPath))) {
            for (Path filePath : directoryStream) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(BugReport.class, new BugAdapter())
                        .create();
                String data = "";
                try {
                    String path = filePath.toString();
                    data = new String(Files.readAllBytes(Paths.get(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BugReport report = gson.fromJson(data, BugReport.class);
                Boolean resolved = report.isResolved();
                bugReports.put(report, resolved);
            }
        } catch (IOException e) {
            System.out.println("No report file found.");
        }
    }

    private static BugReport createNewReport(String bugName, String bugDescription) {
        BugReport newReport = new BugReport(bugName, bugDescription);
        bugReports.put(newReport, false);
        return newReport;
    }
}
/*End of BugReportManager class*/
