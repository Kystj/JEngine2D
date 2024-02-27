/*
 Title: Shortcuts
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.utils;

import engine.io.KeyInputs;
import engine.ui.widgets.HelpPanel;
import engine.ui.widgets.Preferences;
import imgui.type.ImBoolean;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class contains all the keyboard shortcut options
 */
public class ShortcutHandler {

    private static final HelpPanel HELP_PANEL_MENU = new HelpPanel();
    private static final Preferences PREFERENCES = new Preferences();

    /**
     * Check for input and call shortcut functionality This method is called in the
     * Renderer class's tick() method currently
     */
    public static void tick() {
        projectShortcuts();
        helpShortcut();
        debugShortcuts();
    }

    public static boolean closeWithEscape() {
        return !KeyInputs.keyPressed(GLFW_KEY_ESCAPE);
    }

    /**
     * Recognize user input for shortcuts available under the Project drop down menu.
     */
    public static void projectShortcuts() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_S)) saveShortcut();
            if (KeyInputs.keyPressed(GLFW_KEY_L)) loadShortcut();
        }
        preferencesShortcut();
    }

    /**
     * The shortcut function to save the current project
     */
    private static void saveShortcut() {
        //TODO: Implement save method and call here
        System.out.println("Saving.....");
    }

    /**
     * The shortcut function to load the current project
     */
    private static void loadShortcut() {
        //TODO: Implement load method and call here
        System.out.println("Loading.....");
    }

    /**
     * The shortcut function to load the preferences for the Editor and current project
     */
    private static void preferencesShortcut() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_P)) PREFERENCES.setOpen(new ImBoolean(true));
        }
        PREFERENCES.tick();
    }

    /**
     * The shortcut function to load the Help options
     */
    private static void helpShortcut() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_H)) HELP_PANEL_MENU.setOpen(new ImBoolean(true));
        }
        HELP_PANEL_MENU.tick();
    }

    /**
     * The shortcut function for the Debug options
     */
    private static void debugShortcuts() {
        // TODO: Implement debug shortcuts
    }
}
/*End of Shortcuts class*/
