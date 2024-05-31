/*
 Title: Draw
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debugging.draw;

import engine.graphics.Shader;
import engine.editor.GameEditor;
import engine.utils.engine.ResourceUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

import static engine.utils.engine.EConstants.DEBUG_LINE_WIDTH;
import static engine.utils.engine.EConstants.MAX_DEBUG_LINES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * The DebugDraw class is responsible for rendering debug lines in the game engine.
 * It utilizes OpenGL for rendering and supports dynamic addition and removal of debug lines.
 */
public class DebugRenderer {

    // Rendering
    private static int VAO_ID;
    private static int VBO_ID;
    private static final float[] Vertices = new float[MAX_DEBUG_LINES * 6 * 2];
    private static final Shader Debug_Shader = ResourceUtils.getOrCreateShader("C:\\Dev\\StellarSprite2D\\JEngine2D\\shaders\\Debug.glsl");

    // Debug Lines
    private static final ArrayList<DebugLine> DEBUG_LINES = new ArrayList<>();
    private static boolean Is_Running = false;

    /**
     * Initializes the DebugDraw class by generating vertex array and buffer objects,
     * enabling vertex attributes, and setting the line width.
     */
    public static void init() {
        // Generate the vertex array object
        VAO_ID = glGenVertexArrays();
        glBindVertexArray(VAO_ID);

        // Generate the vertex buffer object
        VBO_ID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferData(GL_ARRAY_BUFFER, Vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Enable the attributes
        // Position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Color
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Set the line width (for windows)
        glLineWidth(DEBUG_LINE_WIDTH);
    }


    /**
     * Renders the debug lines by preparing the vertex data, updating the buffer,
     * and drawing the lines using OpenGL.
     */
    public static void render() {
        // Check if there are any lines to render
        if (DEBUG_LINES.isEmpty()) {
            return;
        }

        // Index to keep track of the current position in the vertices array
        int index = 0;

        // Iterate through each line in the list
        for (DebugLine debugLine : DEBUG_LINES) {
            // Iterate twice for the start and end points of the line
            for (int i = 0; i < 2; i++) {
                // Determine whether to get the start or end point of the line
                Vector2f position = (i == 0) ? debugLine.getLineStart() : debugLine.getLineEnd();
                Vector3f color = debugLine.getLineColor();

                // Load position into the vertices array
                Vertices[index] = position.x;
                Vertices[index + 1] = position.y;
                Vertices[index + 2] = 0.0f;

                // Load color into the vertices array
                Vertices[index + 3] = color.x;
                Vertices[index + 4] = color.y;
                Vertices[index + 5] = color.z;

                // Move to the next set of coordinates in the vertices array
                index += 6;
            }
        }

        // Update the buffer data with the new vertices
        glBindBuffer(GL_ARRAY_BUFFER, VBO_ID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(Vertices, 0, DEBUG_LINES.size() * 6 * 2));

        // Use the debug shader
        Debug_Shader.use();
        Debug_Shader.uploadMat4f("uProjection", GameEditor.current_Level.getOrthoCamera().getProjectionMatrix());
        Debug_Shader.uploadMat4f("uView", GameEditor.current_Level.getOrthoCamera().calculateViewMatrix());

        // Bind the vertex array object (VAO) and enable vertex attributes
        glBindVertexArray(VAO_ID);
        enableVertexAttributes();

        // Draw the lines
        glDrawArrays(GL_LINES, 0, DEBUG_LINES.size() * 6 * 2);

        // Disable vertex attributes and unbind the VAO
        disableVertexAttributes();
        glBindVertexArray(0);

        // Detach the shader
        Debug_Shader.detach();

        // TODO: Fix the error that is being caused by the conflict with box2D in the following lines of code
     /*        // Check and remove lines that are not live
        for (int i = 0; i < DEBUG_LINES.size(); i++) {
            if (DEBUG_LINES.get(i).isLineLive() < 0) {
                DEBUG_LINES.remove(i);
                i--;
            }
        }*/
    }

    /**
     * Enables vertex attributes for position and color.
     */
    private static void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    /**
     * Disables vertex attributes for position and color.
     */
    private static void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    /**
     * Returns the list of debug lines.
     *
     * @return ArrayList of DebugLine objects.
     */
    public static ArrayList<DebugLine> getLines() {
        return DEBUG_LINES;
    }

    /**
     * Returns the debug shader used for rendering.
     *
     * @return Shader object.
     */
    public static Shader getDebugShader() {
        return Debug_Shader;
    }

    /**
     * Clears all persistent lines
     */
    public static void clearPersistentLines() {
        DEBUG_LINES.clear();
    }
}
/*End of Draw class*/
