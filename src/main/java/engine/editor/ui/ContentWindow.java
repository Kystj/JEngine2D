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
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiPopupFlags;
import imgui.type.ImBoolean;
import org.joml.Vector2f;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static engine.utils.engine.EConstants.X_SPACING;

public class ContentWindow {

    private static final Map<SpriteSheet, String> SPRITE_SHEETS = new HashMap<>();
    private final ImBoolean isOpen = new ImBoolean(false);
    private boolean showTabContextMenu = false;
    public boolean openInAnimationEditor = false;

    public void imgui() {

        // Create the main window
        if (isOpen.get()) {
            ImGui.begin("Content", isOpen);
            ImGui.setCursorPosX(X_SPACING);
            // Create tabs to represent different asset types
            if (ImGui.beginTabBar("Tabs")) {
                // Create an iterator for the Sprite_Sheets map
                Iterator<Map.Entry<SpriteSheet, String>> iterator = SPRITE_SHEETS.entrySet().iterator();
                while (iterator.hasNext()) {
                    // Get the next entry
                    Map.Entry<SpriteSheet, String> entry = iterator.next();
                    SpriteSheet spriteSheet = entry.getKey();
                    String tabName = entry.getValue();

                    // Begin a tab item for the current SpriteSheet
                    if (ImGui.beginTabItem(tabName)) {
                        if (ImGui.isItemClicked(ImGuiMouseButton.Right)) {
                            showTabContextMenu = true;
                        }

                        if (!ImGui.isItemHovered()) {
                            showTabContextMenu = false;
                        }
                        if (showTabContextMenu()) {
                            iterator.remove();
                        }
                        // Populate buttons or other controls in the second column for the current SpriteSheet
                        generateAssetButtons(spriteSheet);

                        // Reset to a single column after the second column
                        ImGui.columns(1);

                        // End the current tab item
                        ImGui.endTabItem();
                    }
                }
                ImGui.endTabBar();
            }
            // End the main window
            ImGui.end();
        }

        if (openInAnimationEditor) {
            openInAnimationEditor = isOpen.get();
        }
    }

    private boolean showTabContextMenu() {
        boolean removeFlag = false;
        if (showTabContextMenu) {
            ImGui.openPopup("TabContextMenu");
        }
        if (ImGui.beginPopup("TabContextMenu", ImGuiPopupFlags.MouseButtonRight)) {
            if (ImGui.menuItem("Delete")) {
                removeFlag = true;
            }
            ImGui.endPopup();
        }
        return removeFlag;
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

    public static Map<SpriteSheet, String> getSpriteSheets() {
        return SPRITE_SHEETS;
    }


    public void setIsOpen(boolean isOpen) {
        this.isOpen.set(isOpen);
    }
}
/*End of ContentWindow class*/