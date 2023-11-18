/*
 Title: renderer
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static engine.settings.EConstants.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Renderer class for the engine
 */
public class Renderer {

    Shader shader = new Shader("shaders/default.glsl");
    Texture texture = new Texture("textures/container.jpg");

    private final float[] vertices = {
            // positions          // colors           // texture cords
            0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,   // top right
            0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,   // bottom right
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,   // bottom left
            -0.5f, 0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f    // top left
    };

    private final int[] indices = {
            2, 1, 3, // first triangle
            3, 1, 2  // second triangle
    };


    private int vaoID;

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
        // Compile and link the shader
        shader.compileAndLinkShaders();

        // Create the VAO
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length);
        vertexBuffer.put(vertices).flip();

        // Create VBO upload the vertex buffer
        int vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create an int buffer of indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indices.length);
        elementBuffer.put(indices).flip();

        // Create an element buffer object
        int eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int vertexSizeBytes = VA_POS_SIZE_BYTES + VA_COLOR_SIZE_BYTES + VA_UV_SIZE_BYTES;

        // Activate the position attribute pointer
        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Activate the color attribute pointer
        glVertexAttribPointer(1, 3, GL_FLOAT, false, vertexSizeBytes, VA_POS_SIZE_BYTES);
        glEnableVertexAttribArray(1);

        // Activate the color attribute pointer
        glVertexAttribPointer(2, 2, GL_FLOAT, false, vertexSizeBytes, (VA_COLOR_SIZE_BYTES + VA_POS_SIZE_BYTES));
        glEnableVertexAttribArray(2);
    }

    /**
     * Update the renderer
     */
    public void render() {
        clear();

        // Activate the shader
        shader.use();

        texture.bind();
        glBindVertexArray(vaoID);

        enableVertexAttributes();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 3);

        disableVertexAttributes();

        glBindVertexArray(0);
        texture.unbind();

        // Deactivate the shader
        shader.detatch();
    }

    /**
     * Enables the various vertex attributes needed for rendering
     */
    private void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    /**
     * Disables the various vertex attributes no longer needed for rendering
     */
    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
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
        glClearColor(0,0,0,0);
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
