/*
 Title: MathUtils
 Date: 2024-02-22
 Author: Kyle St John
 */
package engine.utils;

import org.joml.Vector2f;

public class MathUtils {

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

    public static Vector2f calculateCentroid(Vector2f v1, Vector2f v2, Vector2f v3) {
        // Calculate the average x-coordinate and y-coordinate of the vertices
        return new Vector2f((v1.x + v2.x + v3.x) / 3.0f, (v1.y + v2.y + v3.y) / 3.0f);
    }

    public static String decimalToAspectRatio(float decimal) {
        // Round the decimal to a reasonable precision
        decimal = Math.round(decimal * 10000f) / 10000f;

        // Define an error margin for comparing float values
        float epsilon = 0.001f;

        // Check for standard aspect ratios
        if (Math.abs(decimal - (16.0f / 9.0f)) < epsilon) {
            return "16:9";
        } else if (Math.abs(decimal - (16.0f / 10.0f)) < epsilon) {
            return "16:10";
        } else if (Math.abs(decimal - (21.0f / 9.0f)) < epsilon) {
            return "21:9";
        } else if (Math.abs(decimal - (4.0f / 3.0f)) < epsilon) {
            return "4:3";
        }

        // If none of the standard aspect ratios match, approximate the ratio
        int numerator = 1;
        int denominator = 1;

        // Keep multiplying until the decimal is close enough to an integer
        while (Math.abs(decimal - (float)numerator / denominator) > epsilon) {
            if (decimal > (float)numerator / denominator) {
                numerator++;
            } else {
                denominator++;
            }
        }

        return numerator + ":" + denominator;
    }
}
/*End of MathUtils class*/
