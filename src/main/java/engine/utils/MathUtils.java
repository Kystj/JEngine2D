/*
 Title: MathUtils
 Date: 2024-02-22
 Author: Kyle St John
 */
package engine.utils;

import org.joml.Vector2f;

public class MathUtils {
    /**
     * Rotates a 2D vector around a specified origin by a given angle in degrees.
     * @param vec The vector to rotate.
     * @param angleDeg The angle of rotation in degrees.
     * @param origin The origin point around which to rotate the vector.
     */
    public static void rotate(Vector2f vec, float angleDeg, Vector2f origin) {
        // Calculate the relative position of the vector with respect to the origin
        float x = vec.x - origin.x;
        float y = vec.y - origin.y;

        // Convert the angle from degrees to radians and compute sine and cosine
        float cos = (float)Math.cos(Math.toRadians(angleDeg));
        float sin = (float)Math.sin(Math.toRadians(angleDeg));

        // Apply the rotation transformation
        float xPrime = x * cos - y * sin + origin.x; // Calculate the rotated x-coordinate
        float yPrime = x * sin + y * cos + origin.y; // Calculate the rotated y-coordinate

        // Update the original vector with the rotated coordinates
        vec.set(xPrime, yPrime);
    }

    /**
     * Helper method to calculate the centroid of a triangle.
     * @param v1 The first vertex of the triangle.
     * @param v2 The second vertex of the triangle.
     * @param v3 The third vertex of the triangle.
     * @return The centroid of the triangle as a Vector2f.
     */
    public static Vector2f calculateCentroid(Vector2f v1, Vector2f v2, Vector2f v3) {
        // Calculate the average x-coordinate and y-coordinate of the vertices
        return new Vector2f((v1.x + v2.x + v3.x) / 3.0f, (v1.y + v2.y + v3.y) / 3.0f);
    }
}
/*End of MathUtils class*/
