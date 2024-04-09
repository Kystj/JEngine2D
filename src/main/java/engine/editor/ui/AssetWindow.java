/*
 Title: AssetPanel
 Date: 2024-01-06
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.editor.controls.EditorControls;
import engine.graphics.SpriteSheet;
import engine.world.components.Sprite;
import engine.world.objects.GameObjFactory;
import engine.world.objects.GameObject;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.util.Map;

import static engine.utils.EConstants.X_SPACING;

public class AssetWindow {


    public void imgui() {

        // Create the main window
        ImGui.begin("Assets");
        ImGui.setCursorPosX(X_SPACING);
        // Create tabs to represent different asset types
        if (ImGui.beginTabBar("Tabs")) {
            // Iterate through each SpriteSheet in the AssetManager
            for (Map.Entry<SpriteSheet, String> entry : ImportWindow.Import_Info.getSpriteSheets().entrySet()) {
                SpriteSheet spriteSheet = entry.getKey();
                String tabName = entry.getValue();

                // Begin a tab item for the current SpriteSheet
                if (ImGui.beginTabItem(tabName)) {
                    // Populate buttons or other controls in the second column for the current SpriteSheet
                    generateAssetButtons(spriteSheet);

                    // Reset to a single column after the second column
                    ImGui.columns(1);

                    // End the current tab item
                    ImGui.endTabItem();
                }
            }
            // End the tab bar
            ImGui.endTabBar();
        }
        // End the main window
        ImGui.end();
    }


    private void generateAssetButtons(SpriteSheet sprites) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        // Calculate the rightmost position of the ImGui window
        float windowX2 = windowPos.x + windowSize.x;

        // Iterate through each sprite in the SpriteSheet
        ImGui.setCursorPosX(X_SPACING);
        for (int i = 0; i < sprites.numOfSprites(); i++) {
            Sprite sprite = sprites.getSprite(i);

            float spriteWidth = sprites.getSpriteWidth() * 2;
            float spriteHeight = sprites.getSpriteHeight() * 2;

            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getUvCoordinates();

            // Push a unique ID for the ImGui elements
            ImGui.pushID(i);

            // Create an image button for the current sprite
            if (ImGui.imageButton(id, 32, 32, texCoords[0].x, texCoords[2].y, texCoords[2].x, texCoords[0].y)) {

                Sprite temp = new Sprite();
                temp.setTexture(sprite.getSpriteTexture());
                temp.setUvCoordinates(sprite.getUvCoordinates());
                GameObject obj = GameObjFactory.generateGameObject(temp, 32, 32);
                EditorControls.setActiveAsset(obj);
            }

            // Pop the unique ID for ImGui elements
            ImGui.popID();

            // Calculate the rightmost position of the last button
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;

            // Calculate the rightmost position of the next button
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;

            // If there are more sprites and there's enough space, place the next button on the same line
            if (i + 1 < sprites.numOfSprites() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            } else {
                ImGui.setCursorPosX(X_SPACING);
            }
        }
    }
}
/*End of AssetPanel class*/