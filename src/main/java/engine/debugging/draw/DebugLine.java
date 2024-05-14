/*
 Title: Line
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debugging.draw;

import org.joml.Vector2f;
import org.joml.Vector3f;


public class DebugLine {

    private Vector2f lineStart;   // Starting point of the debug line
    private Vector2f lineEnd;     // Ending point of the debug line
    private Vector3f lineColor;   // Color of the debug line
    private int lineLife;         // Remaining life of the debug line
    boolean isPersistent;
    boolean debugLine = true;

    public DebugLine(Vector2f lineStart, Vector2f lineEnd, Vector3f lineColor, int lineLife, boolean bIsPersistent) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.lineColor = lineColor;
        this.lineLife = lineLife;
        this.isPersistent = bIsPersistent;
    }

    public int isLineLive() {
        this.lineLife--;
        return this.lineLife;
    }


    public Vector2f getLineStart() {
        return lineStart;
    }


    public void setLineStart(Vector2f lineStart) {
        this.lineStart = lineStart;
    }


    public Vector2f getLineEnd() {
        return lineEnd;
    }


    public void setLineEnd(Vector2f lineEnd) {
        this.lineEnd = lineEnd;
    }


    public Vector3f getLineColor() {
        return lineColor;
    }


    public void setLineColor(Vector3f lineColor) {
        this.lineColor = lineColor;
    }


    public int getLineLife() {
        return lineLife;
    }


    public void setLineLife(int lineLife) {
        this.lineLife = lineLife;
    }
}
/*End of Line class*/