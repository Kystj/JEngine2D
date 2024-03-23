/*
 Title: AssetManager
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.utils;

import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.serialization.SpriteSheetSerializer;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The AssetManager class handles the import and removal of SpriteSheets,
 * storing them in a map along with associated asset types.
 */
public class AssetUtils {

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

    // Map to store SpriteSheet objects and their associated asset types
    private final Map<SpriteSheet, String> spriteSheets = new HashMap<>();

    /**
     * Initializes the AssetManager by loading existing SpriteSheets from storage.
     */
    public AssetUtils() {
        // Load existing sprite sheets from storage
        SpriteSheetSerializer.loadSpriteSheets(spriteSheets);
    }

    /**
     * Renders the input form for importing SpriteSheets using ImGui.
     */
    public void renderInputForm() {
        int itemSpacing = 10;

        ImGui.begin("Import");

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
                        spriteSpacing, amount, type));

        // Save the sprite sheets info to a GSON file
        SpriteSheetSerializer.saveSpriteSheet(ResourceUtils.getSpriteSheet(filePath));

        // Update the map with the new SpriteSheet and its asset type
        spriteSheets.put(
                ResourceUtils.getSpriteSheet(filePath),
                type
        );

        // Refresh input fields after adding
        refreshFields();
    }

    /**
     * Renders the form for removing existing SpriteSheets using ImGui.
     */
    public void renderRemoveForm() {
        ImGui.begin("Remove");

        // Display a combo box with the list of existing SpriteSheets
        ImGui.setCursorPos(ImGui.getWindowSizeX() / 6, 50);
        if (ImGui.beginCombo("##MapKeysCombo", "Select SpriteSheet")) {
            // Iterate over existing SpriteSheets
            Iterator<Map.Entry<SpriteSheet, String>> iterator = spriteSheets.entrySet().iterator();

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
                    // TODO: Change. Should be optional to full delete or hide
                    ResourceUtils.deleteFile(filePath);
                    ResourceUtils.removeGSONReferenceFile(filePath, directory, fileType);
                }
            }
            ImGui.endCombo();
        }
        ImGui.end();
    }


    /**
     * Checks for conditions that would prevent adding a sprite sheet and handles error cases.
     *
     * @param fileName     The file path of the sprite sheet.
     * @param type         The name/type of the sprite sheet.
     * @param spriteWidth  The width of each sprite in the sprite sheet.
     * @param spriteHeight The height of each sprite in the sprite sheet.
     * @param amount       The number of sprites in the sprite sheet.
     * @return {@code true} if conditions are met that prevent adding the sprite sheet,
     *         {@code false} otherwise.
     */
    private boolean checkAndHandleErrors(String fileName, String type, int spriteWidth, int spriteHeight, int amount) {
        // Prevents adding duplicate sprite sheets or sprite sheets with the same file path
        if (spriteSheets.get(ResourceUtils.getSpriteSheet(fileName)) != null) {
            bFileErrorPopup = true;
            refreshFields();
            return true;
        }

        // Prevents adding duplicate sprite sheets or sprite sheets with the same name
        for (String val : spriteSheets.values()) {
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

    /**
     * Helper method to refresh input fields after adding a SpriteSheet.
     */
    private void refreshFields() {
        filePath = new ImString();
        assetType = new ImString();
        width = new ImInt();
        height = new ImInt();
        spacing = new ImInt();
        numSprites = new ImInt();
    }

    /**
     * Retrieves the map of SpriteSheets and their associated asset types.
     *
     * @return The map of SpriteSheets and asset types.
     */
    public Map<SpriteSheet, String> getSpriteSheets() {
        return spriteSheets;
    }
}
/*End of AssetManager class*/