/*
 Title: BugReportManager
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.handlers;

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

import static engine.UI.settings.EConstants.X_SPACING;

/**
 * The ReportManager class manages bug reports, including saving, loading, and displaying them.
 */
public class ReportHandler {

    private static final Map<DebugReport, Boolean> bugReports = new HashMap<>();
    private static final String bugDirectoryPath = "bugs/";
    private static final DebugReport SELECTED_DEBUG_REPORT = new DebugReport("Error", "No file found");

    /**
     * Displays the bug report in the ImGui window.
     *
     * @param ID          The bug ID.
     * @param description The bug description.
     * @return true if the report should remain displayed, false otherwise.
     */
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
            ResourceHandler.deleteFile(fileName);
            bugReports.entrySet().removeIf(entry -> entry.getKey().getBugID().equals(SELECTED_DEBUG_REPORT.getBugID()));
            return false;
        }
        return true;
    }

    /**
     * Saves a bug report.
     *
     * @param bugName        The name of the bug.
     * @param bugDescription The description of the bug.
     */
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

    /**
     * Loads bug reports from the bug directory.
     */
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

    /**
     * Displays a combo box containing debug reports, allowing the user to view details and perform actions on each report.
     * This method uses ImGui for creating the user interface.
     */
    public static void viewBugs() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {

            // Iterate over existing DebugReports
            for (Map.Entry<DebugReport, Boolean> entry : ReportHandler.getReports().entrySet()) {
                DebugReport key = entry.getKey();

                // Display selectable options for each DebugReport
                String bugID = key.getBugID();
                String bugDescription = key.getBugDescription();

                if (ImGui.selectable(bugID)) {
                    ImGui.begin(bugID);
                    boolean shouldDisplay = ReportHandler.displayReport(bugID, bugDescription);

                    // If displayReport returns false, end the window
                    if (!shouldDisplay) {
                        ImGui.end();
                    }
                }

                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip(bugDescription);
                    ImGui.endTooltip();
                }
            }
            ImGui.endCombo();
        }
    }

    /**
     * Creates a new bug report.
     *
     * @param bugName        The name of the bug.
     * @param bugDescription The description of the bug.
     * @return The newly created DebugReport.
     */
    private static DebugReport createReport(String bugName, String bugDescription) {
        DebugReport newDebugReport = new DebugReport(bugName, bugDescription);
        bugReports.put(newDebugReport, false);
        return newDebugReport;
    }

    /**
     * Gets the map of bug reports.
     *
     * @return The map of bug reports.
     */
    public static Map<DebugReport, Boolean> getReports() {
        return bugReports;
    }
}
/*End of ReportManager class*/
