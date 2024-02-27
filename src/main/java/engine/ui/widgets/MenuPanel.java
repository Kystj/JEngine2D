/*
 Title: Menu
 Date: 2023-11-11
 Author: Kyle St John
 */
package engine.ui.widgets;

import imgui.ImGui;
import imgui.type.ImBoolean;

public class MenuPanel {

    private ImBoolean open = new ImBoolean(false);
    protected String fileName;
    protected String menuName;

    public void tick() {
        if (open.get()) {
            displayMenu();
        }
    }

    protected void readFile() {
        // TODO: Implement file reader
    }

    protected void displayMenu() {
        ImGui.begin(menuName, open);
        // TODO: Create Help info text file, read it and place its contents here

        ImGui.end();
    }

    public ImBoolean isOpen() {
        return open;
    }

    public void setOpen(ImBoolean open) {
        this.open = open;
    }
}
/*End of Menu class*/
