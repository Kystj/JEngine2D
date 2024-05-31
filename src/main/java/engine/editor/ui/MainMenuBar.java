/*
 Title: JMenuBar
 Date: 2023-11-08
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debugging.info.Logger;
import engine.debugging.ui.DebugPanel;
import engine.editor.GameEditor;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.graphics.Renderer;
import engine.physics.PhysicsMain;
import engine.serialization.LevelSerializer;
import engine.utils.engine.EConstants;
import engine.world.levels.TestLevel;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class MainMenuBar {

    private final HelpMenu helpPanelMenu = new HelpMenu();
    private final Preferences preferences = new Preferences();

    public void imgui() {
        ImGui.beginMainMenuBar();
        projectMenuItem();
        toolsMenu();
        helpMenu();
        ImGui.endMainMenuBar();
    }

    private void projectMenuItem() {
        if (ImGui.beginMenu("Project")) {
            saveMenuItem();
            loadMenuItem();
            importMenuItem();
            preferencesMenuItem();
            ImGui.endMenu();
        }
        checkWidgetStatus();
    }

    private void checkWidgetStatus() {
        updatePreferenceWindow();
        updateHelpWindow();
    }

    private void updatePreferenceWindow() {
        if (preferences.isOpen().get()) {
            preferences.tick();
        }
    }

    private void updateHelpWindow() {
        if (helpPanelMenu.isOpen().get()) {
            helpPanelMenu.tick();
        }
    }

    private void preferencesMenuItem() {
        if (ImGui.menuItem("Preferences", "Ctrl+p")) preferences.setOpen(new ImBoolean(true));
        // TODO: Implement preferences
    }

    private void saveMenuItem() {
        if (ImGui.menuItem("Save", "Ctrl+s")) {
            LevelSerializer.save(GameEditor.current_Level);
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Save));
        }
    }

    private void loadMenuItem() {
        if (ImGui.menuItem("Load", "Ctrl+o")) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Load_New_Scene),
                    new TestLevel(new Renderer(), new PhysicsMain()));
        }
    }


    private void importMenuItem() {
        if (ImGui.menuItem("Import", "Ctrl+i")) {
            ImportWindow.bIsImportOpen.set(true);
        }
    }

    private void helpMenuItem() {
        if (ImGui.menuItem("Help", "Ctrl+h")) helpPanelMenu.setOpen(new ImBoolean(true));
        //TODO: Implement help and shortcuts
    }


    private void toolsMenu() {
        if (ImGui.beginMenu("Tools")) {

            if (ImGui.menuItem(" Debug Console", "Ctrl+d")) {
                DebugPanel.setIsOpen(true);

            }

            if (ImGui.menuItem(" Task Manager", "Ctrl+m")) {
                System.out.println("Opening task manager");
            }

            if (ImGui.menuItem(" Log Console", "Ctrl+l")) {
                Logger.setIsOpen(true);
            }

            if (ImGui.menuItem(" View Assets", "Ctrl+a")) {
                ImportWindow.setIsOpen(true);
            }

            if (ImGui.menuItem(" Content Window", "Ctrl+c")) {
                ContentWindow.setIsOpen(true);
            }

            ImGui.endMenu();
        }
    }

    private void helpMenu() {
        if (ImGui.beginMenu("Help")) {
            helpMenuItem();
            ImGui.endMenu();
        }
    }
}
/*End of JMenuBar class*/
