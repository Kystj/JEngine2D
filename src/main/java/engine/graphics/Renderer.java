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

import static engine.settings.EConstants.VA_COLOR_SIZE_BYTES;
import static engine.settings.EConstants.VA_POS_SIZE_BYTES;
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
 /*   Texture texture1 = new Texture("textures/container.jpg");
    Texture texture2 = new Texture("textures/awesomeface.png");*/
    OrthographicCamera camera;


    private float[] vertices = {
            // position               // color
            100.5f, 0.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Bottom right 0
            0.5f,  100.5f, 0.0f,       0.0f, 1.0f, 0.0f, 1.0f, // Top left     1
            100.5f,  100.5f, 0.0f ,      1.0f, 0.0f, 1.0f, 1.0f, // Top right    2
            0.5f, 0.5f, 0.0f,       1.0f, 1.0f, 0.0f, 1.0f, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] indices = {
            /*
                    x        x


                    x        x
             */
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };


    private int vaoID;
    

    /**
     * Initialize the VAO, VBO, amd EBO buffer objects
     */
    public void init() {
        camera = new OrthographicCamera();

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
        int vertexSizeBytes = VA_POS_SIZE_BYTES + VA_COLOR_SIZE_BYTES;

        // Activate the position attribute pointer
        glVertexAttribPointer(0, 3, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Activate the color attribute pointer
        glVertexAttribPointer(1, 3, GL_FLOAT, false, vertexSizeBytes, VA_POS_SIZE_BYTES);
        glEnableVertexAttribArray(1);

        // UV Coordinates
/*        glVertexAttribPointer(2, 2, GL_FLOAT, false, vertexSizeBytes, (VA_COLOR_SIZE_BYTES + VA_POS_SIZE_BYTES));
        glEnableVertexAttribArray(2);*/

        // Set the texture samplers
    /*    shader.uploadTexture("texture1", 0);
        shader.uploadTexture("texture2", 1);*/


    }

    /**
     * Update the renderer
     */
    public void render() {
        clear();

        // Activate the shader
        shader.use();

        camera.position.x -= 0.01;
        camera.position.y -= 0.01;

        shader.uploadMat4f("uView", camera.calculateViewMatrix());
        shader.uploadMat4f("uProjection", camera.getProjectionMatrix());


        /*Activate texture unit 0 and bind the texture. // After activating a texture unit, a subsequent glBindTexture
         call will bind that texture to the currently active texture unit. Texture unit GL_TEXTURE0 is always by
        default activated*/
/*        glActiveTexture(GL_TEXTURE0);
        texture1.bind();
        glActiveTexture(GL_TEXTURE1);
        texture2.bind();*/

        glBindVertexArray(vaoID);

        enableVertexAttributes();
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);


        disableVertexAttributes();

        glBindVertexArray(0);
        /*texture1.unbind();*/

        // Deactivate the shader
        shader.detatch();
    }

    /**
     * Enables the various vertex attributes needed for rendering
     */
    private void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        /*glEnableVertexAttribArray(2);*/
    }

    /**
     * Disables the various vertex attributes no longer needed for rendering
     */
    private void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
       /* glDisableVertexAttribArray(2);*/
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
