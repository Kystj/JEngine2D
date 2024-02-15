/*
 Title: Menu
 Date: 2023-11-11
 Author: Kyle St John
 */
package engine.UI.widgets;

import engine.UI.engine.ImGuiCustom;
import imgui.ImGui;

public class MenuPanel {

    private boolean open = false;
    protected String fileName;
    protected String menuName;

    public void tick() {
        if (open) {
            displayMenu();
        }
    }

    protected void readFile() {
        // TODO: Implement file reader
    }

    protected void displayMenu() {
        ImGui.begin(menuName);
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
/*End of Menu class*/
