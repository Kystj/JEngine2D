/*
 Title: BatchRendererTest
 Date: 2024-01-02
 Author: Kyle St John
 */
package engine.graphics;

import engine.UI.engine.EngineWindow;
import engine.world.components.Sprite;
import engine.handlers.ResourceHandler;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static engine.UI.settings.EConstants.MAX_BATCH_SIZE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * The BatchRenderer class is responsible for rendering a batch of sprites efficiently using OpenGL.
 */
public class BatchRenderer {

    private final int vertexSize = 9;

    private final Sprite[] sprites = new Sprite[MAX_BATCH_SIZE];
    private int numSprites = 0;

    private boolean bBatchHasRoom = true;

    private final float[] vertices = new float[MAX_BATCH_SIZE * 4 * vertexSize];
    private int vaoID, vboID;

    private final Shader shader = ResourceHandler.getOrCreateShader("shaders/Default.glsl");

    private final List<Texture> textures = new ArrayList<>();
    private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    /**
     * Constructs a BatchRenderer and initializes the necessary OpenGL resources.
     */
    public BatchRenderer() {
        init();
    }

    /**
     * Initializes the OpenGL resources, such as shaders, VAO, and VBO.
     */
    public void init() {
        // Compile and link shaders
        shader.compileAndLinkShaders();

        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create and generate a VBO
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable the buffer attribute pointers
        int vertexSizeInBytes = vertexSize * Float.BYTES;
        int vertexPosOffset = 0;
        int vertexPosSize = 2;

        glVertexAttribPointer(0, vertexPosSize, GL_FLOAT, false, vertexSizeInBytes, vertexPosOffset);
        glEnableVertexAttribArray(0);

        int vertexColorSize = 4;
        int colorPosOffset = vertexPosOffset + vertexPosSize * Float.BYTES;
        glVertexAttribPointer(1, vertexColorSize, GL_FLOAT, false, vertexSizeInBytes, colorPosOffset);
        glEnableVertexAttribArray(1);

        int uvCoordinateOffset = colorPosOffset + vertexColorSize * Float.BYTES;
        int uvCoordinateSize = 2;
        glVertexAttribPointer(2, uvCoordinateSize, GL_FLOAT, false, vertexSizeInBytes, uvCoordinateOffset);
        glEnableVertexAttribArray(2);

        int textureIdSize = 1;
        int textureIdOffset = uvCoordinateOffset + uvCoordinateSize * Float.BYTES;
        glVertexAttribPointer(3, textureIdSize, GL_FLOAT, false, vertexSizeInBytes, textureIdOffset);
        glEnableVertexAttribArray(3);
    }

    /**
     * Renders the batch of sprites using the specified shader and textures.
     */
    public void render() {
        // For now, we will re-buffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", EngineWindow.getCurrentScene().getOrthoCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", EngineWindow.getCurrentScene().getOrthoCamera().calculateViewMatrix());

        // Bind textures
        bindTextures();
        shader.uploadIntArray("uTextures", textureSlots);

        glBindVertexArray(vaoID);
        enableVertexAttributes();

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        disableVertexAttributes();
        glBindVertexArray(0);

        unBindTextures();

        shader.detach();
    }

    /**
     * Adds a sprite to the batch for rendering.
     *
     * @param sprite The sprite to be added to the batch.
     */
    public void addSpriteToBatch(Sprite sprite) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        addTexture(sprite);

        // Update the vertex attribute array
        updateVertexAttribArray(index);
        updateBatchCapacityFlag();
    }

    /**
     * Adds the texture of a sprite to the textures list if it is not already present.
     *
     * @param sprite The sprite whose texture needs to be added.
     */
    private void addTexture(Sprite sprite) {
        if (sprite.getSpriteTexture() != null) {
            if (!textures.contains(sprite.getSpriteTexture())) {
                textures.add(sprite.getSpriteTexture());
            }
        }
    }

    /**
     * Updates the vertex attribute array for a specific sprite at the given index.
     *
     * @param index The index of the sprite in the batch.
     */
    private void updateVertexAttribArray(int index) {
        // Store the sprite at the specified index
        Sprite sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * vertexSize;

        // Get the sprite's attributes
        Vector2f spritePos = sprite.getSpritePos();
        Vector2f spriteSize = sprite.getSpriteSize();
        Vector4f color = sprite.getColor();
        Vector2f[] uvCoordinates = sprite.getUvCoordinates();

        // Check if the sprite has a texture, and if so, find its corresponding ID in the textures array
        int textureID = 0;
        Texture spriteTexture = sprite.getSpriteTexture();

        if (spriteTexture != null) {
            for (int i = 0; i < textures.size(); i++) {
                if (textures.get(i) == spriteTexture) {
                    // [0, tex, tex, tex, tex]: Adding texId = i + 1; gives us that special reserve slot for color
                    textureID = i + 1;
                    break;
                }
            }
        }

        // Pre-calculate position components
        float xPos = spritePos.x;
        float yPos = spritePos.y;
        float xSize = spriteSize.x;
        float ySize = spriteSize.y;

        // Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1:
                    yAdd = 0.0f;
                    break;
                case 2:
                    xAdd = 0.0f;
                    break;
                case 3:
                    yAdd = 1.0f;
                    break;
                // No change for case 0
            }

            // Load position
            vertices[offset] = xPos + (xAdd * xSize);
            vertices[offset + 1] = yPos + (yAdd * ySize);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // Load texture coordinates
            vertices[offset + 6] = uvCoordinates[i].x;
            vertices[offset + 7] = uvCoordinates[i].y;

            // Load texture id
            vertices[offset + 8] = textureID;

            offset += vertexSize;
        }
    }

    /**
     * Binds the textures to their respective texture units.
     */
    private void bindTextures() {
        for (int i = 0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
    }

    /**
     * Unbinds the textures.
     */
    private void unBindTextures() {
        for (Texture texture : textures) {
            texture.unbind();
        }
    }

    /**
     * Generates the indices for rendering triangles from quads.
     *
     * @return An array of indices for rendering triangles.
     */
    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * MAX_BATCH_SIZE];
        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    /**
     * Loads the indices for rendering triangles from quads into the elements array.
     *
     * @param elements The array to store the indices.
     * @param index    The index of the quad in the batch.
     */
    private void loadElementIndices(int[] elements, int index) {
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    /**
     * Enables the various vertex attributes needed for rendering.
     */
    private void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
    }

    /**
     * Disables the various vertex attributes no longer needed for rendering.
     */
    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }


    /**
     * Releases any resources, shaders, textures, etc.
     */
    private void cleanup() {
        // TODO: Implement renderer cleanup code
    }

    /**
     * Updates the flag for the batch capacity limit.
     */
    private void updateBatchCapacityFlag() {
        if (numSprites >= MAX_BATCH_SIZE) {
            this.bBatchHasRoom = false;
        }
    }

    /**
     * Getter for the bBatchHasRoom variable.
     *
     * @return True if the batch has room for more sprites, false otherwise.
     */
    public boolean getBatchHasRoom() {
        return this.bBatchHasRoom;
    }
}
/*End of BatchRenderer class*/