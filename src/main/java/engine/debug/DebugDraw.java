/*
 Title: Draw
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debug;

import engine.UI.engine.EngineWindow;
import engine.graphics.Shader;
import engine.managers.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

import static engine.settings.EConstants.DEBUG_LINE_WIDTH;
import static engine.settings.EConstants.MAX_DEBUG_LINES;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugDraw {

    // Rendering
    private static int vaoID;
    private static int vboID;
    private static final float[] vertices = new float[MAX_DEBUG_LINES * 6 * 2];
    private static final Shader debugShader = ResourceManager.getOrCreateShader("shaders/DebugLines.glsl");

    // Debug Lines
    private static final ArrayList<DebugLine> debugLines = new ArrayList<>();
    private static boolean running = false;


    public static void init() {
        // Generate the vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Generate the vertex buffer object
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

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

    public static void tick() {
        // Initialize the Draw class
        if (!running) {
            init();
            running = true;
        }

        // Chek and remove lines that are not live
        for (int i = 0; i < debugLines.size(); i++) {
            if (!debugLines.get(i).isLineLive()) {
                debugLines.remove(i);
                i--;
            }
        }
    }

    public static void render() {
        // Check if there are any lines to render
        if (debugLines.isEmpty()) {
            return;
        }

        // Index to keep track of the current position in the vertices array
        int index = 0;

        // Iterate through each line in the list
        for (DebugLine debugLine : debugLines) {
            // Iterate twice for the start and end points of the line
            for (int i = 0; i < 2; i++) {
                // Determine whether to get the start or end point of the line
                Vector2f position = (i == 0) ? debugLine.getLineStart() : debugLine.getLineEnd();
                Vector3f color = debugLine.getLineColor();

                // Load position into the vertices array
                vertices[index] = position.x;
                vertices[index + 1] = position.y;
                vertices[index + 2] = 0.0f;

                // Load color into the vertices array
                vertices[index + 3] = color.x;
                vertices[index + 4] = color.y;
                vertices[index + 5] = color.z;

                // Move to the next set of coordinates in the vertices array
                index += 6;
            }
        }

        // Update the buffer data with the new vertices
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertices, 0, debugLines.size() * 6 * 2));

        // Use the debug shader
        debugShader.use();
        debugShader.uploadMat4f("uProjection", EngineWindow.getCamera().getProjectionMatrix());
        debugShader.uploadMat4f("uView", EngineWindow.getCamera().calculateViewMatrix());

        // Bind the vertex array object (VAO) and enable vertex attributes
        glBindVertexArray(vaoID);
        enableVertexAttributes();

        // Draw the lines
        glDrawArrays(GL_LINES, 0, debugLines.size() * 6 * 2);

        // Disable vertex attributes and unbind the VAO
        disableVertexAttributes();
        glBindVertexArray(0);

        // Detach the shader
        debugShader.detach();
    }

    private static void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    private static void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    public static ArrayList<DebugLine> getDebugLines() {
        return debugLines;
    }

    public static Shader getDebugShader() {
        return debugShader;
    }
}
/*End of Draw class*/
