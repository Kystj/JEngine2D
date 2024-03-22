package engine.ui.debug;

import engine.io.MouseInputs;
import engine.utils.ImGuiUtils;
import engine.utils.ReportHandler;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import static engine.settings.EConstants.X_SPACING;

public class ReportWindow {

    private boolean showPopup = false;
    private ImBoolean generateNewReport = new ImBoolean(false);

    private final ImString imGuiBugName = new ImString();
    private final ImString imGuiBugDescription = new ImString();

    public ReportWindow() {
        ReportHandler.loadReports();
        imGuiBugDescription.resize(512);
    }

    public void imgui() {
        generateReport();

    }

    private void generateReport() {
        if (generateNewReport.get()) {
            ImGui.begin("Bug Report", generateNewReport);
            generateFields();
            saveAndResetFields();
            ImGui.end();
        }
    }

    private void generateFields() {
        ImGui.setCursorPosX(X_SPACING);
        ImGui.text("ID:   ");
        ImGui.setCursorPosX(X_SPACING);

        // Input text field for bug name
        ImGui.inputText("##name", imGuiBugName);

        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.text("Description:");
        ImGui.setCursorPosX(X_SPACING);

        // Multiline input text field for bug description
        if (imGuiBugDescription.getBufferSize() < 512) {
            imGuiBugDescription.resize(512);
        }
        ImGui.inputTextMultiline("##description", imGuiBugDescription, ImGuiInputTextFlags.CallbackResize);

        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("Generate Report")) {
            String bugName = imGuiBugName.get();
            String bugDescription = imGuiBugDescription.get();

            if (!bugName.isEmpty() && !bugDescription.isEmpty()) {
                ReportHandler.saveReport(bugName, bugDescription);
                this.showPopup = true;
                generateNewReport.set(false);
            } else {
                // Handle empty fields
                ImGuiUtils.activatePopup("Error: Please fill in all fields!");
            }
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

    public void setGenerateNewReport(ImBoolean generateNewReport) {
        this.generateNewReport = generateNewReport;
    }

}
