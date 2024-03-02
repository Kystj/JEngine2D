package engine.ui.debug;

import engine.debug.DebugRenderer;
import engine.graphics.Texture;
import engine.utils.ReportHandler;
import engine.utils.ResourceHandler;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.ArrayList;

import static engine.ui.settings.EConstants.X_SPACING;

public class DebugPanel {

    private static final ImBoolean bIsOpen = new ImBoolean(true);
    private static final ReportPanel REPORT_PANEL = new ReportPanel();
    private static ShapeGenerator SHAPE_GENERATOR = new LineGenerator();

    private static final Texture squareButton = loadTexture("assets/buttons/Square.png");
    private static final Texture circleButton = loadTexture("assets/buttons/Circle.png");
    private static final Texture triangleButton = loadTexture("assets/buttons/Triangle.png");
    private static final Texture lineButton = loadTexture("assets/buttons/Line.png");
    private static final Texture refreshButton = loadTexture("assets/buttons/Refresh.png");

    public static void tick() {
        REPORT_PANEL.tick();
    }

    public static void imgui() {
        if (bIsOpen.get()) {
            ImGui.begin("Debug", bIsOpen);
            ImGui.spacing();
            drawConfigurations();
            drawShapes();
            drawReports();
            drawHelp();
            ImGui.end();
        }
    }

    private static void drawConfigurations() {
        if (ImGui.collapsingHeader("Config")) {
            ImGui.setCursorPosX(X_SPACING);
            ImGui.bulletText("Grid Size");

            ImGui.setCursorPosX(X_SPACING);
            ImGui.bulletText("Clear Color");

            ImGui.setCursorPosX(X_SPACING);
            ImGui.bulletText("Wireframe mode");
        }
    }

    private static void drawShapes() {
        if (ImGui.collapsingHeader("Draw")) {
            generateShapeButtons();
            SHAPE_GENERATOR.drawUI();
        }
    }

    private static void drawReports() {
        if (ImGui.collapsingHeader("Reports")) {
            ReportHandler.viewBugs();
            ImGui.setCursorPosX(X_SPACING);

            ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 1.0f, 0.0f, 0.5f);
            if (ImGui.button("+")) {
                REPORT_PANEL.setGenerateNewReport(true);
            }
            ImGui.popStyleColor();

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.setTooltip("Report new bug");
                ImGui.endTooltip();
            }
        }

        if (ReportHandler.isShowReport()) {
            ReportHandler.displayReport();
        }
    }

    private static void drawHelp() {
        if (ImGui.collapsingHeader("Help")) {
            ImGui.text("Debug Drawing");
            ImGui.bulletText("Line");
            ImGui.bulletText("Square");
            ImGui.bulletText("Circle");
            ImGui.separator();
        }
    }

    private static void generateShapeButtons() {
        ArrayList<Texture> buttons = new ArrayList<>();
        buttons.add(lineButton);
        buttons.add(squareButton);
        buttons.add(circleButton);
        buttons.add(triangleButton);

        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

        ImGui.setCursorPosX(X_SPACING);
        if (ImGui.imageButton(refreshButton.getTextureID(), 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
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
            ImGui.setCursorPosX(X_SPACING + i * ImGui.getWindowSizeX() / 6);
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
        Texture texture = ResourceHandler.getOrCreateTexture(path);
        if (texture == null) {
            System.err.println("Failed to load texture: " + path);
        }
        return texture;
    }

    public static void setIsOpen(boolean bIsOpen) {
        DebugPanel.bIsOpen.set(bIsOpen);
    }
}