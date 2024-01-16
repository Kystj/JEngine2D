/*
 Title: Texture
 Date: 2023-11-17
 Author: Kyle St John
 */
package engine.graphics;

import engine.managers.ResourceManager;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * This class encapsulates all the functionality needed to read a texture from a file,
 * create the texture and bind it
 */
public class Texture {

    private int textureID;

    private String filePath;
    private int textureWidth;
    private int textureHeight;

    public Texture(String filePath) {
        this.filePath = filePath;
        init();
    }

    public Texture(int width, int height) {
        // Generate and bind the texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Defines a 2D texture with RGBA Channels and generates the image
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        stbi_set_flip_vertically_on_load(true);
    }

    /**
     * Initializes a texture for use and applies the parameters to create and use a 2D texture
     */
    private void init() {
        // Generate and bind the texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Texture parameters(filtering/wrapping)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // wrap in the x direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // wrap in the y direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // when minifying
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // when magnifying

        loadTextureFromFile();
    }

    /**
     * The loadTextureFromFile method uses the STB Image library to load a texture from a specified file. It configures a 2D OpenGL
     * texture based on the image's properties, handling different color channels (RGB or RGBA) and performing error checks
     * during the process.
     */
    public void loadTextureFromFile() {
        if (!ResourceManager.isValidFilePath(filePath)) {
            throw new UnsupportedOperationException("Error: Invalid file path provided: '" + filePath + "'");
        }

        // stbi_load() requires an int buffer as a parameter
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filePath, width, height, channels, 0);

        // Check if the image was loaded successfully
        if (image != null) {
            textureWidth = width.get(0);
            textureHeight = width.get(0);

            // Check the number of channels and define the texture accordingly
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: Texture has " + channels.get(0) + " channels";
            }
            // Free the image data after texture creation
            stbi_image_free(image);
        } else {
            // Handle the case where the image couldn't be loaded
            assert false : "Error: Could not load texture with path: '" + filePath + "'";
        }
    }

    /**
     * Bind this texture for use
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    /**
     * Unbind this texture
     */
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Return this textures ID
     */
    public int getTextureID() {
        return textureID;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public String getFilePath() {
        return filePath;
    }

}
/*End of Texture class*/
