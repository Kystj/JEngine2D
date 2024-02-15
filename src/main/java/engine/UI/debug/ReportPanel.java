/*
 * Title: BugReportUI
 * Date: 2023-11-09
 * Author: Kyle St John
 */
package engine.UI.debug;

import engine.io.MouseInputs;
import engine.handlers.ReportHandler;
import engine.UI.engine.ImGuiCustom;
import imgui.ImGui;
import imgui.type.ImString;

import static engine.UI.settings.EConstants.X_SPACING;

/** BugReportUI class for handling bug reporting user interface. */
public class ReportPanel {

    // Flags to control the visibility of UI elements
    private boolean showPopup = false;
    private boolean loadReport = false;
    private boolean generateNewReport = false;

    // Input fields for bug report details
    ImString imGuiBugName = new ImString();
    ImString imGuiBugDescription = new ImString();

    // Constructor that loads existing bug reports
    public ReportPanel() {
        ReportHandler.loadReports();
    }

    // Main tick method for handling UI updates
    public void tick() {
        generateReport();
        loadReportInformation();
    }

    /**
     * Method to generate a new bug report.
     */
    private void generateReport() {
        if (generateNewReport) {
            ImGui.begin("Bug Report");
            generateReportFields();
            saveAndResetFields();
            ImGui.end();
        }
    }

    /**
     * Method to generate input fields for bug report details.
     */
    private void generateReportFields() {
        // Input fields
        ImGui.setCursorPosX(X_SPACING);
        ImGui.text("ID:   ");
        ImGui.setCursorPosX(X_SPACING);

        ImGui.inputText("##name", imGuiBugName);
        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.text("Description:");
        ImGui.setCursorPosX(X_SPACING);
        ImGui.inputTextMultiline("##description", imGuiBugDescription, 256);

        ImGui.setCursorPosX(X_SPACING);
        // Button to generate a new bug report
        if (ImGui.button("Generate Report")) {
            this.showPopup = true;
            String bugName = imGuiBugName.get();
            String bugDescription = imGuiBugDescription.get();
            ReportHandler.saveReport(bugName, bugDescription);
        }
        // Close button to cancel bug report generation
        generateNewReport = ImGuiCustom.closeButton();
    }

    /**
     * Method to load and display existing bug reports.
     */
    private void loadReportInformation() {
        if (loadReport) {
            ImGui.begin("Reports");
            //BugReportManager.displayBugReports();
            // Close button to hide the bug report information
            loadReport = ImGuiCustom.closeButton(ImGui.getWindowSizeX() * .45f, ImGui.getWindowSizeY() * .85f);
            ImGui.end();
        }
    }

    /**
     * Method to save bug report details and reset input fields.
     */
    private void saveAndResetFields() {
        if (this.showPopup) {
            ImGuiCustom.activatePopup("Saving!");
            imGuiBugName.set("");
            imGuiBugDescription.set("");
        }
        // Check for mouse click to close the saving popup
        if (MouseInputs.getMouseButtonPressed(0)) {
            showPopup = false;
        }
    }

    /**
     * Setter for the generateNewReport flag.
     *
     * @param generateNewReport The new value for the flag.
     */
    public void setGenerateNewReport(boolean generateNewReport) {
        this.generateNewReport = generateNewReport;
    }

    /**
     * Setter for the loadReport flag.
     *
     * @param loadReport The new value for the flag.
     */
    public void setLoadReport(boolean loadReport) {
        this.loadReport = loadReport;
    }
}
/* End of BugReportUI class */