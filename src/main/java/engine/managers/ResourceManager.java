/*
 Title: ResourceManager
 Date: 2023-12-06
 Author: Kyle St John
 */
package engine.managers;

import engine.audio.Audio;
import engine.graphics.Shader;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/** The ResourceManager class centrally manages game resources like shaders, textures, and audio.
 *  It uses static maps and provides concise getOrCreate* methods for efficient retrieval or
 *  creation of resources based on unique identifiers. */
public class ResourceManager {

    private static final Map<String, Shader> shaderMap = new HashMap<>();
    private static final Map<String, Texture> textureMap = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheetMap = new HashMap<>();
    private static final Map<String, Audio > audioMap = new HashMap<>();

    /** Create a shader if it does not already exist in the pool and return it. If it does exist
     * just return it. */
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

    /** Create a texture if it does not already exist in the pool and return it. If it does exist
     * just return it. */
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

    public static int SpriteSheetMapSize() {
        return spriteSheetMap.size();
    }
}
/*End of ResourceManager class*/
