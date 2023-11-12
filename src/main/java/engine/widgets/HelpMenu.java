/*
 Title: HelpMenu
 Date: 2023-11-11
 Author: Kyle St John
 */
package engine.widgets;

import imgui.ImGui;

public class HelpMenu {

    private boolean open = false;
    private final String fileName = "file.txt";

    public HelpMenu() {
        readHelpFile();
    }

    public void tick() {
        if (open) {
            displayHelpMenu();
        }
    }

    private void readHelpFile() {
        // TODO: Implement file reader
    }

    private void displayHelpMenu() {
        ImGui.begin("Help");
        // TODO: Create Help info text file, read it and place its contents here
        open = ImGuiCustom.closeButton();
        ImGui.end();
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}
/*End of HelpMenu class*/
