/*
 Title: Line
 Date: 2024-01-24
 Author: Kyle St John
 */
package engine.debug;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class DebugLine {

    private Vector2f lineStart;
    private Vector2f lineEnd;
    private Vector3f lineColor;
    private int lineLife;

    public DebugLine(Vector2f lineStart, Vector2f lineEnd, Vector3f lineColor, int lineLife) {
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
        this.lineColor = lineColor;
        this.lineLife = lineLife;
    }

    public boolean isLineLive() {
        this.lineLife--;
        return lineLife >= 0;
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
