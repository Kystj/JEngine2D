/*
 Title: Shortcuts
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.managers;

import engine.inputs.KeyInputs;
import engine.widgets.HelpMenu;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This class contains all the keyboard shortcut options
 */
public class ShortcutManager {

    private final HelpMenu helpMenu = new HelpMenu();

    /**
     * Check for input and call shortcut functionality This method is called in the
     * Renderer class's tick() method currently
     */
    public void tick() {
        projectShortcuts();
        helpShortcuts();
        debugShortcuts();
    }

    /**
     * Recognize user input for shortcuts available under the Project drop down menu.
     */
    public void projectShortcuts() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_S)) saveShortcut();
            if (KeyInputs.keyPressed(GLFW_KEY_L)) loadShortcut();
            if (KeyInputs.keyPressed(GLFW_KEY_P)) preferencesShortcut();
        }
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
     * The shortcut function to load the current project
     */
    private void preferencesShortcut() {
        //TODO: Implement the open preferences method and call here
        System.out.println("Opening Preferences.....");
    }

    /**
     * The shortcut function to load the Help options
     */
    private void helpShortcuts() {
        if (KeyInputs.keyPressed(GLFW_KEY_LEFT_CONTROL)) {
            if (KeyInputs.keyPressed(GLFW_KEY_H)) helpMenu.setOpen(true);
        }
        helpMenu.tick();
    }

    private static void debugShortcuts() {
        // TODO: Implement debug shortcuts
    }
}
/*End of Shortcuts class*/
