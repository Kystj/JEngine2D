/*
 Title: ImGuiCustom
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.utils;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.graphics.Texture;
import engine.io.MouseInputs;
import engine.settings.EConstants;
import engine.ui.engine.EngineWindow;
import engine.ui.editor.EditorScene;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static engine.settings.EConstants.POPUP_WIN_SIZE;
import static engine.settings.EConstants.X_SPACING;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class ImGuiUtils {

    public static final Texture lockTexture = ResourceHandler.getOrCreateTexture("assets/buttons/locked.png");
    public static final Texture unlock = ResourceHandler.getOrCreateTexture("assets/buttons/unlocked.png");
    public static final Texture grid = ResourceHandler.getOrCreateTexture("assets/buttons/grid.png");
    public static final Texture launch = ResourceHandler.getOrCreateTexture("assets/buttons/launch.png");
    public static final Texture play = ResourceHandler.getOrCreateTexture("assets/buttons/play.png");
    public static final Texture stop = ResourceHandler.getOrCreateTexture("assets/buttons/stop.png");
    public static final Texture wireframe = ResourceHandler.getOrCreateTexture("assets/buttons/frame.png");

    private static final Vector2f[] texCords = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public static void renderMetricsInfo() {
        ImGui.setCursorPosX(X_SPACING);
        float deltaTimeInSeconds = EngineWindow.get().getDeltaTime();
        double deltaTimeInMilliseconds = deltaTimeInSeconds * 1000; // Convert seconds to milliseconds
        double fps = 1.0 / deltaTimeInSeconds; // Calculate frames per second

        String string = " TPF(ms): " + String.format("%.2f", deltaTimeInMilliseconds) +
                "    FPS: " + String.format("%.2f", fps);
        ImGui.text(string);

    }

    public static void renderWireFrameModeButton(boolean isOn) {
        if (ImGui.imageButton(wireframe.getTextureID(), 16, 16, texCords[0].x, texCords[0].y, texCords[2].x, texCords[2].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Wire_Frame));
        }
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            if (isOn) {
                ImGui.setTooltip("Turn Off Wireframe Mode");
            } else {
                ImGui.setTooltip("Turn On Wireframe Mode");
            }
            ImGui.endTooltip();
        }
    }

    public static void renderLaunchButton() {
        if (ImGui.imageButton(launch.getTextureID(), 16, 16, texCords[2].x, texCords[0].y, texCords[0].x, texCords[2].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Launch));
        }
    }

    public static void renderPlayButton() {
        if (ImGui.imageButton(play.getTextureID(), 16, 16, texCords[2].x, texCords[2].y, texCords[0].x, texCords[0].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Play));
        }
    }

    public static void renderStopButton() {
        if (ImGui.imageButton(stop.getTextureID(), 16, 16, texCords[2].x, texCords[2].y, texCords[0].x, texCords[0].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Stop));
        }
    }

    public static void renderGridSizeButton() {
        if (ImGui.beginPopupContextItem("cellSize")) {
            if (ImGui.selectable("16"))   EditorScene.setCellSize(16);
            if (ImGui.selectable("32"))   EditorScene.setCellSize(32);
            if (ImGui.selectable("64"))   EditorScene.setCellSize(64);
            if (ImGui.selectable("128"))  EditorScene.setCellSize(128);
            if (ImGui.selectable("256"))  EditorScene.setCellSize(256);
            ImGui.endPopup();
        }

        if (ImGui.imageButton(grid.getTextureID(), 16, 16, texCords[0].x, texCords[0].y, texCords[2].x, texCords[2].y)) {
            ImGui.openPopup("cellSize");
        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Change Cell Size");
            ImGui.endTooltip();
        }
    }

    public static boolean renderLockButton(Texture texture, boolean isLocked) {
        String lockedStr = "Lock";
        String unlockedStr = "Unlock";

        if (ImGui.imageButton(texture.getTextureID(), 16, 16, texCords[0].x, texCords[0].y, texCords[2].x, texCords[2].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Grid_Lock)); // Turn off snap to grid
            isLocked = !isLocked;
        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            if (isLocked) {
                ImGui.setTooltip(unlockedStr + " Grid Snapper");
            } else {
                ImGui.setTooltip(lockedStr + " Grid Snapper");
            }
            ImGui.endTooltip();
        }
        return isLocked;
    }

    public static void renderAspectRatioButton() {
        if (ImGui.beginPopupContextItem("aspectRatio")) {
            if (ImGui.selectable("16:9"))   EngineWindow.get().setDefaultAspectRatio(16.0f / 9.0f);
            if (ImGui.selectable("16:10"))  EngineWindow.get().setDefaultAspectRatio(16.0f / 10.0f);
            if (ImGui.selectable("21:9"))   EngineWindow.get().setDefaultAspectRatio(21.0f / 9.0f);
            if (ImGui.selectable("4:3"))    EngineWindow.get().setDefaultAspectRatio(4.0f / 3.0f);
            ImGui.endPopup();
        }

        String aspectRatio = MathUtils.decimalToAspectRatio(EngineWindow.get().getDefaultAspectRatio());

        if (ImGui.button(aspectRatio)) {
            ImGui.openPopup("aspectRatio");
        }

        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Change Aspect Ratio");
            ImGui.endTooltip();
        }
    }

    public static Vector3f renderColorPicker3f(String sliderID, Vector3f color) {
        Vector3f newColor = new Vector3f(color);
        ImGui.pushID(sliderID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 80); // TODO: Should be dynamic
        ImGui.text(sliderID);
        ImGui.nextColumn();

        float[] imColor = {newColor.x, newColor.y, newColor.z};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            newColor.set(imColor[0], imColor[1], imColor[2]);
        }

        ImGui.columns(1);
        ImGui.popID();

        return newColor;
    }

    public static Vector4f renderColorPicker4f(String sliderID, Vector4f color) {
        Vector4f newColor = new Vector4f(color);
        ImGui.pushID(sliderID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 80); // TODO: Should be dynamic
        ImGui.text(sliderID);
        ImGui.nextColumn();

        float[] imColor = {newColor.x, newColor.y, newColor.z, newColor.w};
        if (ImGui.colorEdit4("##colorPicker", imColor)) {
            newColor.set(imColor[0], imColor[1], imColor[2], imColor[3]);
        }

        ImGui.columns(1);
        ImGui.popID();
        return newColor;
    }


    public static void renderVec2Sliders(String sliderID, Vector2f val, Vector2f resetVal) {
        ImGui.pushID(sliderID);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 80); // TODO: Should be dynamic
        ImGui.text(sliderID);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f); // TODO: Customize
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);
        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            val.x = resetVal.x;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesX = {val.x};
        ImGui.dragFloat("##x", vecValuesX, 0.1f); // TODO: Make a constant
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);
        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            val.y = resetVal.y;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValuesY = {val.y};
        ImGui.dragFloat("##y", vecValuesY, 0.1f);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.nextColumn();

        val.x = vecValuesX[0];
        val.y = vecValuesY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static float renderFloatSlider(String label, float val) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 80); // TODO: Should be dynamic
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {val};
        ImGui.dragFloat("##dragFloat", valArr, 0.3f); // TODO: Make a constant

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int renderIntSlider(String label, int val) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 80); // TODO: Should be dynamic
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {val};
        ImGui.dragInt("##dragInt", valArr, 0.3f); // TODO: Make a constant

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }


    public static boolean activatePopup(String description) {
        ImGui.openPopup(description);
        centrePopup();
        // AlwaysAutoResize ensures the window resizes itself based on its content
        if (ImGui.beginPopup(description, ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoScrollbar)) {
            centrePopupText(description);
        }
        ImGui.endPopup();
        return closePopup();
    }


    private static void centrePopupText(String description) {
        ImVec2 textSize = ImGui.calcTextSize(description);
        ImVec2 centerPos = new ImVec2(new ImVec2((POPUP_WIN_SIZE.x - textSize.x) * 0.5f,
                (POPUP_WIN_SIZE.y - textSize.y) * 0.5f));
        // Allows me to manually set where the next UI element will be drawn
        ImGui.setCursorPos(centerPos.x, centerPos.y);
        ImGui.text(description);
    }


    private static void centrePopup() {
        ImVec2 popupPos = new ImVec2((EngineWindow.get().getWindowWidth() * 0.5f),
                (EngineWindow.get().getWindowHeight() * 0.5f));
        ImGui.setNextWindowSize(POPUP_WIN_SIZE.x, POPUP_WIN_SIZE.y);
        ImGui.setNextWindowPos(popupPos.x, popupPos.y);
    }


    private static boolean closePopup() {
        if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
            ImGui.closeCurrentPopup();
            return false;
        }
        return true;
    }
}
/*End of ImGuiCustom class*/
