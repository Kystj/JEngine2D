/*
 * Title: Viewport
 * Date: 2023-11-18
 * Author: Kyle St John
 */
package engine.UI.editor;

import engine.UI.engine.EngineWindow;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.UI.settings.EConstants.EventType;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiViewportFlags;
import imgui.flag.ImGuiWindowFlags;

public class Viewport {

    public void tick() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoScrollWithMouse | ImGuiWindowFlags.MenuBar | ImGuiViewportFlags.NoTaskBarIcon);

        viewportsMenuBar();

        int textureId = EngineWindow.get().getFramebufferTexID();

        ImVec2 windowSize = getViewportSize();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        ImGui.end();
    }

    private ImVec2 getViewportSize() {
        ImVec2 windowSize = getWindowSize();
        float aspectWidth = windowSize.x;
        // Aspect ratio (for example, 16:9)
        float aspectRatio = 16.0f / 9.0f;
        float aspectHeight = aspectWidth / aspectRatio;
        if (aspectHeight > windowSize.y) {
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * aspectRatio;
        }
        return new ImVec2(aspectWidth, aspectHeight);
    }

    private ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = getWindowSize();
        float viewportX = (windowSize.x - aspectSize.x) * 0.5f;
        float viewportY = (windowSize.y - aspectSize.y) * 0.5f;
        return new ImVec2(viewportX + ImGui.getCursorPosX(), viewportY + ImGui.getCursorPosY());
    }

    private ImVec2 getWindowSize() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        return windowSize;
    }

    private void viewportsMenuBar() {
        ImGui.beginMenuBar();

        float menuBarWidth = ImGui.getContentRegionAvailX();
        float totalButtonWidth = 3 * ImGui.calcTextSize("Play").x + ImGui.getStyle().getItemSpacingX() * 2;
        float leftPadding = (menuBarWidth - totalButtonWidth) * 0.5f;

        ImGui.setCursorPosX(leftPadding);

        if (ImGui.button("Play")) {
            EventDispatcher.dispatchEvent(new Event(EventType.Play));
        }

        ImGui.sameLine();

        if (ImGui.button("Stop")) {
            EventDispatcher.dispatchEvent(new Event(EventType.Stop));
        }

        ImGui.sameLine();

        if (ImGui.button("Launch")) {
            EventDispatcher.dispatchEvent(new Event(EventType.FullPlay));
        }

        ImGui.endMenuBar();
    }
}
/* End of Viewport class */