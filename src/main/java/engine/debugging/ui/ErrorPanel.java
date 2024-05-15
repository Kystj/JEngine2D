/*
 Title: ErrorForm
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debugging.ui;

import engine.debugging.info.Logger;
import engine.debugging.info.ErrorManager;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import static engine.utils.EConstants.X_SPACING;

/**
 * Represents a panel for managing error reports and generating new bug reports.
 */
public class ErrorPanel {

    // Variables

    private boolean shouldRefresh = false;
    private ImBoolean generateNewReport = new ImBoolean(false);
    private final ImString errorID = new ImString();
    private final ImString errorDescription = new ImString();

    // Constructor

    /**
     * Constructs an ErrorPanel and loads existing error reports.
     */
    public ErrorPanel() {
        ErrorManager.loadReports();
        errorDescription.resize(512);
    }

    // Public methods

    /**
     * Renders the error panel user interface.
     */
    public void imGui() {
        renderErrorUI();
    }

    /**
     * Sets the state of generating a new bug report.
     *
     * @param generateNewReport The state of generating a new bug report.
     */
    public void setGenerateNewReport(ImBoolean generateNewReport) {
        this.generateNewReport = generateNewReport;
    }

    // Private methods

    /**
     * Renders the error panel UI elements.
     */
    private void renderErrorUI() {
        if (generateNewReport.get()) {
            ImGui.begin("Error", generateNewReport);
            generateFields();
            ImGui.end();
        }
        if (shouldRefresh) {
            resetFields();
        }
        ImGui.spacing();
    }

    /**
     * Generates UI fields for entering bug report details.
     */
    private void generateFields() {
        ImGui.setCursorPosX(X_SPACING);
        ImGui.text("ID:   ");
        ImGui.setCursorPosX(X_SPACING);

        // Input text field for bug name
        ImGui.inputText("##name", errorID);

        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);

        ImGui.text("Description:");
        ImGui.setCursorPosX(X_SPACING);

        // Multiline input text field for bug description
        if (errorDescription.getBufferSize() < 512) {
            errorDescription.resize(512);
        }
        ImGui.inputTextMultiline("##description", errorDescription, ImGuiInputTextFlags.CallbackResize);

        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.button("Generate Report")) {
            String bugName = errorID.get();
            String bugDescription = errorDescription.get();

            if (!bugName.isEmpty() && !bugDescription.isEmpty()) {
                ErrorManager.saveReport(bugName, bugDescription);
                Logger.info("Successfully saved error report: " + errorID);
                this.shouldRefresh = true;
                generateNewReport.set(false);
            } else {
                // Handle empty fields
                Logger.error("Error: Please fill in all fields!");
            }
        }
    }

    /**
     * Resets the error panel fields.
     */
    private void resetFields() {
        errorID.set("");
        errorDescription.set("");
    }
}/* End of ErrorPanel class */