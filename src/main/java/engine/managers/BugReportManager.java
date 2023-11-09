/*
 Title: BugReportManager
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debug.BugReport;
import engine.typeadapters.BugTypeAdapter;
import imgui.ImGui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class BugReportManager {

    private static Map<BugReport, Boolean> bugReports = new HashMap<>();
    private static final String bugDirectoryPath = "bugs/";
    private static final BugReport selectedReport = new BugReport("Error", "No file found");

    public static void displayBugReports() {
            boolean showSelectedReport = false;

            for (Map.Entry<BugReport, Boolean> entry : bugReports.entrySet()) {
                if (ImGui.button(entry.getKey().getBugID())) {
                    selectedReport.setBugID(entry.getKey().getBugID());
                    selectedReport.setBugDescription(entry.getKey().getBugDescription());
                    showSelectedReport = true;
                }

                ImGui.sameLine();

                if (ImGui.checkbox("##" + entry.getKey(), entry.getValue())) {
                    entry.setValue(!entry.getValue()); // Toggle the checkbox value
                }
            }
            if (showSelectedReport) {
                ImGui.begin("Details");
                ImGui.text(selectedReport.getBugDescription());
                ImGui.spacing();
                if (ImGui.button("close")) {
                    showSelectedReport = false;
                }
                ImGui.sameLine();
                if (ImGui.button("delete")) {
                    String fileName = bugDirectoryPath + selectedReport.getBugID() + ".json";
                    deleteBugReport(fileName);
                    System.out.println(fileName);
                    bugReports.entrySet().removeIf(entry -> entry.getKey().getBugID().equals(selectedReport.getBugID()));
                    showSelectedReport = false;
                }
                ImGui.end();
            }
    }

    public static void saveBugReport(String bugName, String bugDescription) {
        BugReport newReport = createNewReport(bugName, bugDescription);
        // Save bug reports using GSON
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(BugReport.class, new BugTypeAdapter())
                .create();

        String fileName = bugName.replace(" ", "_");
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
                        .registerTypeAdapter(BugReport.class, new BugTypeAdapter())
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

    public static void deleteBugReport(String path) {

        File fileToDelete = new File(path);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println("File deleted successfully.");
            } else {
                System.out.println("Failed to delete the file.");
            }
        } else {
            System.out.println("File does not exist.");
        }
    }
}
/*End of BugReportManager class*/
