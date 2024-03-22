/*
 Title: MouseInputs
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.io;

import engine.graphics.OrthographicCamera;
import engine.ui.editor.Viewport;
import engine.ui.engine.EngineWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseInputs {

    private static double scrollX, scrollY;
    private static double xPos, yPos, lastY, lastX;
    private static boolean[] mouseButtonPressed = new boolean[9];
    private static boolean isDragging;

    private static Vector2f viewportPos = new Vector2f();
    private static Vector2f viewportSize = new Vector2f();

    private MouseInputs() {
        scrollX = 0.0;
        scrollY = 0.0;
        xPos = 0.0;
        yPos = 0.0;
        lastX = 0.0;
        lastY = 0.0;
    }


    public static void mousePosCallback(long window, double xpos, double ypos) {
        lastX = xPos;
        lastY = yPos;
        xPos = xpos;
        yPos = ypos;
        isDragging = mouseButtonPressed[0] || mouseButtonPressed[1] || mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < mouseButtonPressed.length) {
                mouseButtonPressed[button] = false;
               isDragging = false;
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


    public static float getScreenX() {
        float currentX = getX() - Viewport.getViewportPos().x;
        currentX = (currentX / Viewport.getViewportSize().x) * EngineWindow.get().getWindowWidth();
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - Viewport.getViewportPos().y;
        currentY = 1440.0f - ((currentY / Viewport.getViewportSize().y) * EngineWindow.get().getWindowHeight());
        return currentY;
    }

    public static float getX() {
        return (float) xPos;
    }

    public static float getY() {
        return (float) yPos;
    }


    public static float getOrthoX() {
         float currentX = getX() - viewportPos.x;
        currentX = (currentX / viewportSize.x) * 2.0f - 1.015f;
        OrthographicCamera camera = EngineWindow.get().getCamera();

        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);
        tmp.mul(camera.getInverseProjection()).mul(camera.getInverseView());
        currentX = tmp.x;

        return currentX;
    }

    public static float getOrthoY() {
        float currentY = getY() - viewportPos.y;
        currentY = -((currentY / viewportSize.y) * 2.0f - 1.0f);
        OrthographicCamera camera = EngineWindow.get().getCamera();

        Vector4f tmp = new Vector4f(0, currentY, 0, 1);
        tmp.mul(camera.getInverseProjection()).mul(camera.getInverseView());
        currentY = tmp.y;

        return currentY;
    }

    public static float getDx() {
        return (float) (lastX - xPos);
    }

    public static float getDy() {
        return (float) (lastY - yPos);
    }

    public static float getScrollX() {
        return (float) scrollX;
    }

    public static float getScrollY() {
        return (float) scrollY;
    }

    public static boolean isDragging() {
        return isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < mouseButtonPressed.length) {
            return mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public static void setViewportPos(Vector2f pos) {
        viewportPos = pos;
    }

    public static void setViewportSize(Vector2f size) {
        viewportSize = size;
    }
}
/*End of MouseInputs class*/
