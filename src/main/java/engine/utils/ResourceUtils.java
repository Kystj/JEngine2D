/*
 Title: ResourceManager
 Date: 2023-12-06
 Author: Kyle St John
 */
package engine.utils;

import engine.io.Audio;
import engine.graphics.Shader;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/** ResourceManager class for managing shaders, textures, sprite sheets, and audio resources. */
public class ResourceUtils {

    private static final Map<String, Shader> shaderMap = new HashMap<>();
    private static final Map<String, Texture> textureMap = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();
    private static final Map<String, Audio> audioMap = new HashMap<>();

    public static Shader getOrCreateShader(String shaderID) {
        File file = new File(shaderID);
        if (!shaderMap.containsKey(shaderID)) {
            Shader newShader = new Shader(shaderID);
            newShader.compileAndLinkShaders();
            shaderMap.put(file.getAbsolutePath(), newShader);
            return newShader;
        }
        return shaderMap.get(file.getAbsolutePath());
    }

    public static Texture getOrCreateTexture(String textureID) {
        File file = new File(textureID);
        if (!textureMap.containsKey(textureID)) {
            Texture texture = new Texture(textureID);
            textureMap.put(file.getAbsolutePath(), texture);
            return texture;
        }
        return textureMap.get(file.getAbsolutePath());
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet) {
        File file = new File(resourceName);
        if (!spriteSheetMap.containsKey(file.getAbsolutePath())) {
            spriteSheetMap.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName) {
        File file = new File(resourceName);
        return spriteSheetMap.get(file.getAbsolutePath());
    }


    public static Map<String, SpriteSheet> getSpriteSheets() {
        return spriteSheetMap;
    }

    public static void deleteFile(String path) {
        File fileToDelete = new File(path);

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                System.out.println(path + " deleted successfully.");
            } else {
                System.out.println("Failed to delete the file: " + path);
            }
        } else {
            System.out.println("File does not exist: " + path);
        }
    }

    public static void removeGSONReferenceFile(String fileName, String directory, String fileExtension) {
        String GSONRef = fileName.replace(fileExtension, "txt");
        String filePath = GSONRef.replace(directory, "saved");
        deleteFile(filePath);
    }

    public static boolean isValidFilePath(String path) {
        Path filePath = Paths.get(path);
        return Files.exists(filePath) && Files.isReadable(filePath);
    }
}
/*End of ResourceManager class*/
