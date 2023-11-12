/*
 Title: BugReportUI
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.debug;

import engine.UI.EngineWindow;
import engine.inputs.MouseInputs;
import engine.widgets.ImGuiCustom;
import engine.managers.BugReportManager;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;

import static engine.settings.EConstants.POPUP_WIN_SIZE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

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
            ImGui.begin("Debug");
            generateReportFields();
            resetFields();
            ImGui.end();
        }
    }

    private void generateReportFields() {
        // Input fields with meaningful labels
        ImGui.text("ID:   ");
        ImGui.inputText("##name", imGuiBugName);
        ImGui.spacing();
        ImGui.text("Description:");
        ImGui.inputTextMultiline("##description", imGuiBugDescription);

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
            loadReport = ImGuiCustom.closeButton();
            ImGui.end();
        }
    }

    private void resetFields() {
        if (this.showPopup) {
            runPopup();
            imGuiBugName.set("");
            imGuiBugDescription.set("");
        }
    }

    private void runPopup() {
        ImGui.openPopup("Saving");
        centrePopup();
        // AlwaysAutoResize ensures the window resizes itself based on its content
        if (ImGui.beginPopup("Saving", ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoScrollbar)) {
            centrePopupText();
        }
        closePopup();
        ImGui.endPopup();
    }

    private void centrePopupText() {
        ImVec2 textSize = ImGui.calcTextSize("Saved!");
        ImVec2 centerPos = new ImVec2(new ImVec2((POPUP_WIN_SIZE.x - textSize.x) * 0.5f,
                (POPUP_WIN_SIZE.y - textSize.y) * 0.5f));
        // Allows me to manually set where the next UI element will be drawn
        ImGui.setCursorPos(centerPos.x, centerPos.y);
        ImGui.text("Saved!");
    }

    private void centrePopup() {
        ImVec2 popupPos = new ImVec2((EngineWindow.get().getWindowWidth() * 0.5f),
                (EngineWindow.get().getWindowHeight() * 0.5f));
        ImGui.setNextWindowSize(POPUP_WIN_SIZE.x, POPUP_WIN_SIZE.y);
        ImGui.setNextWindowPos(popupPos.x, popupPos.y);
    }

    private void closePopup() {
        if (MouseInputs.getMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            ImGui.closeCurrentPopup();
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
