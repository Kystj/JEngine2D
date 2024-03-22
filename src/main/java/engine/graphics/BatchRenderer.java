/*
 Title: BatchRendererTest
 Date: 2024-01-02
 Author: Kyle St John
 */
package engine.graphics;

import engine.ui.engine.EngineWindow;
import engine.world.components.Sprite;
import engine.utils.ResourceHandler;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static engine.settings.EConstants.MAX_BATCH_SIZE;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class BatchRenderer implements Comparable<BatchRenderer> {

    private final int vertexSize = 10;

    private final Sprite[] sprites = new Sprite[MAX_BATCH_SIZE];
    private int numSprites = 0;

    private boolean bBatchHasRoom = true;
    private boolean reBuffer = true;

    private final float[] vertices = new float[MAX_BATCH_SIZE * 4 * vertexSize];
    private int vaoID, vboID;

    private Shader shader = ResourceHandler.getOrCreateShader("shaders/Default.glsl");

    private final List<Texture> textures = new ArrayList<>();
    private final int NUM_TEXTURE_SLOTS = 16; // Define the number of texture slots
    private final int[] textureSlots = new int[NUM_TEXTURE_SLOTS];

    private int zIndex;

    public BatchRenderer(int zIndex) {
        this.zIndex = zIndex;
        init();
    }

    @Override
    public int compareTo(BatchRenderer o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }

    public void init() {
        // Initialize textureSlots
        // Initialize the texture slots array
        for (int i = 0; i < NUM_TEXTURE_SLOTS; i++) {
            textureSlots[i] = i;
        }

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

        int uvCoordinateSize = 2;
        int uvCoordinateOffset = colorPosOffset + vertexColorSize * Float.BYTES;
        glVertexAttribPointer(2, uvCoordinateSize, GL_FLOAT, false, vertexSizeInBytes, uvCoordinateOffset);
        glEnableVertexAttribArray(2);

        int textureIdSize = 1;
        int textureIdOffset = uvCoordinateOffset + uvCoordinateSize * Float.BYTES;
        glVertexAttribPointer(3, textureIdSize, GL_FLOAT, false, vertexSizeInBytes, textureIdOffset);
        glEnableVertexAttribArray(3);

        int objectUIDSize = 1;
        int objectUIDOffset = textureIdOffset + textureIdSize * Float.BYTES;
        glVertexAttribPointer(4, objectUIDSize, GL_FLOAT, false, vertexSizeInBytes, objectUIDOffset);
        glEnableVertexAttribArray(4);
    }

    public void render() {
        for (int i=0; i < numSprites; i++) {
            Sprite sprite = sprites[i];
            if (sprite.isBisModified()) {
                updateVertexAttribArray(i);
                sprite.setModified(false);
                reBuffer = true;
            }
        }

        if (reBuffer) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
            reBuffer = false;
        }

        // Use shader
        shader = Renderer.getActiveShader();
        shader.use();
        shader.uploadMat4f("uProjection", EngineWindow.get().getCurrentScene().getOrthoCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", EngineWindow.get().getCurrentScene().getOrthoCamera().calculateViewMatrix());

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

    public void addSpriteToBatch(Sprite sprite) {
        // Get index and add renderObject
        int index = this.numSprites;
        this.sprites[index] = sprite;
        this.numSprites++;

        addTexture(sprite);

        // Update the vertex attribute array
        updateVertexAttribArray(index);
        checkCapacity();
    }

    private void addTexture(Sprite sprite) {
        if (sprite.getSpriteTexture() != null) {
            if (!textures.contains(sprite.getSpriteTexture())) {
                textures.add(sprite.getSpriteTexture());
            }
        }
    }

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
        float rotation = sprite.getTransform().getRotation(); // Get the rotation angle

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

        // Calculate the half size of the sprite for rotation
        float halfWidth = xSize * 0.5f;
        float halfHeight = ySize * 0.5f;

        // Calculate the cosine and sine of the rotation angle
        float cos = (float) Math.cos(Math.toRadians(rotation)); // Ensure angle is in radians
        float sin = (float) Math.sin(Math.toRadians(rotation));

        // Add vertices with the appropriate properties
        for (int i = 0; i < 4; i++) {
            // Calculate vertex position relative to the sprite's center and apply rotation
            float localX = ((i == 0 || i == 1) ? -halfWidth : halfWidth);
            float localY = ((i == 0 || i == 3) ? -halfHeight : halfHeight);
            float rotatedX = localX * cos - localY * sin;
            float rotatedY = localX * sin + localY * cos;

            // Load position
            vertices[offset] = xPos + rotatedX;
            vertices[offset + 1] = yPos + rotatedY;

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

            // Load entity id
            vertices[offset + 9] = sprite.getOwningGameObject().getUID() + 1;

            offset += vertexSize;
        }
    }

    private void bindTextures() {
        for (int i = 0; i < textures.size(); i++) {
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

    private void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
    }

    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
    }


    private void checkCapacity() {
        if (numSprites >= MAX_BATCH_SIZE) {
            this.bBatchHasRoom = false;
        }
    }

    public boolean getBatchHasRoom() {
        return this.bBatchHasRoom;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public boolean isTexSlotsFull() {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture tex) {
        return this.textures.contains(tex);
    }
}
/*End of BatchRenderer class*/