/*
 Title: AssetManager
 Date: 2024-01-11
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.serialization.SpriteSheetSerializer;
import engine.utils.engine.ResourceUtils;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    private final List<String> filesInDirectory = new ArrayList<>();
    private final String baseDirectory = Paths.get("assets").toAbsolutePath().toString();  // Set the base directory to "assets"
    private String currentDirectory = baseDirectory;
    private int selectedFileIndex = -1;

    private void updateFileList(String directoryPath) {
        if (directoryPath == null || directoryPath.isEmpty()) {
            return;
        }
        try {
            filesInDirectory.clear();
            Files.list(Paths.get(directoryPath)).forEach(path -> filesInDirectory.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renderInputForm(ImBoolean isOpen, Map<SpriteSheet, String> spriteSheets) {
        int itemSpacing = 10;

        ImGui.begin("Import Form", isOpen);

        // Display input fields for asset properties
        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("File Path:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##FilePath", filePath);

        // Update the file list when the path changes
        updateFileList(currentDirectory);

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Choose File:");
        ImGui.setCursorPosX(itemSpacing);


        if (ImGui.beginCombo("##FileDropdown", selectedFileIndex >= 0 ? filesInDirectory.get(selectedFileIndex) : "")) {
            // Allow navigation to parent directory if not at base directory
            if (!currentDirectory.equals(baseDirectory)) {
                if (ImGui.selectable("..")) {
                    Path parentPath = Paths.get(currentDirectory).getParent();
                    if (parentPath != null && parentPath.startsWith(baseDirectory)) {
                        currentDirectory = parentPath.toString();
                        selectedFileIndex = -1;
                        updateFileList(currentDirectory);
                    }
                }
            }

            for (int i = 0; i < filesInDirectory.size(); i++) {
                boolean isSelected = (i == selectedFileIndex);
                String fileOrDir = filesInDirectory.get(i);

                if (ImGui.selectable(fileOrDir, isSelected)) {
                    Path selectedPath = Paths.get(currentDirectory, fileOrDir);
                    if (Files.isDirectory(selectedPath)) {
                        currentDirectory = selectedPath.toString();
                        selectedFileIndex = -1;
                        updateFileList(currentDirectory);
                    } else {
                        selectedFileIndex = i;
                        filePath.set(selectedPath.toString());
                    }
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }

        ImGui.setCursorPosX(itemSpacing);
        ImGui.text("Type or Name:");
        ImGui.setCursorPosX(itemSpacing);
        ImGui.inputText("##Type", assetType);

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
            if (!filePath.get().isEmpty()) {
                addSpriteSheet();
            }
        }
        ImGui.end();
    }


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
        AssetWindow.getSpriteSheets().put(
                ResourceUtils.getSpriteSheet(filePath),
                type
        );

        // Refresh input fields after adding
        refreshFields();
    }


    private boolean checkAndHandleErrors(String fileName, String type, int spriteWidth, int spriteHeight, int amount) {
        // Prevents adding duplicate sprite sheets or sprite sheets with the same file path
        if (AssetWindow.getSpriteSheets().get(ResourceUtils.getSpriteSheet(fileName)) != null) {
            bFileErrorPopup = true;
            refreshFields();
            return true;
        }

        // Prevents adding duplicate sprite sheets or sprite sheets with the same name
        for (String val : AssetWindow.getSpriteSheets().values()) {
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