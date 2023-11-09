/*
 Title: Shortcuts
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.inputs;

/** This class contains all the keyboard shortcut options*/
public class Shortcuts {

    /** Check for input and call shortcut functionality This method is called in the
     * Renderer class's tick() method currently
     */
    public static void tick() {
        projectShortcuts();
        helpShortcuts();
        debugShortcuts();
    }

    private static void projectShortcuts() {
        //TODO: Implement the project drop down shortcuts. save, load, etc..
    }

    private static void helpShortcuts() {
        // TODO: Implement help shortcuts
    }

    private static void debugShortcuts() {
        // TODO: Implement debug shortcuts
    }
}
/*End of Shortcuts class*/
