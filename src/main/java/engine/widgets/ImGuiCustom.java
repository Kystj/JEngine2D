/*
 Title: ImGuiCustom
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.widgets;

import engine.UI.EngineWindow;
import engine.inputs.MouseInputs;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

import static engine.settings.EConstants.POPUP_WIN_SIZE;
import static engine.settings.EConstants.X_SPACING;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class ImGuiCustom {

    /**
     * Custom placement for the ImGui close button for the application
     */
    public static boolean closeButton(float xPlacement, float yPlacement) {
        ImGui.spacing();
        ImGui.setCursorPos(xPlacement, yPlacement);
        return !ImGui.button("Close", 50.0f, 20.0f);
    }

    /**
     * The ImGui close button for the application
     */
    public static boolean closeButton() {
        ImGui.setCursorPosX(X_SPACING);
        return !ImGui.button("Close", 50.0f, 20.0f);
    }

    /**
     * Actives a popup window with the specified description as the text and calls the centrePopupText it on the screen
     */
    public static void activatePopup(String description) {
        ImGui.openPopup(description);
        centrePopup();
        // AlwaysAutoResize ensures the window resizes itself based on its content
        if (ImGui.beginPopup(description, ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoScrollbar)) {
            centrePopupText(description);
        }
        closePopup();
        ImGui.endPopup();
    }

    /**
     * Centres the popup windows text
     */
    private static void centrePopupText(String description) {
        ImVec2 textSize = ImGui.calcTextSize(description);
        ImVec2 centerPos = new ImVec2(new ImVec2((POPUP_WIN_SIZE.x - textSize.x) * 0.5f,
                (POPUP_WIN_SIZE.y - textSize.y) * 0.5f));
        // Allows me to manually set where the next UI element will be drawn
        ImGui.setCursorPos(centerPos.x, centerPos.y);
        ImGui.text(description);
    }

    /**
     * Centres the popup window
     */
    private static void centrePopup() {
        ImVec2 popupPos = new ImVec2((EngineWindow.get().getWindowWidth() * 0.5f),
                (EngineWindow.get().getWindowHeight() * 0.5f));
        ImGui.setNextWindowSize(POPUP_WIN_SIZE.x, POPUP_WIN_SIZE.y);
        ImGui.setNextWindowPos(popupPos.x, popupPos.y);
    }

    /**
     * Closes the popup if the user clocks anywhere in the editor
     */
    private static void closePopup() {
        if (MouseInputs.getMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            ImGui.closeCurrentPopup();
        }
    }
}
/*End of ImGuiCustom class*/
