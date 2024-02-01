/*
 Title: DebugUtils
 Date: 2024-02-01
 Author: Kyle St John
 */
package engine.debug;

import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.settings.EConstants.MAX_DEBUG_LINES;

public class DebugUtils {

    // TODO: Implement rotation for each shape

    // ============================ Add Debug Line ============================
    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        addLine(from, to, color, 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (DebugDraw.getDebugLines().size() >= MAX_DEBUG_LINES) return;
        DebugDraw.getDebugLines().add(new DebugLine(from, to, color, lifetime));
    }

    // ============================ Add Debug Box ============================
    public static void addBox(Vector2f center, Vector2f dimensions, float rotation) {
        addBox(center, dimensions, rotation, new Vector3f(0, 1, 0), 1);
    }

    public static void addBox(Vector2f center, Vector2f dimensions, float rotation, Vector3f color) {
        addBox(center, dimensions, rotation, color, 1);
    }

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
    public static void addCircle(Vector2f center, float radius, int segments) {
        addCircle(center, radius, new Vector3f(0, 1, 0), 1, segments);
    }

    public static void addCircle(Vector2f center, float radius, Vector3f color) {
        addCircle(center, radius, color, 1, 32); // Using 32 segments for a smooth circle
    }

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
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3) {
        addTriangle(vertex1, vertex2, vertex3, new Vector3f(0, 0, 1), 1);
    }

    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, int lifeTime) {
        addTriangle(vertex1, vertex2, vertex3, new Vector3f(0, 0, 1), lifeTime);
    }


    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, Vector3f color) {
        addTriangle(vertex1, vertex2, vertex3, color, 1);
    }

    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, Vector3f color, int lifetime) {
        addLine(vertex1, vertex2, color, lifetime);
        addLine(vertex2, vertex3, color, lifetime);
        addLine(vertex3, vertex1, color, lifetime);
    }
}
/*End of DebugUtils class*/
