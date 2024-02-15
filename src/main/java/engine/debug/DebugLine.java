/*
 Title: Line
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debug;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * The DebugLine class represents a single debug line in the game engine. It holds information
 * about the line's start and end points, color, and remaining life.
 */
public class DebugLine {

    private Vector2f lineStart;   // Starting point of the debug line
    private Vector2f lineEnd;     // Ending point of the debug line
    private Vector3f lineColor;   // Color of the debug line
    private int lineLife;         // Remaining life of the debug line

    /**
     * Constructs a DebugLine with specified parameters.
     *
     * @param lineStart Starting point of the debug line.
     * @param lineEnd Ending point of the debug line.
     * @param lineColor Color of the debug line.
     * @param lineLife Remaining life of the debug line.
     */
    public DebugLine(Vector2f lineStart, Vector2f lineEnd, Vector3f lineColor, int lineLife) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.lineColor = lineColor;
        this.lineLife = lineLife;
    }

    /**
     * Decreases the remaining life of the debug line and checks if the line is still live.
     *
     * @return The life of the line remaining.
     */
    public int isLineLive() {
        this.lineLife--;
        return this.lineLife;
    }

    /**
     * Gets the starting point of the debug line.
     *
     * @return Vector2f representing the starting point.
     */
    public Vector2f getLineStart() {
        return lineStart;
    }

    /**
     * Sets the starting point of the debug line.
     *
     * @param lineStart New starting point for the debug line.
     */
    public void setLineStart(Vector2f lineStart) {
        this.lineStart = lineStart;
    }

    /**
     * Gets the ending point of the debug line.
     *
     * @return Vector2f representing the ending point.
     */
    public Vector2f getLineEnd() {
        return lineEnd;
    }

    /**
     * Sets the ending point of the debug line.
     *
     * @param lineEnd New ending point for the debug line.
     */
    public void setLineEnd(Vector2f lineEnd) {
        this.lineEnd = lineEnd;
    }

    /**
     * Gets the color of the debug line.
     *
     * @return Vector3f representing the color.
     */
    public Vector3f getLineColor() {
        return lineColor;
    }

    /**
     * Sets the color of the debug line.
     *
     * @param lineColor New color for the debug line.
     */
    public void setLineColor(Vector3f lineColor) {
        this.lineColor = lineColor;
    }

    /**
     * Gets the remaining life of the debug line.
     *
     * @return Remaining life of the debug line.
     */
    public int getLineLife() {
        return lineLife;
    }

    /**
     * Sets the remaining life of the debug line.
     *
     * @param lineLife New remaining life for the debug line.
     */
    public void setLineLife(int lineLife) {
        this.lineLife = lineLife;
    }
}
/*End of Line class*/