/*
 Title: Draw
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debug;

import engine.UI.EngineWindow;
import engine.graphics.Shader;
import engine.managers.ResourceManager;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;

import static engine.settings.EConstants.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Draw {

    // Rendering
    private static int vaoID;
    private static int vboID;
    private static final float[] vertices = new float[MAX_DEBUG_LINES * 6 * 2];
    private static Shader debugShader = ResourceManager.getOrCreateShader("shaders/DebugLines.glsl");

    // Debug Lines
    private static final ArrayList<Line> lines = new ArrayList<>();
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

    public static void render() {
        if (lines.size() <= 0) {
            return;
        }

        int index = 0;
        for (Line line : lines) {

            for (int i = 0; i < 2; i++) {
                Vector2f position = i == 0 ? line.getLineStart() : line.getLineEnd();
                Vector3f color = line.getLineColor();

                // Load position
                vertices[index] = position.x;
                vertices[index + 1] = position.y;
                vertices[index + 2] = -10.0f;

                // Load the color
                vertices[index + 3] = color.x;
                vertices[index + 4] = color.y;
                vertices[index + 5] = color.z;
                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertices, 0, lines.size() * 6 * 2));


        // Use our shader
        debugShader.use();
        debugShader.uploadMat4f("uProjection", EngineWindow.getCurrentScene().getOrthoCamera().getProjectionMatrix());
        debugShader.uploadMat4f("uView", EngineWindow.getCurrentScene().getOrthoCamera().calculateViewMatrix());

        // Bind the vao
        glBindVertexArray(vaoID);
        enableVertexAttributes();

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2);

        // Disable Location
        disableVertexAttributes();
        glBindVertexArray(0);

        // Unbind shader
        debugShader.detach();
    }


    public static void clearDeadLines() {
        if (!running) {
            init();
            running = true;
        }

        // Remove dead lines
        for (int i=0; i < lines.size(); i++) {
            if (lines.get(i).isLineLive() < 0) {
                lines.remove(i);
                i--;
            }
        }
    }

    private static void enableVertexAttributes() {
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    private static void disableVertexAttributes() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }
    // ==================================================
    // Add line2D methods
    // ==================================================
    public static void addLine2D(Vector2f from, Vector2f to) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addLine2D(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color) {
        addLine2D(from, to, color, 1);
    }

    public static void addLine2D(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (lines.size() >= MAX_DEBUG_LINES) return;
        lines.add(new Line(from, to, color, lifetime));
    }

    // ==================================================
    // Add Box2D methods
    // ==================================================
    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation) {
        // TODO: ADD CONSTANTS FOR COMMON COLORS
        addBox2D(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox2D(center, dimensions, rotation, color, 1);
    }

    public static void addBox2D(Vector2f center, Vector2f dimensions, float rotation,
                                Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };


        addLine2D(vertices[0], vertices[1], color, lifetime);
        addLine2D(vertices[0], vertices[3], color, lifetime);
        addLine2D(vertices[1], vertices[2], color, lifetime);
        addLine2D(vertices[2], vertices[3], color, lifetime);
    }

}
/*End of Draw class*/
