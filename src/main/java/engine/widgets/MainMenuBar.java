/*
 Title: JMenuBar
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.widgets;

import engine.debug.BugReportUI;
import imgui.ImGui;

/**
 * Main menu bar for the engine
 */
public class MainMenuBar {

    BugReportUI bugReportUI = new BugReportUI();

    /**
     * The menu bars update method
     */
    public void tick() {
        ImGui.beginMainMenuBar();
        projectMenuItem();
        debugMenu();
        helpMenu();
        ImGui.endMainMenuBar();
    }

    /**
     * The current projects' menu. Contains save, load and preference/setting functionality
     */
    private void projectMenuItem() {
        if (ImGui.beginMenu("Project")) {
            saveMenuItem();
            loadMenuItem();
            preferencesMenuItem();
            ImGui.endMenu();
        }
    }

    /**
     * Selectable preferences option in the Project menu items drop window
     */
    private void preferencesMenuItem() {
        if (ImGui.menuItem("Preferences", "Ctrl+P")) {
            //TODO: Implement preferences window and shortcuts
            System.out.println("Opening preferences...");
        }
    }

    /**
     * Selectable save option in the Project menu items drop window
     */
    private void saveMenuItem() {
        if (ImGui.menuItem("Save", "Ctrl+S")) {
            //TODO: Implement save and shortcuts
            System.out.println("Saving....");
        }
    }

    /**
     * Selectable load option in the Project menu items drop window
     */
    private void loadMenuItem() {
        if (ImGui.menuItem("Load", "Ctrl+O")) {
            //TODO: Implement load and shortcuts
            System.out.println("Loading...");
        }
    }



    /**
     * The debug menu. The drop-down menu contains various debug functions
     */
    private void debugMenu() {
        //TODO: Implement debug window and shortcuts
        if (ImGui.beginMenu("Debug")) {
            if (ImGui.menuItem("New Report")) {
                bugReportUI.setGenerateNewReport(true);
            }

            if (ImGui.menuItem("Open Report")) {
                bugReportUI.setLoadReport(true);
            }
            ImGui.endMenu();
        }
        bugReportUI.tick();
    }

    /**
     * Help menu item drop window. Contain information about key shortcuts and editor controls
     */
    private void helpMenu() {
        if (ImGui.beginMenu("Help")) {
            //TODO: Implement help window and shortcuts
            System.out.println("Opening help menu...");
            ImGui.endMenu();
        }
    }
}
/*End of JMenuBar class*/
