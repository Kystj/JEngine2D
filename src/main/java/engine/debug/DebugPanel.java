/*
 Title: DebugPanel
 Date: 2024-01-26
 Author: Kyle St John
 */
package engine.debug;

import engine.graphics.Texture;
import engine.managers.ReportManager;
import engine.managers.ResourceManager;
import engine.managers.ShortcutHandler;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Map;

import static engine.settings.EConstants.X_SPACING;

public class DebugPanel {

    private static boolean bIsOpen = true;
    private static final DebugReportUI reports = new DebugReportUI();


    public static void tick() {
        reports.tick();
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
                viewBugs();
                ImGui.setCursorPosX(X_SPACING);

                    if (ImGui.button("+")) {
                        reports.setGenerateNewReport(true);
                    }
                    if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip("Report new bug");
                    ImGui.endTooltip();
                }
            }

            if (ImGui.collapsingHeader("Help")) {
                ImGui.text("Debug Drawing:");
                ImGui.bulletText("Line");
                ImGui.bulletText("Square");
                ImGui.bulletText("Circle");
                ImGui.separator();
            }

            ImGui.end();
        }
    }

    private static void generateShapeButtons() {
        Texture square = ResourceManager.getOrCreateTexture("assets/buttons/Circle.png");
        Texture circle = ResourceManager.getOrCreateTexture("assets/buttons/Square.png");
        Texture triangle = ResourceManager.getOrCreateTexture("assets/buttons/Triangle.png");
        Texture line = ResourceManager.getOrCreateTexture("assets/buttons/Line.png");

        ArrayList<Texture> buttons = new ArrayList<>();
        buttons.add(line);
        buttons.add(square);
        buttons.add(circle);
        buttons.add(triangle);

        Vector2f[] texCoords  = new Vector2f[]{
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
                // TODO: Fix shapes
            }
            // Keep the buttons on the same line
            if (i <= 2) {
                ImGui.sameLine();
            }
            // Pop the unique ID for ImGui elements
            ImGui.popID();
        }
    }

    public static void viewBugs() {
        if (ImGui.beginCombo("##MapKeysCombo", "View reports")) {

            // Iterate over existing SpriteSheets
            for (Map.Entry<DebugReport, Boolean> entry : ReportManager.getReports().entrySet()) {
                DebugReport key = entry.getKey();

                // Display selectable options for each SpriteSheet
                String bugID = key.getBugID();
                String bugDescription = key.getBugDescription();

                if (ImGui.selectable(bugID)) {
                    ImGui.begin(bugID);
                    ReportManager.displayReport(bugID, bugDescription);
                }


                if (ImGui.isItemHovered()) {
                    ImGui.beginTooltip();
                    ImGui.setTooltip(bugDescription);
                    ImGui.endTooltip();
                }
            }
            ImGui.endCombo();
        }
    }

    public static void setIsOpen(boolean bIsOpen) {
        DebugPanel.bIsOpen = bIsOpen;
    }
}
/*End of DebugPanel class*/
