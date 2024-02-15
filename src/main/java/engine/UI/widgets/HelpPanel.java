/*
 Title: HelpMenu
 Date: 2023-11-11
 Author: Kyle St John
 */
package engine.UI.widgets;

public class HelpPanel extends MenuPanel {

    public HelpPanel() {
        this.fileName = "";
        this.menuName = "Help";
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
    public boolean isOpen() {
        return super.isOpen();
    }

    @Override
    public void setOpen(boolean open) {
        super.setOpen(open);
    }
}
/*End of HelpMenu class*/
