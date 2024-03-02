/*
 Title: ImGuiCustom
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.ui.engine;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.graphics.Texture;
import engine.io.MouseInputs;
import engine.ui.settings.EConstants;
import engine.utils.ResourceHandler;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector2f;

import static engine.ui.settings.EConstants.POPUP_WIN_SIZE;
import static engine.ui.settings.EConstants.X_SPACING;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class ImGuiUtils {

    public static final Texture lockTexture = ResourceHandler.getOrCreateTexture("assets/buttons/locked.png");
    public static final Texture unlock = ResourceHandler.getOrCreateTexture("assets/buttons/unlocked.png");


    public static void renderMetricsInfo() {
        ImGui.setCursorPosX(X_SPACING);
        float deltaTimeInSeconds = EngineWindow.get().getDeltaTime();
        double deltaTimeInMilliseconds = deltaTimeInSeconds * 1000; // Convert seconds to milliseconds
        double fps = 1.0 / deltaTimeInSeconds; // Calculate frames per second

        String string = " TPF(ms): " + String.format("%.2f", deltaTimeInMilliseconds) +
                "    FPS: " + String.format("%.2f", fps);
        ImGui.text(string);
    }

    public static boolean renderLockButton(Texture texture, boolean isLocked) {
        String lockedStr = "Lock";
        String unlockedStr = "Unlock";

        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)

        };
        if (ImGui.imageButton(texture.getTextureID(), 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
            System.out.println("This ran");
            EventDispatcher.dispatchEvent(new Event(EConstants.EventType.User)); // Turn off snap to grid
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


        // TODO: Remove and refactor
        public static boolean closeButton ( float xPlacement, float yPlacement){
            ImGui.spacing();
            ImGui.setCursorPos(xPlacement, yPlacement);
            return !ImGui.button("Close", 50.0f, 20.0f);
        }

        // TODO: Remove and refactor
        public static boolean closeButton () {
            ImGui.setCursorPosX(X_SPACING);
            return !ImGui.button("Close", 50.0f, 20.0f);
        }


        public static boolean activatePopup (String description){
            ImGui.openPopup(description);
            centrePopup();
            // AlwaysAutoResize ensures the window resizes itself based on its content
            if (ImGui.beginPopup(description, ImGuiWindowFlags.AlwaysAutoResize | ImGuiWindowFlags.NoScrollbar)) {
                centrePopupText(description);
            }
            ImGui.endPopup();
            return closePopup();
        }


        private static void centrePopupText (String description){
            ImVec2 textSize = ImGui.calcTextSize(description);
            ImVec2 centerPos = new ImVec2(new ImVec2((POPUP_WIN_SIZE.x - textSize.x) * 0.5f,
                    (POPUP_WIN_SIZE.y - textSize.y) * 0.5f));
            // Allows me to manually set where the next UI element will be drawn
            ImGui.setCursorPos(centerPos.x, centerPos.y);
            ImGui.text(description);
        }


        private static void centrePopup () {
            ImVec2 popupPos = new ImVec2((EngineWindow.get().getWindowWidth() * 0.5f),
                    (EngineWindow.get().getWindowHeight() * 0.5f));
            ImGui.setNextWindowSize(POPUP_WIN_SIZE.x, POPUP_WIN_SIZE.y);
            ImGui.setNextWindowPos(popupPos.x, popupPos.y);
        }


        private static boolean closePopup () {
            if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
                ImGui.closeCurrentPopup();
                return false;
            }
            return true;
        }
    }
/*End of ImGuiCustom class*/
