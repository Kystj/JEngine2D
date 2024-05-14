package engine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debugging.info.ErrorForm;
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

import static engine.utils.EConstants.*;
import static engine.utils.EConstants.RED_BUTTON;

public class ReportUtils {

    private static final List<ErrorForm> ERROR_FORMS = new ArrayList<>();
    private static final String BUG_DIRECTORY_PATH = "bugs/";
    private static final ErrorForm selectedReport = new ErrorForm("Error", "No file found");

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
            ResourceUtils.deleteFile(fileName);
            // Remove the selected report
            ERROR_FORMS.remove(selectedReport);

            // Refresh the report list
            ERROR_FORMS.clear();
            loadReports();

            // Reset the selected report
            selectedReport.setBugID("Error");
            selectedReport.setBugDescription("No file found");

            showReport = false;
        }
        ImGui.popStyleColor();
    }

    public static void saveReport(String bugName, String bugDescription) {
        ErrorForm newErrorForm = createReport(bugName, bugDescription);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ErrorForm.class, new ReportAdapter())
                .create();

        String fileName = bugName.replace(" ", "") + ".json";
        String pathName = BUG_DIRECTORY_PATH + fileName;

        try (FileWriter writer = new FileWriter(pathName)) {
            writer.write(gson.toJson(newErrorForm));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private static ErrorForm createReport(String bugName, String bugDescription) {
        ErrorForm newErrorForm = new ErrorForm(bugName, bugDescription);
        ERROR_FORMS.add(newErrorForm); // Add the new report to the list
        return newErrorForm;
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
                .registerTypeAdapter(ErrorForm.class, new ReportAdapter())
                .create();
        String data = new String(Files.readAllBytes(filePath));
        ErrorForm errorForm = gson.fromJson(data, ErrorForm.class);
        ERROR_FORMS.add(errorForm); // Add the loaded report to the list
    }

    public static void displayReportList() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {
            for (ErrorForm report : ERROR_FORMS) {
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

    public static List<ErrorForm> getReports() {
        return ERROR_FORMS;
    }

    public static boolean isShowReport() {
        return showReport;
    }
}