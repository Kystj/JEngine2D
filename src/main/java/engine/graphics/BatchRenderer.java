/*
 Title: BatchRendererTest
 Date: 2024-01-02
 Author: Kyle St John
 */
package engine.graphics;

import engine.UI.EngineWindow;
import engine.components.Sprite;
import engine.managers.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static engine.settings.EConstants.MAX_BATCH_SIZE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer {

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 9;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private final Sprite[] sprites = new Sprite[MAX_BATCH_SIZE];
    private int numSprites = 0;

    private boolean bBatchHasRoom = true;

    private final float[] vertices = new float[MAX_BATCH_SIZE * 4 * VERTEX_SIZE];
    private int vaoID, vboID;

    private final Shader shader = ResourceManager.getOrCreateShader("shaders/default.glsl");

    private List<Texture> textures = new ArrayList<>();
    private final int[] textureSlots = new int[7];


    public BatchRenderer() {
        init();
    }

    // Create all the data on the GPU
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

       /* // Attribute values
        int vertexSizeBytes = VERTEX_SIZE * Float.BYTES;
        int posAttribOffset = 0;
        int posAttribSize = 2;
        int colorAttribOffset = posAttribOffset + posAttribSize * Float.BYTES;
        int colorAttribSize = 4;
        int uvAttribSize = 2;
        int uvAttribOffset = colorAttribOffset + colorAttribSize * Float.BYTES;
        int textureIDAttribSize = 1;
        int textureIDOffset = uvAttribOffset + uvAttribSize * Float.BYTES;*/


      /*  // Enable attributes
            // Position
        glVertexAttribPointer(0, posAttribSize, GL_FLOAT, false, vertexSizeBytes, posAttribOffset);
        glEnableVertexAttribArray(0);

            // Color
        glVertexAttribPointer(1, colorAttribSize, GL_FLOAT, false, vertexSizeBytes, colorAttribOffset);
        glEnableVertexAttribArray(1);
            // UV
        glVertexAttribPointer(2, uvAttribSize, GL_FLOAT, false, vertexSizeBytes, uvAttribOffset);
        glEnableVertexAttribArray(2);

            // Texture ID
        glVertexAttribPointer(3, textureIDAttribSize, GL_FLOAT, false, vertexSizeBytes, textureIDOffset);
        glEnableVertexAttribArray(3);*/

        // Enable the buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);
    }

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

        shader.detatch();
    }


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

    private void addTexture(Sprite sprite) {
        if (sprite.getSpriteTexture() != null) {
            if (!textures.contains(sprite.getSpriteTexture())) {
                textures.add(sprite.getSpriteTexture());
            }
        }
    }

    private void updateVertexAttribArray(int index) {
        Sprite sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] uvCoordinates = sprite.getUvCoordinates();
        System.out.println("UVCoordinates: " + Arrays.toString(sprite.getUvCoordinates()));

        // Sprite has a texture so loop through until we find a match and use the texID as the ID in the array
        int textureID = 0;
        if (sprite.getSpriteTexture() != null) {
            for (int i = 0; i < textures.size(); i++) {
                // If we are on the Texture that our sprite has then that is the texID we want to use
                if (textures.get(i) == sprite.getSpriteTexture()) {
                    // [0, tex, tex, tex, tex]: Adding texId = i + 1; gives us that special reserve slot for color
                    textureID = i + 1;
                    break;
                }
            }
        }

        // Add vertices with the appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++) {
            if (i == 1) {
                yAdd = 0.0f;
            } else if (i == 2) {
                xAdd = 0.0f;
            } else if (i == 3) {
                yAdd = 1.0f;
            }

            // Load position
            vertices[offset] = sprite.getSpritePos().x +
                    (xAdd * sprite.getSpriteSize().x);
            vertices[offset + 1] = sprite.getSpritePos().y +
                    (yAdd * sprite.getSpriteSize().y);

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

            offset += VERTEX_SIZE;
        }
    }

    private void bindTextures() {
        for (int i=0; i < textures.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
    }

    private void unBindTextures() {
        for (Texture texture : textures) {
            texture.unbind();
        }
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * MAX_BATCH_SIZE];
        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

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
     * Enables the various vertex attributes needed for rendering
     */
    private void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
    }

    /**
     * Disables the various vertex attributes no longer needed for rendering
     */
    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }

    /**
     * Draws in wireframe mode. Can be toggled on and off
     */
    private void setWireframeMode(boolean active) {
        // TODO: Add as an option in the editor window
        glPolygonMode(GL_FRONT_AND_BACK, active ? GL_LINE : GL_FILL);
    }

    /**
     * Clears the color and depth buffers
     */
    private void clear() {
        glClearColor(1,1,1,1);
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Release any resources, shaders, textures, etc.
     */
    private void cleanup() {
        // TODO: Implement renderer clean up code
    }

    /** Update the flag for the bath capacity limit*/
    private void updateBatchCapacityFlag() {
        if (numSprites >= MAX_BATCH_SIZE) {
            this.bBatchHasRoom = false;
        }
    }

    /** Getter for the bBatchHasRoom variable */
    public boolean getBatchHasRoom() {
        return this.bBatchHasRoom;
    }
}
/*End of BatchRenderer class*/