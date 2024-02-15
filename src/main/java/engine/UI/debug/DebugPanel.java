/*
 Title: DebugPanel
 Date: 2024-01-26
 Author: Kyle St John
 */
package engine.UI.debug;

import engine.graphics.Texture;
import engine.handlers.ReportHandler;
import engine.handlers.ResourceHandler;
import engine.handlers.ShortcutHandler;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;

import static engine.UI.settings.EConstants.X_SPACING;

public class DebugPanel {

    private static boolean bIsOpen = true;
    private static final ReportPanel REPORT_PANEL = new ReportPanel();

    private static final Texture square = ResourceHandler.getOrCreateTexture("assets/buttons/Circle.png");
    private static final Texture circle = ResourceHandler.getOrCreateTexture("assets/buttons/Square.png");
    private static final Texture triangle = ResourceHandler.getOrCreateTexture("assets/buttons/Triangle.png");
    private static final Texture line = ResourceHandler.getOrCreateTexture("assets/buttons/Line.png");

    private static ShapeGenerator SHAPE_GENERATOR = new ShapeGenerator();



    public static void tick() {
        REPORT_PANEL.tick();
        imgui();
    }

    public static void imgui() {
        if (bIsOpen) {

            bIsOpen = ShortcutHandler.closeWithEscape();

            ImGui.begin("Debug");
            ImGui.spacing();
            if (ImGui.collapsingHeader("Configurations")) {
                ImGui.setCursorPosX(X_SPACING);
                ImGui.bulletText("Grid Size");

                ImGui.setCursorPosX(X_SPACING);
                ImGui.bulletText("Clear Color");

                ImGui.setCursorPosX(X_SPACING);
                ImGui.bulletText("Wireframe mode");
            }
            if (ImGui.collapsingHeader("Draw")) {
                generateShapeButtons();
            }

            if (ImGui.collapsingHeader("Reports")) {
                ReportHandler.viewBugs();
                ImGui.setCursorPosX(X_SPACING);

                if (ImGui.button("+")) {
                    REPORT_PANEL.setGenerateNewReport(true);
                }
                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip("Report new bug");
                    ImGui.endTooltip();
                }
            }

            if (ImGui.collapsingHeader("Help")) {
                ImGui.text("Debug Drawing");
                ImGui.bulletText("Line");
                ImGui.bulletText("Square");
                ImGui.bulletText("Circle");
                ImGui.separator();
            }
            ImGui.end();
        }
    }

    private static void generateShapeButtons() {
        ArrayList<Texture> buttons = new ArrayList<>();
        buttons.add(line);
        buttons.add(square);
        buttons.add(circle);
        buttons.add(triangle);

        Vector2f[] texCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };


        // Iterate through each sprite in the SpriteSheet
        for (int i = 0; i < buttons.size(); i++) {

            int id = buttons.get(i).getTextureID();

            ImGui.pushID(i);

            if (ImGui.imageButton(id, 16, 16, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                switch (i) {
                    case 0:
                        SHAPE_GENERATOR = new LineGenerator();
                        break;
                    case 1:
                        SHAPE_GENERATOR = new CircleGenerator();
                        break;
                    case 2:
                        SHAPE_GENERATOR = new BoxGenerator();
                        break;
                    case 3:
                        SHAPE_GENERATOR = new TriangleGenerator();
                        break;
                }
            }
            // Keep the buttons on the same line
            if (i <= 2) {
                ImGui.sameLine();
            }
            // Pop the unique ID for ImGui elements
            ImGui.popID();
        }
        SHAPE_GENERATOR.drawUI();
    }

    public static void setIsOpen(boolean bIsOpen) {
        DebugPanel.bIsOpen = bIsOpen;
    }
}
/*End of DebugPanel class*/
