/*
 Title: ErrorForm
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debugging.info;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.serialization.ReportAdapter;
import engine.utils.engine.ResourceUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static engine.utils.engine.EConstants.*;

/**
 * Manages error reports including saving, loading, and displaying them.
 */
public class ErrorManager {

    // Variables

    private static final List<ErrorForm> ERROR_FORMS = new ArrayList<>();
    private static final String BUG_DIRECTORY_PATH = "errors/";
    private static final ErrorForm SELECTED_REPORT = new ErrorForm("Error", "No file found");
    private static final Path BUG_DIRECTORY = Paths.get(BUG_DIRECTORY_PATH);
    private static boolean SHOW_REPORT = false;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ErrorForm.class, new ReportAdapter())
            .create();

    // Public methods

    /**
     * Displays the selected error report.
     */
    public static void displayReport() {
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.textWrapped(SELECTED_REPORT.getErrorDescription());

        ImGui.pushStyleColor(ImGuiCol.Button, YELLOW_BUTTON.x, YELLOW_BUTTON.y, YELLOW_BUTTON.z, YELLOW_BUTTON.w); // Yellow color
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("-")) {
            SHOW_REPORT = false;
        }
        ImGui.popStyleColor();

        ImGui.sameLine();
        ImGui.pushStyleColor(ImGuiCol.Button, RED_BUTTON.x, RED_BUTTON.y, RED_BUTTON.z, RED_BUTTON.w);
        if (ImGui.button("x")) {
            deleteSelectedReport();
        }
        ImGui.popStyleColor();
    }

    /**
     * Saves a new error report.
     *
     * @param bugName        The name of the bug.
     * @param bugDescription The description of the bug.
     */
    public static void saveReport(String bugName, String bugDescription) {
        ErrorForm newErrorForm = createReport(bugName, bugDescription);
        String fileName = bugName.replace(" ", "") + ".json";
        String pathName = BUG_DIRECTORY_PATH + fileName;

        try {
            Files.writeString(Paths.get(pathName), GSON.toJson(newErrorForm));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays a list of error reports.
     */
    public static void displayReportList() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {
            for (ErrorForm report : ERROR_FORMS) {
                if (ImGui.selectable(report.getErrorID())) {
                    SHOW_REPORT = true;
                    SELECTED_REPORT.setErrorDescription(report.getErrorDescription());
                    SELECTED_REPORT.setErrorID(report.getErrorID());
                }
                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip(report.getErrorDescription());
                    ImGui.endTooltip();
                }
            }
            ImGui.endCombo();
        }
        if (SHOW_REPORT) {
            displayReport();
        }
    }

    /**
     * Loads error reports from files.
     */
    public static void loadReports() {
        ERROR_FORMS.clear();
        try {
            Files.list(BUG_DIRECTORY)
                    .filter(Files::isRegularFile)
                    .forEach(ErrorManager::processReportFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Private methods

    /**
     * Creates a new error report.
     *
     * @param bugName        The name of the bug.
     * @param bugDescription The description of the bug.
     * @return The created ErrorForm object.
     */
    private static ErrorForm createReport(String bugName, String bugDescription) {
        ErrorForm newErrorForm = new ErrorForm(bugName, bugDescription);
        ERROR_FORMS.add(newErrorForm);
        return newErrorForm;
    }

    /**
     * Processes a single error report file.
     *
     * @param filePath The path of the error report file.
     */
    private static void processReportFile(Path filePath) {
        try {
            ErrorForm errorForm = GSON.fromJson(Files.readString(filePath), ErrorForm.class);
            ERROR_FORMS.add(errorForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected error report.
     */
    private static void deleteSelectedReport() {
        String fileName = BUG_DIRECTORY_PATH + SELECTED_REPORT.getErrorID().replace(" ", "") + ".json";
        ResourceUtils.deleteFile(fileName);
        ERROR_FORMS.remove(SELECTED_REPORT);
        loadReports();
        SHOW_REPORT = false;
    }
}/* End of ErrorManager class */
