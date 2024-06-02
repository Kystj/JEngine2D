/*
 Title: ImportUtils
 Date: 2024-04-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debugging.info.Logger;
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
import static engine.utils.imgui.ImGuiUtils.renderRightClickContext;

public class AssetWindow {

    private static final ImBoolean IS_ASSET_WIN_OPEN = new ImBoolean(true);
    protected final static ImportFunctions IMPORT_FUNCTIONS = new ImportFunctions();
    public static final ImBoolean IS_IMPORT_WIN_OPEN = new ImBoolean(false);
    private static final Map<SpriteSheet, String> SPRITE_SHEETS = new HashMap<>();

    private static final Vector2f[] texCords = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public AssetWindow() {
        // Load existing sprite sheets from storage
        SpriteSheetSerializer.loadSpriteSheets(SPRITE_SHEETS);
    }


    public static void imgui() {
        //checkForErrors();

        if (IS_IMPORT_WIN_OPEN.get()) {
            IMPORT_FUNCTIONS.renderInputForm(IS_IMPORT_WIN_OPEN, SPRITE_SHEETS);
        }

        if (IS_ASSET_WIN_OPEN.get()) {
            ImGui.begin("Assets", IS_ASSET_WIN_OPEN, ImGuiWindowFlags.MenuBar);
            ImGui.beginMenuBar();
            if (ImGui.menuItem("Import")) {
                AssetWindow.IS_IMPORT_WIN_OPEN.set(true);
            }


            ImGui.endMenuBar();
            iterateSpriteSheets();
            ImGui.end();
        }
    }


    public static void iterateSpriteSheets() {
        Map<String, SpriteSheet> spriteSheets = ResourceUtils.getSpriteSheets();

        ImGui.spacing();
        ImGui.setCursorPosX(X_SPACING);
        for (Map.Entry<String, SpriteSheet> entry : spriteSheets.entrySet()) {
            String key = entry.getKey();
            SpriteSheet spriteSheet = entry.getValue();
            int id = spriteSheet.getSprite(0).getTextureID();

            if (ImGui.imageButton(id, 32, 32, texCords[0].x, texCords[2].y, texCords[2].x, texCords[0].y)) {
                ContentWindow.getSpriteSheets().put(spriteSheet, spriteSheet.getAssetType());
            }

            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.image(id, 128, 128, texCords[0].x, texCords[2].y, texCords[2].x, texCords[0].y);
                ImGui.endTooltip();
            }

            renderRightClickContext(" Delete");
        }
    }

    private void checkForErrors() { //TODO: FIX THIS
        // Perform a safety check for file error popup
        if (IMPORT_FUNCTIONS.bFileErrorPopup) {
            Logger.error("File Path Already In Use.\nPlease choose a different file path.");
        }

        // Perform a safety check for type error popup
        if (IMPORT_FUNCTIONS.bTypeErrorPopup) {
            Logger.error("Type Name Already In Use.\nPlease choose a different name.");
        }

        // Perform a safety check for type error popup
        if (IMPORT_FUNCTIONS.bValErrorPopup) {
            Logger.error("Sprite extraction values cannot be 0.\n");
        }
    }


    public static void setIsAssetWinOpen(boolean isAssetWinOpen) {
        IS_ASSET_WIN_OPEN.set(isAssetWinOpen);
    }

    public static Map<SpriteSheet, String> getSpriteSheets() {
        return SPRITE_SHEETS;
    }
}
/*End of ImportUtils class*/