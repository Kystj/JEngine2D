/*
 Title: AssetPanel
 Date: 2024-01-06
 Author: Kyle St John
 */
package engine.editor;

import engine.components.Sprite;
import engine.graphics.SpriteSheet;
import engine.managers.AssetManager;
import engine.managers.ShortcutHandler;
import engine.widgets.ImGuiCustom;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import java.util.Map;

/**
 * The AssetPanel class manages the display and interaction with assets in the editor,
 * including importing, removing, and rendering tabs for various asset types.
 */
public class AssetPanel {

    private final AssetManager assetManager = new AssetManager();
    private boolean bIsImportOpen = false;
    private boolean bIsRemoveOpen = false;

    /**
     * Updates ImGui and the various tabs representing asset types.
     */
    public void tick() {
        render();
    }

    /**
     * Renders the AssetPanel, including the main window, import and remove forms,
     * asset dropdown, and tabs for different asset types.
     */
    private void render() {
        // Check the Asset Managers state
        handleConditionsAndForms();

        // Create the main window
        ImGui.begin("Assets");
        ImGui.spacing();

        // Update the asset dropdown for adding or removing assets
        updateAssetDropdown();

        // Move to the second column for organizing UI elements
        ImGui.nextColumn();

        // Create tabs to represent different asset types
        if (ImGui.beginTabBar("Tabs")) {
            // Iterate through each SpriteSheet in the AssetManager
            for (Map.Entry<SpriteSheet, String> entry : assetManager.getSpriteSheets().entrySet()) {
                SpriteSheet spriteSheet = entry.getKey();
                String tabName = entry.getValue();

                // Begin a tab item for the current SpriteSheet
                if (ImGui.beginTabItem(tabName)) {
                    // Populate buttons or other controls in the second column for the current SpriteSheet
                    populateAssetButtons(spriteSheet);

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

    /**
     * Updates the asset dropdown, allowing the user to add or remove assets.
     */
    private void updateAssetDropdown() {
        //Set up columns
        ImGui.columns(2, "Columns", true);

        // First column
        ImGui.setColumnWidth(0, 150);
        ImGui.text(" ");
        ImGui.setCursorPos(25,50);

        if (ImGui.beginCombo("##combo", "+/-")) {
            // Add new sprite sheet
            if (ImGui.selectable("Add")) {
                bIsRemoveOpen = false;
                bIsImportOpen = true;
            }
            // Remove existing sprite sheer
            if (ImGui.selectable("Remove")) {
                bIsImportOpen = false;
                bIsRemoveOpen = true;
            }
            ImGui.endCombo();
        }
    }

    /**
     * Populates the AssetPanel with buttons representing individual assets in a SpriteSheet.
     *
     * @param sprites The SpriteSheet containing the assets.
     */
    private void populateAssetButtons(SpriteSheet sprites) {
        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        // Calculate the rightmost position of the ImGui window
        float windowX2 = windowPos.x + windowSize.x;

        // Iterate through each sprite in the SpriteSheet
        for (int i = 0; i < sprites.numOfSprites(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprites.getSpriteWidth() * 2;
            float spriteHeight = sprites.getSpriteHeight() * 2;
            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getUvCoordinates();

            // Push a unique ID for the ImGui elements
            ImGui.pushID(i);

            // Create an image button for the current sprite
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                // Handle the selection of the current asset
                selectTargetAsset();
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
            }
        }
    }

    /**
     * Checks for specific conditions and takes appropriate actions,
     * including handling error popups and closing import/remove forms.
     */
    private void handleConditionsAndForms() {
        // Perform a safety check for file error popup
        if (assetManager.bFileErrorPopup) {
            assetManager.bFileErrorPopup = ImGuiCustom.activatePopup("File Path Already In Use.\nPlease choose a different file path.");
        }

        // Perform a safety check for type error popup
        if (assetManager.bTypeErrorPopup) {
            assetManager.bTypeErrorPopup = ImGuiCustom.activatePopup("Type Name Already In Use.\nPlease choose a different name.");
        }

        // Perform a safety check for type error popup
        if (assetManager.bValErrorPopup) {
            assetManager.bValErrorPopup = ImGuiCustom.activatePopup("Sprite extraction values cannot be 0.\n");
        }

        // Check if the import form is open and close it if the escape key is pressed
        if (bIsImportOpen) {
            bIsImportOpen = ShortcutHandler.closeWithEscape();
            assetManager.renderInputForm();
        }

        // Check if the remove form is open and close it if the escape key is pressed
        if (bIsRemoveOpen) {
            bIsRemoveOpen = ShortcutHandler.closeWithEscape();
            assetManager.renderRemoveForm();
        }
    }

    /**
     * Handles the selection of a target asset, including drag-and-drop interactions.
     */
    private void selectTargetAsset() {
        handleAssetDrag();
        handleAssetDrop();
    }

    /**
     * Handles the dragging of assets within the AssetPanel.
     */
    private void handleAssetDrag() {

    }

    /**
     * Handles the dropping of assets onto the AssetPanel.
     */
    private void handleAssetDrop() {

    }
}
/*End of AssetPanel class*/