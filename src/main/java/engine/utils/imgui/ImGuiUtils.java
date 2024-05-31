/*
 Title: ImGuiCustom
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.utils.imgui;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.graphics.EngineWindow;
import engine.graphics.Texture;
import engine.utils.engine.EConstants;
import engine.utils.math.MathUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiPopupFlags;
import imgui.flag.ImGuiStyleVar;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static engine.utils.engine.EConstants.X_SPACING;
import static engine.utils.engine.EConstants.TEXTURE_COORDINATES;
import static engine.utils.imgui.GConstants.*;

public class ImGuiUtils {

    public static void renderMetricsInfo() {
        ImGui.setCursorPosX(X_SPACING);
        float deltaTimeInSeconds = EngineWindow.get().getDeltaTime();

        String string = " TPF(ms): " + String.format("%.2f", EngineWindow.FRAME_TIME) +
                "    FPS: " + String.format("%.0f", EngineWindow.FPS);
        ImGui.text(string);
    }

    public static void renderWireFrameModeButton(boolean isOn) {
        if (ImGui.imageButton(WIREFRAME_BUTTON_TEXTURE.getTextureID(), 16, 16, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[0].y, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[2].y)) {
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
        if (ImGui.imageButton(LAUNCH_BUTTON_TEXTURE.getTextureID(), 16, 16, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[0].y, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[2].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Launch));
        }
    }

    public static void renderPlayButton() {
        if (ImGui.imageButton(PLAY_BUTTON_TEXTURE.getTextureID(), 16, 16, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[2].y, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[0].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Play));
        }
    }

    public static void renderStopButton() {
        if (ImGui.imageButton(STOP_BUTTON_TEXTURE.getTextureID(), 16, 16, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[2].y, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[0].y)) {
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Stop));
        }
    }

    public static void renderGridSizeButton() {
        if (ImGui.beginPopupContextItem("cellSize")) {
            if (ImGui.selectable("16"));
            if (ImGui.selectable("32"));
            if (ImGui.selectable("64"));
            if (ImGui.selectable("128"));
            if (ImGui.selectable("256"));
            ImGui.endPopup();
        }

        if (ImGui.imageButton(GRID_BUTTON_TEXTURE.getTextureID(), 16, 16, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[0].y, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[2].y)) {
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

        if (ImGui.imageButton(texture.getTextureID(), 16, 16, TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[0].y, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[2].y)) {
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
            if (ImGui.selectable("16:9"))   EngineWindow.get().setAspectRatio(16.0f / 9.0f);
            if (ImGui.selectable("16:10"))  EngineWindow.get().setAspectRatio(16.0f / 10.0f);
            if (ImGui.selectable("21:9"))   EngineWindow.get().setAspectRatio(21.0f / 9.0f);
            if (ImGui.selectable("4:3"))    EngineWindow.get().setAspectRatio(4.0f / 3.0f);
            ImGui.endPopup();
        }

        String aspectRatio = MathUtils.decimalToAspectRatio(EngineWindow.get().getAspectRatio());

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
        ImGui.setColumnWidth(0, COLUMN_WIDTH_MEDIUM);
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
        ImGui.setColumnWidth(0, COLUMN_WIDTH_MEDIUM);
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
        ImGui.setColumnWidth(0, COLUMN_WIDTH_MEDIUM);
        ImGui.text(sliderID);
        ImGui.nextColumn();

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0, 0);

        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float widthEach = (ImGui.calcItemWidth() - buttonSize.x * 2.0f) / 2.0f;

        float[] vecValuesX = {val.x};
        float[] vecValuesY = {val.y};

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.15f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.2f, 0.2f, 1.0f); // TODO: Customize
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.8f, 0.1f, 0.15f, 1.0f);

        if (ImGui.button("X", buttonSize.x, buttonSize.y)) {
            vecValuesX[0] = resetVal.x;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        ImGui.dragFloat("##x", vecValuesX, SLIDER_SPEED);
        ImGui.popItemWidth();
        ImGui.sameLine();

        ImGui.pushItemWidth(widthEach);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.2f, 0.7f, 0.2f, 1.0f);

        if (ImGui.button("Y", buttonSize.x, buttonSize.y)) {
            vecValuesY[0] = resetVal.y;
        }
        ImGui.popStyleColor(3);

        ImGui.sameLine();
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
        ImGui.setColumnWidth(0, COLUMN_WIDTH_MEDIUM);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {val};
        ImGui.dragFloat("##dragFloat", valArr, SLIDER_SPEED);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int renderIntSlider(String label, int val) {
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, COLUMN_WIDTH_MEDIUM);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {val};
        ImGui.dragInt("##dragInt", valArr, SLIDER_SPEED);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }


    public static void renderRightClickContext(String contextString) {

        boolean showTabContextMenu = ImGui.isItemClicked(ImGuiMouseButton.Right);

        if (showTabContextMenu) {
            ImGui.openPopup("TabContextMenu");
        }
        ImGui.sameLine();


        if (ImGui.beginPopup("TabContextMenu", ImGuiPopupFlags.MouseButtonRight)) {
            if (ImGui.menuItem(contextString)) {

            }
            ImGui.endPopup();
        }
    }


    public static void setSectionName(String name) {
        ImGui.spacing();
        ImGui.separator();
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);
        ImGui.text(name);
    }

    public static String camelCaseToWords(String camelCaseString) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < camelCaseString.length(); i++) {
            char c = camelCaseString.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append(" ").append(c);
            } else {
                result.append(c);
            }
        }

        // Capitalize the first letter of the first word
        result.setCharAt(0, Character.toUpperCase(result.charAt(0)));

        return result.toString();
    }
}
/*End of ImGuiCustom class*/
