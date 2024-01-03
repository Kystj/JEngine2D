/*
 Title: BatchRendererTest
 Date: 2024-01-02
 Author: Kyle St John
 */
package engine.graphics;

import engine.UI.EngineWindow;
import engine.components.Sprite;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import static engine.settings.EConstants.MAX_BATCH_SIZE;
import static engine.settings.EConstants.VERTEX_SIZE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer {

    private final Sprite[] sprites = new Sprite[MAX_BATCH_SIZE];
    private int numSprites = 0;
    private boolean bBatchHasRoom = true;
    private final float[] vertices = new float[MAX_BATCH_SIZE * 4 * VERTEX_SIZE];
    private int vaoID, vboID;
    private final Shader shader = new Shader("shaders/default.glsl");

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

        // Attribute values
        int vertexSizeBytes = VERTEX_SIZE * Float.BYTES;
        int posAttribOffset = 0;
        int posAttribSize = 2;
        int colorAttribOffset = posAttribOffset + posAttribSize * Float.BYTES;
        int colorAttribSize = 4;

        // Enable attributes
            // Position
        glVertexAttribPointer(0, posAttribSize, GL_FLOAT, false, vertexSizeBytes, posAttribOffset);
        glEnableVertexAttribArray(0);

            // Color
        glVertexAttribPointer(1, colorAttribSize, GL_FLOAT, false, vertexSizeBytes, colorAttribOffset);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(Sprite spr) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        // Update the vertex attribute array
        updateVertexAttribArray(index);

        if (numSprites >= MAX_BATCH_SIZE) {
            this.bBatchHasRoom = false;
        }
    }

    public void render() {
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", EngineWindow.getCurrentScene().getOrthoCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", EngineWindow.getCurrentScene().getOrthoCamera().calculateViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detatch();
    }

    private void updateVertexAttribArray(int index) {
        // Method 0
        Sprite sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

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
            vertices[offset] = sprite.owningGameObject.transform.position.x + (xAdd * sprite.owningGameObject.transform.scale.x);
            vertices[offset + 1] = sprite.owningGameObject.transform.position.y + (yAdd * sprite.owningGameObject.transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    // Method 1
   /*     Sprite sprite = sprites[index];
        GameObject gameObject = sprite.getOwningGameObject();
        Transform transform = gameObject.getTransform();
        Vector4f color = sprite.getColor();
        int offset = index * 4 * VERTEX_SIZE;
        float[] xOffsets = {1.0f, 0.0f, 0.0f, 1.0f};
        float[] yOffsets = {1.0f, 0.0f, 1.0f, 1.0f};
        for (int i = 0; i < 4; i++) {
            float x = transform.getPosition().x + (xOffsets[i] * transform.getScale().x);
            float y = transform.getPosition().y + (yOffsets[i] * transform.getScale().y);
            vertices[offset] = x;
            vertices[offset + 1] = y;
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;
            offset += VERTEX_SIZE;
        }*/

        // Method 3
       /* Sprite sprite = this.sprites[index];

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Transform transform = sprite.owningGameObject.transform;

        // Add vertices with the appropriate properties
        for (int i = 0; i < 4; i++) {
            float xAdd = (i == 1 || i == 2) ? 0.0f : 1.0f;
            float yAdd = (i == 1 || i == 3) ? 0.0f : 1.0f;

            // Load position
            vertices[offset] = transform.position.x + (xAdd * transform.scale.x);
            vertices[offset + 1] = transform.position.y + (yAdd * transform.scale.y);

            // Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }*/
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
        //glEnableVertexAttribArray(2);
    }

    /**
     * Disables the various vertex attributes no longer needed for rendering
     */
    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        //glDisableVertexAttribArray(2);
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
    public void cleanup() {
        // TODO: Implement renderer clean up code
    }

    public boolean hasRoom() {
        return this.bBatchHasRoom;
    }
}
/*End of BatchRenderer class*/