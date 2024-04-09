/*
 Title: ImportUtils
 Date: 2024-04-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debug.info.DebugLogger;
import engine.graphics.SpriteSheet;
import engine.utils.ImGuiUtils;
import engine.utils.ResourceUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.Map;

import static engine.utils.EConstants.*;

public class ImportWindow {

    private static final ImBoolean bIsOpen = new ImBoolean(false);
    protected final static ImportForms Import_Info = new ImportForms();
    public static final ImBoolean bIsImportOpen = new ImBoolean(false);
    private final ImBoolean bIsRemoveOpen = new ImBoolean(false);

    private final Vector2f[] texCords = new Vector2f[] {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };


    public void imgui() {
        checkForErrors();

        if (bIsImportOpen.get()) {
            Import_Info.renderInputForm(bIsImportOpen);
        }

        // Check if the remove form is open and close it if the escape key is pressed
        if (bIsRemoveOpen.get()) {
            Import_Info.renderRemoveForm(bIsRemoveOpen);
        }

        if (bIsOpen.get()) {
            ImGui.begin("Import", bIsOpen);
            ImGui.spacing();
            ImGui.setCursorPosX(X_SPACING);
            ImGui.pushStyleColor(ImGuiCol.Button, RED_BUTTON.x, RED_BUTTON.y, RED_BUTTON.z, RED_BUTTON.w);
            if (ImGui.button("-")) {
                bIsRemoveOpen.set(true);
            }
            ImGui.popStyleColor();

            iterateSpriteSheets();
            ImGui.end();
        }
    }




    public void iterateSpriteSheets() {
        Map<String, SpriteSheet> spriteSheets = ResourceUtils.getSpriteSheets();

        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);
        for (Map.Entry<String, SpriteSheet> entry : spriteSheets.entrySet()) {
            String key = entry.getKey();
            SpriteSheet spriteSheet = entry.getValue();
            int id = spriteSheet.getSprite(0).getTextureID();

            if (ImGui.imageButton(id, 32, 32, texCords[0].x, texCords[2].y, texCords[2].x, texCords[0].y)) {
                DebugLogger.info("Pressed");
            }

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.image(id, 128, 128, texCords[0].x, texCords[2].y, texCords[2].x, texCords[0].y);
                ImGui.endTooltip();
            }
            ImGui.sameLine();
        }
    }

    private void checkForErrors() {
        // Perform a safety check for file error popup
        if (Import_Info.bFileErrorPopup) {
            Import_Info.bFileErrorPopup = ImGuiUtils.activatePopup("File Path Already In Use.\nPlease choose a different file path.");
        }

        // Perform a safety check for type error popup
        if (Import_Info.bTypeErrorPopup) {
            Import_Info.bTypeErrorPopup = ImGuiUtils.activatePopup("Type Name Already In Use.\nPlease choose a different name.");
        }

        // Perform a safety check for type error popup
        if (Import_Info.bValErrorPopup) {
            Import_Info.bValErrorPopup = ImGuiUtils.activatePopup("Sprite extraction values cannot be 0.\n");
        }
    }


    public static void setIsOpen(boolean isOpen) {
        bIsOpen.set(isOpen);
    }
}
/*End of ImportUtils class*/
