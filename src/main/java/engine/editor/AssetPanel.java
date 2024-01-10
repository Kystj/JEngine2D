/*
 Title: AssetPanel
 Date: 2024-01-06
 Author: Kyle St John
 */
package engine.editor;

import engine.components.Sprite;
import engine.graphics.SpriteSheet;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.List;

public class AssetPanel {

    private final String[] tabNames = {"Terrain", "Pawns"};
        List<SpriteSheet> spriteSheets;

    public AssetPanel(List<SpriteSheet> spriteSheet) {
        spriteSheets = spriteSheet;
    }

    // Update ImGui and the various tabs representing asset types
    public void tick() {
        render();
    }

    private void render() {
        // Create a window
        ImGui.begin("Assets");

        // Create tabs
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
                if (ImGui.beginTabItem(tabName, new ImBoolean(true), 0)) {
                    populateAssetButtons(tempSheet);
                    ImGui.endTabItem();
                }
            }
            ImGui.endTabBar();
        }
        // End the window
        ImGui.end();
    }


    private void populateAssetButtons(SpriteSheet sprites) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.numOfSprites(); i++) {
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
