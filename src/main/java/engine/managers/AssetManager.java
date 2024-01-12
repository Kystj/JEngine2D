/*
 Title: ImportSpriteSheet
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.managers;

import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
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
    private ImInt numSprites = new ImInt();

    private final Map<String, SpriteSheet> spriteSheets = new HashMap<>();

    public AssetManager() {
        init();
    }

    // TODO: Add functionality to save/load the assets based on the editors previous state
    private void init() {
        this.spriteSheets.put("Terrain", ResourceManager.getSpriteSheet("textures/test.png"));
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
        ImGui.text("Width:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##Width", width);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Height:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##Height", height);

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

    private void addSpriteSheet() {
        String fileName = String.valueOf(filePath);
        String type = String.valueOf(assetType);

        int spriteWidth = width.intValue();
        int spriteHeight = height.intValue();
        int amount = numSprites.intValue();

        spriteSheets.put(type,
                new SpriteSheet(
                        new Texture(fileName),
                        spriteWidth,
                        spriteHeight,
                        0,
                        amount)
        );
        refreshImportFields();
    }

    public void renderRemoveForm() {
        ImGui.begin("Remove");

        ImGui.setCursorPos(ImGui.getWindowSizeX() / 6, 50);
        if (ImGui.beginCombo("##MapKeysCombo", "Select SpriteSheet")) {
            Iterator<Map.Entry<String, SpriteSheet>> iterator = spriteSheets.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, SpriteSheet> entry = iterator.next();
                String key = entry.getKey();
                SpriteSheet value = entry.getValue();

                String option = value.getFilePathOfTexture();

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
        numSprites = new ImInt();
    }

    public Map<String, SpriteSheet> getSpriteSheets() {
        return spriteSheets;
    }

}
/*End of ImportSpriteSheet class*/