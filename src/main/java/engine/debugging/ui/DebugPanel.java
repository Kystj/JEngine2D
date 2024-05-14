package engine.debugging.ui;

import engine.debugging.draw.DebugRenderer;
import engine.graphics.Texture;
import engine.utils.ReportUtils;
import engine.utils.ResourceUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.ArrayList;

import static engine.utils.EConstants.GREEN_BUTTON;
import static engine.utils.EConstants.X_SPACING;

public class DebugPanel {

    private static final ImBoolean bIsOpen = new ImBoolean(true);
    private static final ErrorPanel ERROR_PANEL = new ErrorPanel();
    private static ShapeGenerator SHAPE_GENERATOR = new ShapeGenerator();

    private static final Texture SQUARE_BUTTON = loadTexture("assets/buttons/Square.png");
    private static final Texture CIRCLE_BUTTON = loadTexture("assets/buttons/Circle.png");
    private static final Texture TRIANGLE_BUTTON = loadTexture("assets/buttons/Triangle.png");
    private static final Texture LINE_BUTTON = loadTexture("assets/buttons/Line.png");
    private static final Texture REFRESH_BUTTON = loadTexture("assets/buttons/Refresh.png");

    public static void imgui() {
        if (bIsOpen.get()) {
            ImGui.begin("Debug", bIsOpen);
            ERROR_PANEL.imgui();
            ImGui.spacing();
            drawShapes();
            drawReports();
            ImGui.end();
        }
    }

    private static void drawShapes() {
        if (ImGui.collapsingHeader("Draw")) {
            generateShapeButtons();
            SHAPE_GENERATOR.drawUI();
        }
    }

    private static void drawReports() {
        if (ImGui.collapsingHeader("Bugs")) {
            ImGui.setCursorPosX(X_SPACING);
            ReportUtils.displayReportList();
            ImGui.setCursorPosX(X_SPACING);

            ImGui.pushStyleColor(ImGuiCol.Button, GREEN_BUTTON.x, GREEN_BUTTON.y, GREEN_BUTTON.z, GREEN_BUTTON.w);
            if (ImGui.button("+")) {
                ERROR_PANEL.setGenerateNewReport(new ImBoolean(true));
            }
            ImGui.popStyleColor();

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.setTooltip("Report new bug");
                ImGui.endTooltip();
            }
        }

        if (ReportUtils.isShowReport()) {
            ReportUtils.displayReport();
        }
    }


    private static void generateShapeButtons() {
        ArrayList<Texture> buttons = new ArrayList<>();
        buttons.add(LINE_BUTTON);
        buttons.add(SQUARE_BUTTON);
        buttons.add(CIRCLE_BUTTON);
        buttons.add(TRIANGLE_BUTTON);

        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.imageButton(REFRESH_BUTTON.getTextureID(), 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
            DebugRenderer.clearPersistentLines();
        }
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Clear Debug Lines");
            ImGui.endTooltip();
        }

        ImGui.separator();

        for (int i = 0; i < buttons.size(); i++) {
            int id = buttons.get(i).getTextureID();
            ImGui.pushID(i);
            ImGui.setCursorPosX(X_SPACING + i * ImGui.getWindowSizeX() / 4);
            if (ImGui.imageButton(id, 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                switch (i) {
                    case 0:
                        SHAPE_GENERATOR = new LineGenerator();
                        break;
                    case 1:
                        SHAPE_GENERATOR = new BoxGenerator();
                        break;
                    case 2:
                        SHAPE_GENERATOR =  new CircleGenerator();
                        break;
                    case 3:
                        SHAPE_GENERATOR = new TriangleGenerator();
                        break;
                }
            }
            if (i <= 2) {
                ImGui.sameLine();
            }
            ImGui.popID();
        }
    }

    private static Texture loadTexture(String path) {
        Texture texture = ResourceUtils.getOrCreateTexture(path);
        if (texture == null) {
            System.err.println("Failed to load texture: " + path);
        }
        return texture;
    }

    public static void setIsOpen(boolean bIsOpen) {
        DebugPanel.bIsOpen.set(bIsOpen);
    }
}