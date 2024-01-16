/*
 * Title: Viewport
 * Date: 2023-11-18
 * Author: Kyle St John
 */
package engine.editor;

import engine.UI.EngineWindow;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.settings.EConstants.EventType;
import imgui.ImGui;
import imgui.flag.ImGuiViewportFlags;
import imgui.flag.ImGuiWindowFlags;

/**
 * Viewport class for rendering and interacting with the game view.
 */
public class Viewport {

    private boolean playing = false;

    /**
     * Renders and updates the Viewport.
     */
    public void tick() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar | ImGuiViewportFlags.NoTaskBarIcon);

        viewportsMenuBar();

        int textureId = EngineWindow.get().getFramebufferTexID();
        ImGui.image(textureId, EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight(), 0, 1, 1, 0);

        ImGui.end();
    }

    /**
     * Renders the menu bar with playback controls.
     */
    private void viewportsMenuBar() {
        ImGui.beginMenuBar();

        // Calculate the width of the menu bar
        float menuBarWidth = ImGui.getContentRegionAvailX();

        // Calculate the total width of the buttons
        float totalButtonWidth = 3 * 100f; // Adjust the button width and count as needed

        // Calculate the left padding required to center the buttons
        float leftPadding = (menuBarWidth - totalButtonWidth) * 0.5f;

        // Set the cursor position to the left padding
        ImGui.setCursorPosX(leftPadding);

        // Button 1 (Play)
        if (ImGui.button("Play")) {
            playing = true;
            EventDispatcher.dispatchEvent(new Event(EventType.Play));
        }

        // Use ImGui.sameLine() to keep the next elements on the same line
        ImGui.sameLine();

        // Set the cursor position to center the remaining space
        ImGui.setCursorPosX(ImGui.getCursorPosX());

        // Button 2 (Stop)
        if (ImGui.button("Stop")) {
            playing = false;
            EventDispatcher.dispatchEvent(new Event(EventType.Stop));
        }

        // Use ImGui.sameLine() to keep the next elements on the same line
        ImGui.sameLine();

        // Button 3 (Launch)
        if (ImGui.button("Launch")) {
            EventDispatcher.dispatchEvent(new Event(EventType.FullPlay));
        }

        ImGui.endMenuBar();
    }
}
/* End of Viewport class */