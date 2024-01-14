/*
 Title: ImportSpriteSheet
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.managers;

import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.serialization.SpriteSheetSerializer;
import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetManager {

    private ImString filePath = new ImString();
    private ImString assetType = new ImString();
    private ImInt width = new ImInt();
    private ImInt height = new ImInt();
    private ImInt spacing = new ImInt();
    private ImInt numSprites = new ImInt();

    private final Map<SpriteSheet, String> spriteSheets = new HashMap<>();

    public AssetManager() {
        SpriteSheetSerializer.loadSpriteSheets(spriteSheets);
    }

    public void renderInputForm() {
        int itemSpacing = 10;

        ImGui.begin("Import");

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("File Path:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##FilePath", filePath, 256);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Asset Type:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##AssetType", assetType, 256);

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

        ImGui.setCursorPosX(itemSpacing);
        if (ImGui.button("Submit")) {
            if (!filePath.isEmpty()) {
                addSpriteSheet();
            }
        }
        ImGui.end();
    }

    public void addSpriteSheet() {
        String fileName = String.valueOf(filePath);
        String type = String.valueOf(assetType);
        System.out.println("Asset type: " + type);

        int spriteWidth = width.intValue();
        int spriteHeight = height.intValue();
        int spriteSpacing = spacing.intValue();
        int amount = numSprites.intValue();

        ResourceManager.addSpriteSheet(fileName,
                new SpriteSheet(new Texture(fileName),
                        spriteWidth, spriteHeight,
                        spriteSpacing, amount, type));

        SpriteSheetSerializer.saveSpriteSheet(ResourceManager.getSpriteSheet(fileName));

        spriteSheets.put(
                ResourceManager.getSpriteSheet(fileName),
                        type
        );

        refreshImportFields();
    }

    public void renderRemoveForm() {
        ImGui.begin("Remove");

        ImGui.setCursorPos(ImGui.getWindowSizeX() / 6, 50);
        if (ImGui.beginCombo("##MapKeysCombo", "Select SpriteSheet")) {
            Iterator<Map.Entry<SpriteSheet, String>> iterator = spriteSheets.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<SpriteSheet, String> entry = iterator.next();
                SpriteSheet key = entry.getKey();
                String value = entry.getValue();

                String option = key.getFilePathOfTexture();

                if (ImGui.selectable(option)) {
                    // Remove the selected SpriteSheet from the map
                    iterator.remove();
                }
            }
            ImGui.endCombo();
        }
        ImGui.end();
    }

    private void refreshImportFields() {
        filePath = new ImString();
        assetType = new ImString();
        width = new ImInt();
        height = new ImInt();
        spacing = new ImInt();
        numSprites = new ImInt();
    }

    public Map<SpriteSheet, String> getSpriteSheets() {
        return spriteSheets;
    }
}
/*End of ImportSpriteSheet class*/