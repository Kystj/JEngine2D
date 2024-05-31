/*
 Title: ImportUtils
 Date: 2024-04-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.graphics.SpriteSheet;
import engine.serialization.SpriteSheetSerializer;
import engine.utils.engine.ResourceUtils;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Map;

import static engine.utils.engine.EConstants.X_SPACING;

public class ImportWindow {

    private static final ImBoolean bIsOpen = new ImBoolean(true);
    protected final static ImportFunctions Import_Functions = new ImportFunctions();
    public static final ImBoolean bIsImportOpen = new ImBoolean(false);
    private final ImBoolean bIsRemoveOpen = new ImBoolean(false);
    private static final Map<SpriteSheet, String> Sprite_Sheets = new HashMap<>();

    private final Vector2f[] texCords = new Vector2f[] {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public ImportWindow() {
        // Load existing sprite sheets from storage
        SpriteSheetSerializer.loadSpriteSheets(Sprite_Sheets);
    }


    public void imgui() {
        checkForErrors();

        if (bIsImportOpen.get()) {
            Import_Functions.renderInputForm(bIsImportOpen);
        }

        // Check if the remove form is open and close it if the escape key is pressed
        if (bIsRemoveOpen.get()) {
            Import_Functions.renderRemoveForm(bIsRemoveOpen);
        }

        if (bIsOpen.get()) {
            ImGui.begin("Assets", bIsOpen, ImGuiWindowFlags.MenuBar);
            ImGui.beginMenuBar();
            if (ImGui.menuItem("Import")) {
                ImportWindow.bIsImportOpen.set(true);
            }
            if (ImGui.menuItem("Remove")) {
                bIsRemoveOpen.set(true);
            }
            ImGui.endMenuBar();
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
                ContentWindow.getSprite_Sheets().put(spriteSheet, spriteSheet.getAssetType());
            }

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.image(id, 128, 128, texCords[0].x, texCords[2].y, texCords[2].x, texCords[0].y);
                ImGui.endTooltip();
            }
            ImGui.sameLine();
        }
    }

    private void checkForErrors() { // TODO: Fix this
/*        // Perform a safety check for file error popup
        if (Import_Functions.bFileErrorPopup) {
            Import_Functions.bFileErrorPopup = ImGuiUtils.activatePopup("File Path Already In Use.\nPlease choose a different file path.");
        }

        // Perform a safety check for type error popup
        if (Import_Functions.bTypeErrorPopup) {
            Import_Functions.bTypeErrorPopup = ImGuiUtils.activatePopup("Type Name Already In Use.\nPlease choose a different name.");
        }

        // Perform a safety check for type error popup
        if (Import_Functions.bValErrorPopup) {
            Import_Functions.bValErrorPopup = ImGuiUtils.activatePopup("Sprite extraction values cannot be 0.\n");
        }*/
    }


    public static void setIsOpen(boolean isOpen) {
        bIsOpen.set(isOpen);
    }

    public static Map<SpriteSheet, String> getSprite_Sheets() {
        return Sprite_Sheets;
    }
}
/*End of ImportUtils class*/
