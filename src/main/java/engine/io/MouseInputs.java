/*
 Title: MouseInputs
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.io;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInputs {

    private static double scrollX = 0.0, scrollY = 0.0;
    private static double xPos = 0.0, yPos = 0.0, lastY = 0.0, lastX = 0.0;
    private static final boolean[] mouseButtons = new boolean[3];


    public static void mousePosCallback(long window, double xpos, double ypos) {
        lastX = xPos;
        lastY = yPos;
        xPos = xpos;
        yPos = ypos;
    }

    public static boolean getMouseButtonPressed(int button) {
        if (button < mouseButtons.length) {
            return mouseButtons[button];
        } else {
            return false;
        }
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < mouseButtons.length) {
                mouseButtons[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < mouseButtons.length) {
                mouseButtons[button] = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        scrollX = xOffset;
        scrollY = yOffset;
    }

    public static void endFrame() {
        scrollX = 0;
        scrollY = 0;
        lastX = xPos;
        lastY = yPos;
    }

    public static float getX() {
        return (float)xPos;
    }

    public static float getY() {
        return (float)yPos;
    }

    public static float getDx() {
        return (float)(lastX - xPos);
    }

    public static float getDy() {
        return (float)(lastY - yPos);
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }


    public static boolean mouseButtonDown(int button) {
        if (button < mouseButtons.length) {
            return mouseButtons[button];
        } else {
            return false;
        }
    }
}
/*End of MouseInputs class*/
