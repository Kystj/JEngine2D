/*
 Title: renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL15.*;

/**
 * Renderer class for the engine
 */
public class Renderer {

    Shader shader = new Shader();

    private float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };

    /**
     * VAO, VBO, EBO id's
     */
    private int vaoID, vboID, eboID;

    /**
     * Constructor for the renderer class. Sets glClearColor to black
     */
    public Renderer(long glfwWindow) {
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Initialize the VAO, VBO, amd EBO buffer objects
     */
    public void init() {
        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
    }

    /**
     * Update the renderer
     */
    public void render() {
        shader.use();
        clear();
    }

    /**
     * Clears the color and depth buffers
     */
    private void clear() {
        glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Release any resources, shaders, textures, etc.
     */
    public void cleanup() {
        // TODO: Implement renderer clean up code
    }

}
/*End of renderer class*/
