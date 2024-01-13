/*
 Title: BugReportUI
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.debug;

import engine.inputs.MouseInputs;
import engine.managers.BugReportManager;
import engine.widgets.ImGuiCustom;
import imgui.ImGui;
import imgui.type.ImString;

import static engine.settings.EConstants.X_SPACING;

public class BugReportUI {

    private boolean showPopup = false;

    private boolean loadReport = false;

    private boolean generateNewReport = false;

    ImString imGuiBugName = new ImString();
    ImString imGuiBugDescription = new ImString();

    public BugReportUI() {
        BugReportManager.loadBugReports();
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
        // Input fields with meaningful labels
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
            this.showPopup = true;
            String bugName = imGuiBugName.get();
            String bugDescription = imGuiBugDescription.get();
            BugReportManager.saveBugReport(bugName, bugDescription);
        }
        generateNewReport = ImGuiCustom.closeButton();
    }

    private void loadReportInformation() {
        if (loadReport) {
            ImGui.begin("Reports");
            BugReportManager.displayBugReports();
            loadReport = ImGuiCustom.closeButton(ImGui.getWindowSizeX() * .45f,ImGui.getWindowSizeY() * .85f);
            ImGui.end();
        }
    }

    private void saveAndResetFields() {
        if (this.showPopup) {
            ImGuiCustom.activatePopup("Saving!");
            imGuiBugName.set("");
            imGuiBugDescription.set("");
        }
       if (MouseInputs.getMouseButtonPressed(0)) {
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
/*End of BugReportUI class*/
