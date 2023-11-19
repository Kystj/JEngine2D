/*
 Title: Shortcuts
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.managers;

import engine.inputs.KeyInputs;
import engine.widgets.HelpMenu;
import engine.widgets.PreferencesMenu;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class contains all the keyboard shortcut options
 */
public class ShortcutHandler {

    private final HelpMenu helpMenu = new HelpMenu();
    private final PreferencesMenu preferencesMenu = new PreferencesMenu();

    /**
     * Check for input and call shortcut functionality This method is called in the
     * Renderer class's tick() method currently
     */
    public void tick() {
        projectShortcuts();
        helpShortcut();
        debugShortcuts();
    }

    /**
     * Recognize user input for shortcuts available under the Project drop down menu.
     */
    public void projectShortcuts() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_S)) saveShortcut();
            if (KeyInputs.keyPressed(GLFW_KEY_L)) loadShortcut();
        }
        preferencesShortcut();
    }

    /**
     * The shortcut function to save the current project
     */
    private void saveShortcut() {
        //TODO: Implement save method and call here
        System.out.println("Saving.....");
    }

    /**
     * The shortcut function to load the current project
     */
    private void loadShortcut() {
        //TODO: Implement load method and call here
        System.out.println("Loading.....");
    }

    /**
     * The shortcut function to load the preferences for the Editor and current project
     */
    private void preferencesShortcut() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_P)) preferencesMenu.setOpen(true);
        }
        preferencesMenu.tick();
    }

    /**
     * The shortcut function to load the Help options
     */
    private void helpShortcut() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_H)) helpMenu.setOpen(true);
        }
        helpMenu.tick();
    }

    /**
     * The shortcut function for the Debug options
     */
    private static void debugShortcuts() {
        // TODO: Implement debug shortcuts
    }
}
/*End of Shortcuts class*/