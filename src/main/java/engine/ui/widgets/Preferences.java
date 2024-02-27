/*
 Title: PreferencesMenu
 Date: 2023-11-11
 Author: Kyle St John
 */
package engine.ui.widgets;

import imgui.type.ImBoolean;

public class Preferences extends MenuPanel {

    public Preferences() {
        this.fileName = "";
        this.menuName = "Preferences";
        readFile();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void readFile() {
        super.readFile();
    }

    @Override
    protected void displayMenu() {
        super.displayMenu();
    }

    @Override
    public ImBoolean isOpen() {
        return super.isOpen();
    }

    @Override
    public void setOpen(ImBoolean open) {
        super.setOpen(open);
    }
}
/*End of PreferencesMenu class*/
