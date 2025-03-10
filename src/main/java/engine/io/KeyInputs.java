/*
 Title: KeyInput
 Date: 2023-11-06
 Author: Kyle St John
 */

package engine.io;

import static org.lwjgl.glfw.GLFW.*;

public class KeyInputs {

    private static final boolean[] keyCodes = new boolean[GLFW_KEY_LAST];

    public static void keyCallBack(long window, int keyPressed, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            keyCodes[keyPressed] = true;
        } else if(action == GLFW_RELEASE) {
            keyCodes[keyPressed] = false;
        }
    }

    public static boolean keyPressed(int keyCode) {
        return keyCodes[keyCode];
    }
}
/*End of KeyInput class*/
