/*
 Title: Texture
 Date: 2023-11-17
 Author: Kyle St John
 */
package engine.graphics;

import engine.utils.ResourceHandler;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * The Texture class encapsulates functionality to read a texture from a file, create the texture, and bind it for use.
 */
public class Texture {

    private int textureID;
    private String filePath;
    private int textureWidth;
    private int textureHeight;

    /**
     * Constructs a Texture object with the specified file path.
     *
     * @param filePath The file path of the texture.
     */
    public Texture(String filePath) {
        this.filePath = filePath;
        init();
    }

    /**
     * Constructs a Texture object with the specified width and height for procedural texture generation.
     *
     * @param width  The width of the procedural texture.
     * @param height The height of the procedural texture.
     */
    public Texture(int width, int height) {
        // Generate and bind the texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // Defines a 2D texture with RGBA Channels and generates the image
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    /**
     * Initializes the texture, generating and binding it for use.
     */
    private void init() {
        // Generate and bind the texture
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // Texture parameters (filtering/wrapping)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // wrap in the x direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // wrap in the y direction
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST); // when minifying
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); // when magnifying

        loadTextureFromFile();
    }

    /**
     * Loads a texture from a file using the STB Image library.
     */
    private void loadTextureFromFile() {
        if (!ResourceHandler.isValidFilePath(filePath)) {
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
            textureHeight = height.get(0);

            // Check the number of channels and define the texture accordingly
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, textureWidth, textureHeight, 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Error: Texture has " + channels.get(0) + " channels";
            }
            // Free the image data after texture creation
            stbi_image_free(image);
        } else {
            // Handle the case where the image couldn't be loaded
            assert false : "Error: Could not load texture with path: '" + filePath + "'";
        }

        stbi_set_flip_vertically_on_load(true);
    }

    /**
     * Binds the texture for use.
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    /**
     * Unbinds the texture.
     */
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Gets the ID of the texture.
     *
     * @return The texture ID.
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * Gets the width of the texture.
     *
     * @return The texture width.
     */
    public int getTextureWidth() {
        return textureWidth;
    }

    /**
     * Gets the height of the texture.
     *
     * @return The texture height.
     */
    public int getTextureHeight() {
        return textureHeight;
    }

    /**
     * Gets the file path of the texture.
     *
     * @return The file path.
     */
    public String getFilePath() {
        return filePath;
    }
}
/*End of Texture class*/