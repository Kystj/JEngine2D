/*
 * Title: Viewport
 * Date: 2023-11-18
 * Author: Kyle St John
 */
package engine.ui.editor;

import engine.io.MouseInputs;
import engine.ui.engine.EngineWindow;
import engine.ui.engine.ImGuiUtils;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiViewportFlags;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

public class Viewport {

    private boolean isGridLocked = true;

    public void tick() {
        ImGui.begin("Viewport", ImGuiWindowFlags.NoScrollbar
                | ImGuiWindowFlags.NoScrollWithMouse | ImGuiViewportFlags.NoTaskBarIcon);

        viewportControls();

        int textureId = EngineWindow.get().getFramebufferTexID();

        ImVec2 windowSize = getViewportSize();
        ImVec2 windowPos = getCenteredPositionForViewport(windowSize);

        ImGui.setCursorPos(windowPos.x, windowPos.y);

        ImVec2 topLeft = new ImVec2();
        ImGui.getCursorScreenPos(topLeft);
        topLeft.x -= ImGui.getScrollX();
        topLeft.y -= ImGui.getScrollY();

        ImGui.image(textureId, windowSize.x, windowSize.y, 0, 1, 1, 0);

        MouseInputs.setViewportPos(new Vector2f(topLeft.x, topLeft.y));
        MouseInputs.setViewportSize(new Vector2f(windowSize.x, windowSize.y));

        ImGui.end();
    }

    private ImVec2 getViewportSize() {
        ImVec2 windowSize = getWindowSize();
        float aspectWidth = windowSize.x;
        float aspectRatio = EngineWindow.get().getDefaultAspectRatio();
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

    private void viewportControls() {
        float totalButtonWidth = 3 * ImGui.calcTextSize("Launch").x + ImGui.getStyle().getItemSpacingX() * 2;
        float leftPadding = (ImGui.getContentRegionAvailX() - totalButtonWidth) * 0.5f;
        float topPadding = (ImGui.getContentRegionAvailY() * 0.025f);

        ImGui.setCursorPosY(topPadding);
        ImGuiUtils.renderMetricsInfo();

        ImGui.setCursorPosX(leftPadding);
        ImGui.setCursorPosY(topPadding);

        ImGuiUtils.renderPlayButton();
        ImGui.sameLine();

        ImGuiUtils.renderStopButton();
        ImGui.sameLine();

        ImGuiUtils.renderLaunchButton();
        ImGui.sameLine();

        ImGui.setCursorPosX(ImGui.getWindowSizeX() * .92f);
        ImGuiUtils.renderAspectRatioButton();

        ImGui.sameLine();
        if (isGridLocked) {
            isGridLocked = ImGuiUtils.renderLockButton(ImGuiUtils.lockTexture, true);
        } else {
            isGridLocked = ImGuiUtils.renderLockButton(ImGuiUtils.unlock, false);
        }

        ImGui.sameLine();
        ImGuiUtils.renderGridSizeButton();

        ImGui.sameLine();
        ImGuiUtils.renderWireFrameModeButton(EngineWindow.get().isFrameModeOn());
    }
}
/* End of Viewport class */