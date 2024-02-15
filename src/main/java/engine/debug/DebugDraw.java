/*
 Title: DebugUtils
 Date: 2024-02-01
 Author: Kyle St John
 */
package engine.debug;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.UI.settings.EConstants.MAX_DEBUG_LINES;

/**
 * The DebugUtils class provides utility methods for adding various debug shapes
 * such as lines, boxes, circles, and triangles to the debug rendering system.
 */
public class DebugDraw {

    // TODO: Implement rotation for each shape

    // ============================ Add Debug Line ============================
    /**
     * Adds a debug line from the specified starting point to the ending point with default color and lifetime.
     *
     * @param from Starting point of the debug line.
     * @param to Ending point of the debug line.
     */
    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, new Vector3f(0, 1, 0), 1);
    }

    /**
     * Adds a debug line from the specified starting point to the ending point with a specified color and default lifetime.
     *
     * @param from Starting point of the debug line.
     * @param to Ending point of the debug line.
     * @param color Color of the debug line.
     */
    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        addLine(from, to, color, 1);
    }

    /**
     * Adds a debug line from the specified starting point to the ending point with a specified color and lifetime.
     * Limits the number of debug lines to prevent exceeding the maximum allowed.
     *
     * @param from Starting point of the debug line.
     * @param to Ending point of the debug line.
     * @param color Color of the debug line.
     * @param lifetime Lifetime of the debug line.
     */
    public static void addLine(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (DebugRenderer.getDebugLines().size() >= MAX_DEBUG_LINES) return;
        DebugRenderer.getDebugLines().add(new DebugLine(from, to, color, lifetime));
    }

    // ============================ Add Debug Box ============================
    /**
     * Adds a debug box at the specified center with default color, dimensions, rotation, and lifetime.
     *
     * @param center Center of the debug box.
     * @param dimensions Dimensions of the debug box.
     * @param rotation Rotation of the debug box.
     */
    public static void addBox(Vector2f center, Vector2f dimensions, float rotation) {
        addBox(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    /**
     * Adds a debug box at the specified center with a specified color, default dimensions, rotation, and lifetime.
     *
     * @param center Center of the debug box.
     * @param dimensions Dimensions of the debug box.
     * @param rotation Rotation of the debug box.
     * @param color Color of the debug box.
     */
    public static void addBox(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox(center, dimensions, rotation, color, 1);
    }

    /**
     * Adds a debug box at the specified center with a specified color, rotation, and lifetime.
     * Limits the number of debug lines to prevent exceeding the maximum allowed.
     *
     * @param center Center of the debug box.
     * @param dimensions Dimensions of the debug box.
     * @param rotation Rotation of the debug box.
     * @param color Color of the debug box.
     * @param lifetime Lifetime of the debug box.
     */
    public static void addBox(Vector2f center, Vector2f dimensions, float rotation,
                              Vector3f color, int lifetime) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            addLine(vertices[i], vertices[nextIndex], color, lifetime);
        }
    }

    // ============================ Add Debug Circle ============================
    /**
     * Adds a debug circle at the specified center with a default radius and color, and default lifetime.
     *
     * @param center Center of the debug circle.
     * @param radius Radius of the debug circle.
     * @param segments Number of segments for the debug circle.
     */
    public static void addCircle(Vector2f center, float radius, int segments) {
        addCircle(center, radius, new Vector3f(0, 1, 0), 1, segments);
    }

    /**
     * Adds a debug circle at the specified center with a specified color, default radius, and lifetime.
     *
     * @param center Center of the debug circle.
     * @param radius Radius of the debug circle.
     * @param color Color of the debug circle.
     */
    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1, 36); // Using 32 segments for a smooth circle
    }

    /**
     * Adds a debug circle at the specified center with a specified color, lifetime, and number of segments.
     * Limits the number of debug lines to prevent exceeding the maximum allowed.
     *
     * @param center Center of the debug circle.
     * @param radius Radius of the debug circle.
     * @param color Color of the debug circle.
     * @param lifetime Lifetime of the debug circle.
     * @param segments Number of segments for the debug circle.
     */
    public static void addCircle(Vector2f center, float radius, Vector3f color, int lifetime, int segments) {
        float angleIncrement = 2.0f * (float) Math.PI / segments;

        Vector2f[] vertices = new Vector2f[segments];

        for (int i = 0; i < segments; i++) {
            float angle = i * angleIncrement;
            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);
            vertices[i] = new Vector2f(x, y);
        }

        for (int i = 0; i < segments; i++) {
            int nextIndex = (i + 1) % segments;
            addLine(vertices[i], vertices[nextIndex], color, lifetime);
        }
    }

    // ============================ Add Debug Triangle ============================
    /**
     * Adds a debug triangle with default color and lifetime.
     *
     * @param vertex1 First vertex of the debug triangle.
     * @param vertex2 Second vertex of the debug triangle.
     * @param vertex3 Third vertex of the debug triangle.
     */
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3) {
        addTriangle(vertex1, vertex2, vertex3, new Vector3f(0, 0, 1), 1);
    }

    /**
     * Adds a debug triangle with a specified lifetime.
     *
     * @param vertex1 First vertex of the debug triangle.
     * @param vertex2 Second vertex of the debug triangle.
     * @param vertex3 Third vertex of the debug triangle.
     * @param lifeTime Lifetime of the debug triangle.
     */
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, int lifeTime) {
        addTriangle(vertex1, vertex2, vertex3, new Vector3f(0, 0, 1), lifeTime);
    }

    /**
     * Adds a debug triangle with a specified color and default lifetime.
     *
     * @param vertex1 First vertex of the debug triangle.
     * @param vertex2 Second vertex of the debug triangle.
     * @param vertex3 Third vertex of the debug triangle.
     * @param color Color of the debug triangle.
     */
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, Vector3f color) {
        addTriangle(vertex1, vertex2, vertex3, color, 1);
    }

    /**
     * Adds a debug triangle with a specified color and lifetime.
     * Limits the number of debug lines to prevent exceeding the maximum allowed.
     *
     * @param vertex1 First vertex of the debug triangle.
     * @param vertex2 Second vertex of the debug triangle.
     * @param vertex3 Third vertex of the debug triangle.
     * @param color Color of the debug triangle.
     * @param lifetime Lifetime of the debug triangle.
     */
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, Vector3f color, int lifetime) {
        addLine(vertex1, vertex2, color, lifetime);
        addLine(vertex2, vertex3, color, lifetime);
        addLine(vertex3, vertex1, color, lifetime);
    }
}
/*End of DebugUtils class*/
