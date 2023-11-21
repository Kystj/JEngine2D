/*
 Title: Viewport
 Date: 2023-11-18
 Author: Kyle St John
 */
package engine.editor;

import engine.UI.EngineWindow;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.settings.EConstants.EventType;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class Viewport {

    private boolean playing = false;

    public void tick() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar);

        viewportsMenuBar();
        int textureId = EngineWindow.get().getFramebufferTexID();
        ImGui.image(textureId, EngineWindow.get().getWindowWidth(),EngineWindow.get().getWindowHeight(), 0, 1, 1, 0);

        ImGui.end();
    }

    private void viewportsMenuBar() {
        ImGui.beginMenuBar();

        if (ImGui.menuItem("Play", "", playing, !playing)) {
            playing = true;
            EventDispatcher.dispatchEvent(new Event(EventType.Play));
        }

        if (ImGui.menuItem("Stop", "", !playing, playing)) {
            playing = false;
            EventDispatcher.dispatchEvent(new Event(EventType.Stop));
        }

        if (ImGui.menuItem("Launch", "")) {
            EventDispatcher.dispatchEvent(new Event(EventType.FullPlay));
        }
        ImGui.endMenuBar();
    }
}
/*End of Viewport class*/
