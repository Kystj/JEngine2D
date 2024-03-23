/*
 Title: DebugUtils
 Date: 2024-02-01
 Author: Kyle St John
 */
package engine.debug.draw;

import engine.utils.MathUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.utils.EConstants.MAX_DEBUG_LINES;

public class DebugDraw {

    // ============================ Add Debug Line ============================
    public static void addLine(Vector2f from, Vector2f to) {
        addLine(from, to, new Vector3f(0, 1, 0), 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color) {
        addLine(from, to, color, 1);
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color, boolean bIsPersistent) {
        if (DebugRenderer.getDebugLines().size() >= MAX_DEBUG_LINES) return;
        DebugRenderer.getDebugLines().add(new DebugLine(from, to, color, -1, bIsPersistent));
    }

    public static void addLine(Vector2f from, Vector2f to, Vector3f color, int lifetime) {
        if (DebugRenderer.getDebugLines().size() >= MAX_DEBUG_LINES) return;
        DebugRenderer.getDebugLines().add(new DebugLine(from, to, color, lifetime, false));
    }

    // ============================ Add Debug Box ============================
    public static void addBox(Vector2f center, Vector2f dimensions, float rotation, Vector3f color, boolean bIsPersistent) {
        addBox(center, dimensions, rotation, color, 1, bIsPersistent);
    }


    public static void addBox(Vector2f center, Vector2f dimensions, float rotation,
                              Vector3f color, int lifetime, boolean bIsPersistent) {
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimensions).mul(0.5f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimensions).mul(0.5f));

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if (rotation != 0.0f) {
            for (Vector2f vert : vertices) {
                MathUtils.rotate(vert, rotation, center);
            }
        }

        for (int i = 0; i < 4; i++) {
            int nextIndex = (i + 1) % 4;
            if (!bIsPersistent) {
                addLine(vertices[i], vertices[nextIndex], color, lifetime);
            } else {
                addLine(vertices[i], vertices[nextIndex], color, true);
            }
        }
    }

    // ============================ Add Debug Circle ============================
    public static void addCircle(Vector2f center, float radius, float rotationAngle,
                                 Vector3f color, int segments, boolean bIsPersistent) {
        addCircle(center, radius, rotationAngle, color, 1, segments, bIsPersistent);
    }


    public static void addCircle(Vector2f center, float radius, float rotationAngle, Vector3f color, int lifetime, int segments,
                                 boolean bIsPersistent) {

        float angleIncrement = 2.0f * (float) Math.PI / segments;
        Vector2f[] vertices = new Vector2f[segments];

        for (int i = 0; i < segments; i++) {
            float angle = i * angleIncrement + rotationAngle; // Add rotation angle
            float x = center.x + radius * (float) Math.cos(angle);
            float y = center.y + radius * (float) Math.sin(angle);
            vertices[i] = new Vector2f(x, y);
        }

        for (int i = 0; i < segments; i++) {
            int nextIndex = (i + 1) % segments;
            if (!bIsPersistent) {
                addLine(vertices[i], vertices[nextIndex], color, lifetime);
            } else {
                addLine(vertices[i], vertices[nextIndex], color, true);
            }
        }
    }

    // ============================ Add Debug Triangle ============================
    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3,
                                   Vector3f color, boolean bIsPersistent, float rotationAngle) {
        addTriangle(vertex1, vertex2, vertex3, color,1, bIsPersistent, rotationAngle);
    }


    public static void addTriangle(Vector2f vertex1, Vector2f vertex2, Vector2f vertex3, Vector3f color,
                                   int lifetime, boolean bIsPersistent, float rotationAngle) {
        // Calculate the centroid of the triangle
        Vector2f centroid = MathUtils.calculateCentroid(vertex1, vertex2, vertex3);

        // Apply rotation to each vertex around the centroid
        MathUtils.rotate(vertex1, rotationAngle, centroid);
        MathUtils.rotate(vertex2, rotationAngle, centroid);
        MathUtils.rotate(vertex3, rotationAngle, centroid);

        if (!bIsPersistent) {
            addLine(vertex1, vertex2, color, lifetime);
            addLine(vertex2, vertex3, color, lifetime);
            addLine(vertex3, vertex1, color, lifetime);
        } else {
            addLine(vertex1, vertex2, color, true);
            addLine(vertex2, vertex3, color, true);
            addLine(vertex3, vertex1, color, true);
        }
    }
}
/*End of DebugUtils class*/
