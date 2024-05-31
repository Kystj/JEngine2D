package engine.debugging.ui;

import engine.debugging.draw.DebugRenderer;
import engine.debugging.info.Logger;
import engine.graphics.Texture;
import engine.debugging.info.ErrorManager;
import engine.utils.engine.ResourceUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.ArrayList;

import static engine.utils.engine.EConstants.GREEN_BUTTON;
import static engine.utils.engine.EConstants.X_SPACING;
import static engine.utils.imgui.ImGuiUtils.setSectionName;

/**
 * Represents a debug panel containing various debugging tools and information for user interface rendering.
 */
public class DebugPanel {

    // Variables

    private static final ImBoolean isOpen = new ImBoolean(true);
    private static final ErrorPanel ERROR_PANEL = new ErrorPanel();
    private static ShapeGenerator SHAPE_GENERATOR = new ShapeGenerator();
    private static final Texture REFRESH_BUTTON = ResourceUtils.getOrCreateTexture("assets/buttons/Refresh.png");
    private static int SHAPE_ID = 0;

    // Public methods

    /**
     * Renders the debug panel using ImGui.
     */
    public static void imgui() {
        if (isOpen.get()) {
            ImGui.begin("Debug", isOpen);
            renderDebugUI();
            ImGui.end();
        }
    }

    /**
     * Sets the state of the debug panel.
     *
     * @param shouldOpen True to open the debug panel, false to close it.
     */
    public static void setIsOpen(boolean shouldOpen) {
        isOpen.set(isOpen);
    }

    // Private methods

    /**
     * Renders the debug user interface.
     */
    private static void renderDebugUI() {
        ERROR_PANEL.imGui();
        setSectionName("Draw");
        updateDebugDrawing();
        SHAPE_GENERATOR.renderUI();

        setSectionName("Info");
        ImGui.setCursorPosX(X_SPACING);
        ErrorManager.displayReportList();

        ImGui.sameLine();
        ImGui.pushStyleColor(ImGuiCol.Button, GREEN_BUTTON.x, GREEN_BUTTON.y, GREEN_BUTTON.z, GREEN_BUTTON.w);
        if (ImGui.button("+")) {
            ERROR_PANEL.setGenerateNewReport(new ImBoolean(true));
        }
        ImGui.popStyleColor();
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Report new error");
            ImGui.endTooltip();
        }
    }

    /**
     * Updates the debug drawing based on user input.
     */
    private static void updateDebugDrawing() {

        ArrayList<String> shapes = new ArrayList<>();
        shapes.add("Line");
        shapes.add("Box");
        shapes.add("Circle");
        shapes.add("Triangle");
        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.beginCombo("##debugShapes", "Select shape")) {
            for (int i = 0; i < shapes.size(); i++) {
                if (ImGui.selectable(shapes.get(i))) {
                    SHAPE_ID = i;
                }
            }
            ImGui.endCombo();
        }
        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.imageButton(REFRESH_BUTTON.getTextureID(), 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
            DebugRenderer.clearPersistentLines();
        }
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Clear debug lines");
            ImGui.endTooltip();
        }
        if (SHAPE_ID != -1) {
            switch (SHAPE_ID) {
                case 0:
                    SHAPE_GENERATOR = new LineGenerator();
                    break;
                case 1:
                    SHAPE_GENERATOR = new BoxGenerator();
                    break;
                case 2:
                    SHAPE_GENERATOR = new CircleGenerator();
                    break;
                case 3:
                    SHAPE_GENERATOR = new TriangleGenerator();
                    break;
                default:
                    Logger.error("Invalid shapeID");
                    break;
            }
        }
    }
}