/*
 Title: AssetPanel
 Date: 2024-01-06
 Author: Kyle St John
 */
package engine.editor;

import engine.components.Sprite;
import engine.graphics.SpriteSheet;
import engine.managers.ShortcutHandler;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.util.List;

public class AssetPanel {

    private final String[] tabNames = {"Terrain", "Pawns"};
    private final List<SpriteSheet> spriteSheets;
    private final ImportSpriteSheet spriteSheetImporter = new ImportSpriteSheet();
    private boolean bIsImportOpen = false;
    private boolean bIsRemoveOpen = false;
    private boolean bShowOptions = true;


    public AssetPanel(List<SpriteSheet> spriteSheet) {
        spriteSheets = spriteSheet;
    }

    // Update ImGui and the various tabs representing asset types
    public void tick() {
        render();
    }

    private void render() {
        if (bIsImportOpen) {
            bIsImportOpen = ShortcutHandler.closeWithEscape();
            spriteSheetImporter.renderInputForm();
        }

        if (bIsRemoveOpen) {
            bIsRemoveOpen = ShortcutHandler.closeWithEscape();
            spriteSheetImporter.renderRemoveForm();
        }

        // Create a window
        ImGui.begin("Assets");
        ImGui.spacing();

        // Create tabs
        // TODO: Create a system that allows users to create tabs and populate them from within the editor
        if (ImGui.beginTabBar("Tabs")) {
            SpriteSheet tempSheet;

            for (String tabName : tabNames) {
                switch (tabName) {
                    case "Terrain":
                        tempSheet = spriteSheets.get(0);
                        break;
                    case "Pawns":
                        tempSheet = spriteSheets.get(1);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + tabName);
                }

                if (ImGui.beginTabItem(tabName)) {

                    if (bShowOptions) {
                        updateAssetDropdown();

                        // Second column
                        ImGui.nextColumn();

                        // Populate buttons or other controls in the second column
                        populateAssetButtons(tempSheet);

                        // End columns
                        ImGui.columns(1);  // Reset to a single column

                        // End the tab item
                        ImGui.endTabItem();
                    }
                }
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }

    private void updateAssetDropdown() {
        //Set up columns
        ImGui.columns(2, "Columns", true);

        // First column
        ImGui.setColumnWidth(0, 150);  // Adjust the width as needed
        ImGui.text(" ");
        ImGui.setCursorPos(25,70);

        if (ImGui.beginCombo("##combo", "+/-")) {
            if (ImGui.selectable("Import")) {
                bIsRemoveOpen = false;
                bIsImportOpen = true;
            }

            if (ImGui.selectable("Remove")) {
                bIsImportOpen = false;
                bIsRemoveOpen = true;
            }
            ImGui.endCombo();
        }
    }


    private void populateAssetButtons(SpriteSheet sprites) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.numOfSprites(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprites.spriteWidth() * 2;
            float spriteHeight = sprites.getSpriteHeight() * 2;
            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getUvCoordinates();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                selectTargetAsset();
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.numOfSprites() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }
    }

    private void selectTargetAsset() {
        handleAssetDrag();
        handleAssetDrop();
    }

    private void handleAssetDrag() {

    }

    private void handleAssetDrop() {

    }
}
/*End of AssetPanel class*/