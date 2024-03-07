/*
 Title: JMenuBar
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.ui.widgets;

import engine.ui.debug.DebugWindow;
import imgui.ImGui;
import imgui.type.ImBoolean;

/**
 * Main menu bar for the engine
 */
public class MainMenuBar {

    private final HelpPanel helpPanelMenu = new HelpPanel();
    private final Preferences preferences = new Preferences();

    /**
     * The menu bars update method
     */
    public void imgui() {
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
        checkWidgetStatus();
    }

    /**
     * Check the status of the various widgets or windows that may be open
     */
    private void checkWidgetStatus() {
        updatePreferenceWindow();
        updateHelpWindow();
    }

    /**
     * Opens the Preferences window if the preferencesMenu.isOpen() is true
     */
    private void updatePreferenceWindow() {
        if (preferences.isOpen().get()) {
            preferences.tick();
        }
    }

    /**
     * Opens the Help window if helpMenu.isOpen() is true
     */
    private void updateHelpWindow() {
        if (helpPanelMenu.isOpen().get()) {
            helpPanelMenu.tick();
        }
    }

    /**
     * Selectable preferences option in the Project menu items drop window
     */
    private void preferencesMenuItem() {
        if (ImGui.menuItem("Preferences", "Ctrl+p")) preferences.setOpen(new ImBoolean(true));
        // TODO: Implement preferences
        System.out.println("Preferences...");
    }

    /**
     * Selectable save option in the Project menu items drop window
     */
    private void saveMenuItem() {
        if (ImGui.menuItem("Save", "Ctrl+s")) {
            // TODO: Implement save
            System.out.println("Saving...");
        }
    }

    /**
     * Selectable load option in the Project menu items drop window
     */
    private void loadMenuItem() {
        if (ImGui.menuItem("Load", "Ctrl+o")) {
            //TODO: Implement load and shortcuts
            System.out.println("Loading...");
        }
    }

    private void helpMenuItem() {
        if (ImGui.menuItem("Shortcuts", "Ctrl+t"));
        if (ImGui.menuItem("Help", "Ctrl+h")) helpPanelMenu.setOpen(new ImBoolean(true));
        //TODO: Implement load and shortcuts
        System.out.println("Loading...");
    }


    /**
     * The debug menu. The drop-down menu contains various debug functions
     */
    private void debugMenu() {
        //TODO: Implement debug window and shortcuts
        if (ImGui.beginMenu("Tools")) {

            if (ImGui.menuItem("Debug Console", "Ctrl+d")) {
                DebugWindow.setIsOpen(true);

            }

            if (ImGui.menuItem("Task Manager", "Ctrl+m")) {

            }
            ImGui.endMenu();
        }
    }

    /**
     * Help menu item drop window. Contain information about key shortcuts and editor controls
     */
    private void helpMenu() {
        if (ImGui.beginMenu("Help")) {
            helpMenuItem();
            System.out.println("Opening help menu...");
            ImGui.endMenu();
        }
    }
}
/*End of JMenuBar class*/
