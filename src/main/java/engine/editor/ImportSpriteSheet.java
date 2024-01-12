/*
 Title: ImportSpriteSheet
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.editor;

import imgui.ImGui;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.List;

public class ImportSpriteSheet {

    private  ImString filePath = new ImString();
    private  ImString assetType = new ImString();
    private  ImInt width = new ImInt();
    private  ImInt height = new ImInt();
    private  ImInt numSprites = new ImInt();
    private final int itemSpacing = 10;

    private static List<String> spriteSheets = new ArrayList<>();
    private static int selectedSpriteSheetIndex = -1;

    public void renderInputForm() {
        ImGui.begin("Import");

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("File Path:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##FilePath", filePath, 256);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Asset Type:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputInt("##AssetType", width);

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
            // TODO: Handle the submitted values (filePath, width, height, numSprites)
            System.out.println("File Path: " + filePath);
            System.out.println("Width: " + width);
            System.out.println("Height: " + height);
            System.out.println("Number of Sprites: " + numSprites);
        }

        ImGui.end();
    }

    public void renderRemoveForm() {
        ImGui.begin("Remove");
        ImGui.text(" ");
        ImGui.setCursorPosX(itemSpacing);
        if (ImGui.beginCombo("##SpriteSheetCombo", selectedSpriteSheetIndex >= 0 ? spriteSheets.get(selectedSpriteSheetIndex) : "Select Sprite Sheet")) {
            for (int i = 0; i < spriteSheets.size(); i++) {
                boolean isSelected = (selectedSpriteSheetIndex == i);
                if (ImGui.selectable(spriteSheets.get(i), isSelected)) {
                    selectedSpriteSheetIndex = i;
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }
        ImGui.spacing();

        ImGui.setCursorPosX(itemSpacing);

        if (ImGui.button("Remove") && selectedSpriteSheetIndex >= 0) {
            // TODO: Handle the removal of the selected sprite sheet
            System.out.println("Removing Sprite Sheet: " + spriteSheets.get(selectedSpriteSheetIndex));
            spriteSheets.remove(selectedSpriteSheetIndex);
            selectedSpriteSheetIndex = -1; // Reset selection after removal
        }
        ImGui.end();
    }
}
/*End of ImportSpriteSheet class*/