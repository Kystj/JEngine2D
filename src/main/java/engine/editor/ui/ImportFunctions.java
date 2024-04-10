/*
 Title: AssetManager
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.serialization.SpriteSheetSerializer;
import engine.utils.ResourceUtils;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.Iterator;
import java.util.Map;

/**
 * The AssetManager class handles the import and removal of SpriteSheets,
 * storing them in a map along with associated asset types.
 */
public class ImportFunctions {

    // Input fields for the asset properties
    private ImString filePath = new ImString();   // Path to the sprite sheet file
    private ImString assetType = new ImString();  // Type of asset (e.g., character, item)
    private ImInt width = new ImInt();             // Width of individual sprites in the sheet
    private ImInt height = new ImInt();            // Height of individual sprites in the sheet
    private ImInt spacing = new ImInt();           // Spacing between sprites in the sheet
    private ImInt numSprites = new ImInt();        // Number of sprites in the sheet

    public boolean bFileErrorPopup = false;           // Flag that is used to generate an error alert related to the file path
    public boolean bTypeErrorPopup = false;           // Flag that is used to generate an error alert related to the type name
    public boolean bValErrorPopup = false;


    /**
     * Renders the input form for importing SpriteSheets using ImGui.
     */
    public void renderInputForm(ImBoolean isOpen) {

        int itemSpacing = 10;

        ImGui.begin("Import Form", isOpen);

        // Display input fields for asset properties
        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("File Path:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##FilePath", filePath, 256);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Type or Name:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##Type", assetType, 256);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Sprite Width:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##Width", width);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Sprite Height:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##Height", height);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Spacing:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##Spacing", spacing);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Number of Sprites:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##NumSprites", numSprites);

        // Submit button to add the SpriteSheet to the manager
        ImGui.setCursorPosX(itemSpacing);
        if (ImGui.button("Submit")) {
            // Check if file path is not empty before adding
            if (!filePath.isEmpty()) {
                addSpriteSheet();
            }
        }

        ImGui.end();
    }

    /**
     * Adds a new SpriteSheet based on the input form data and updates the map.
     */
    private void addSpriteSheet() {
        String filePath = String.valueOf(this.filePath);
        String type = String.valueOf(assetType);

        int spriteWidth = width.intValue();
        int spriteHeight = height.intValue();
        int spriteSpacing = spacing.intValue();
        int amount = numSprites.intValue();

        // Preform error checking before creating the new sprite sheet
        if (checkAndHandleErrors(filePath, type, spriteWidth, spriteHeight, amount)) {
            return;
        }

        // Add new SpriteSheet to the ResourceManager
        ResourceUtils.addSpriteSheet(filePath,
                new SpriteSheet(new Texture(filePath),
                        spriteWidth, spriteHeight,
                        spriteSpacing, type));

        // Save the sprite sheets info to a GSON file
        SpriteSheetSerializer.saveSpriteSheet(ResourceUtils.getSpriteSheet(filePath));

        // Update the map with the new SpriteSheet and its asset type
        ImportWindow.getSprite_Sheets().put(
                ResourceUtils.getSpriteSheet(filePath),
                type
        );

        // Refresh input fields after adding
        refreshFields();
    }

    /**
     * Renders the form for removing existing SpriteSheets using ImGui.
     */
    public void renderRemoveForm(ImBoolean isOpen) {
        ImGui.begin("Remove", isOpen);

        // Display a combo box with the list of existing SpriteSheets
        ImGui.setCursorPos(ImGui.getWindowSizeX() / 6, 50);
        if (ImGui.beginCombo("##MapKeysCombo", "Select SpriteSheet")) {
            // Iterate over existing SpriteSheets
            Iterator<Map.Entry<SpriteSheet, String>> iterator = ImportWindow.getSprite_Sheets().entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<SpriteSheet, String> entry = iterator.next();
                SpriteSheet key = entry.getKey();

                // Display selectable options for each SpriteSheet
                String filePath = key.getFilePathOfTexture();
                String option = key.getAssetType();

                // TODO: Change. This should get the file info dynamically
                String directory = "spritesheets";
                String fileType = "png";

                if (ImGui.selectable(option)) {
                    // Remove the selected SpriteSheet from the map
                    iterator.remove();
                    ResourceUtils.deleteFile(filePath);
                    ResourceUtils.removeGSONReferenceFile(filePath, directory, fileType);
                }
            }
            ImGui.endCombo();
        }
        ImGui.end();
    }


    private boolean checkAndHandleErrors(String fileName, String type, int spriteWidth, int spriteHeight, int amount) {
        // Prevents adding duplicate sprite sheets or sprite sheets with the same file path
        if (ImportWindow.getSprite_Sheets().get(ResourceUtils.getSpriteSheet(fileName)) != null) {
            bFileErrorPopup = true;
            refreshFields();
            return true;
        }

        // Prevents adding duplicate sprite sheets or sprite sheets with the same name
        for (String val : ImportWindow.getSprite_Sheets().values()) {
            if (val.equals(type)) {
                refreshFields();
                bTypeErrorPopup = true;
                return true;
            }
        }

        // Prevents adding sprite sheets without the correct fields
        if (spriteHeight < 1 || spriteWidth < 1 || amount < 1 || type.length() < 1 || fileName.length() < 1) {
            refreshFields();
            bValErrorPopup = true;
            return true;
        }

        return false;
    }


    private void refreshFields() {
        filePath = new ImString();
        assetType = new ImString();
        width = new ImInt();
        height = new ImInt();
        spacing = new ImInt();
        numSprites = new ImInt();
    }
}
/*End of AssetManager class*/