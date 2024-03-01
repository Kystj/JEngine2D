package engine.ui.debug;

import engine.io.MouseInputs;
import engine.utils.ReportHandler;
import engine.ui.engine.ImGuiUtils;
import imgui.ImGui;
import imgui.type.ImString;

import static engine.ui.settings.EConstants.X_SPACING;

/**
 * BugReportUI class for handling bug reporting user interface.
 */
public class ReportPanel {

    private boolean showPopup = false;
    private boolean loadReport = false;
    private boolean generateNewReport = false;

    private final ImString imGuiBugName = new ImString();
    private final ImString imGuiBugDescription = new ImString();

    public ReportPanel() {
        ReportHandler.loadReports();
    }

    public void tick() {
        generateReport();
        loadReportInformation();
    }

    private void generateReport() {
        if (generateNewReport) {
            ImGui.begin("Bug Report");
            generateReportFields();
            saveAndResetFields();
            ImGui.end();
        }
    }

    private void generateReportFields() {
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
        if (ImGui.button("Generate Report")) {
            String bugName = imGuiBugName.get();
            String bugDescription = imGuiBugDescription.get();
            if (!bugName.isEmpty() && !bugDescription.isEmpty()) {
                ReportHandler.saveReport(bugName, bugDescription);
                this.showPopup = true;
            } else {
                // Handle empty fields
                ImGuiUtils.activatePopup("Error: Please fill in all fields!");
            }
        }
        generateNewReport = ImGuiUtils.closeButton();
    }

    private void loadReportInformation() {
        if (loadReport) {
            ImGui.begin("Reports");
            // Display existing bug reports
            ReportHandler.viewBugs();
            loadReport = ImGuiUtils.closeButton(ImGui.getWindowSizeX() * .50f, ImGui.getWindowSizeY() * .50f);
            ImGui.end();
        }
    }

    private void saveAndResetFields() {
        if (this.showPopup) {
            ImGuiUtils.activatePopup("Saving!");
            imGuiBugName.set("");
            imGuiBugDescription.set("");
        }
        if (MouseInputs.mouseButtonDown(0)) {
            showPopup = false;
        }
    }

    public void setGenerateNewReport(boolean generateNewReport) {
        this.generateNewReport = generateNewReport;
    }

    public void setLoadReport(boolean loadReport) {
        this.loadReport = loadReport;
    }
}
