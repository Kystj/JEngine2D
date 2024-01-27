/*
 Title: DebugPanel
 Date: 2024-01-26
 Author: Kyle St John
 */
package engine.debug;

import engine.managers.ShortcutHandler;
import imgui.ImGui;

import static engine.settings.EConstants.X_SPACING;

public class DebugPanel {

    private static boolean bIsOpen = false;
    private static final ReportUI reports = new ReportUI();

    public static void init() {

    }

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

            if (ImGui.collapsingHeader("Debug Draw")) {
                ImGui.setCursorPosX(X_SPACING);
                if (ImGui.button("Line")) {
                    // TODO: Implement form for lines
                }

                ImGui.setCursorPosX(X_SPACING);
                if (ImGui.button("Square")) {
                    // TODO: Implement form for squares
                }

                ImGui.setCursorPosX(X_SPACING);
                if (ImGui.button("Circle")) {
                    // TODO: Implement forum for circle
                }
            }

            if (ImGui.collapsingHeader("Bug Reports")) {
                ImGui.setCursorPosX(X_SPACING);
                if (ImGui.button("New")) {
                    reports.setGenerateNewReport(true);
                }
                ImGui.setCursorPosX(X_SPACING);
                if (ImGui.button("Open")) {
                    reports.setLoadReport(true);
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


    public static void setIsOpen(boolean bIsOpen) {
        DebugPanel.bIsOpen = bIsOpen;
    }
}
/*End of DebugPanel class*/
