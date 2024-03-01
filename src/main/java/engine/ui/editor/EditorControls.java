/*
 Title: EditorMouseController
 Date: 2024-02-28
 Author: Kyle St John
 */
package engine.ui.editor;

import engine.io.MouseInputs;
import engine.ui.engine.EngineWindow;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;
import org.joml.Vector2f;

import static engine.ui.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.ui.settings.EConstants.DEFAULT_GRID_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorControls {

    GameObject selectedObject = null;

    public void pickUp(GameObject gameObject) {
        selectedObject = gameObject;
        EngineWindow.get().getCurrentScene().addGameObject(selectedObject);
    }

    public void place() {
        selectedObject = null;
    }

    public void tick(float deltaTime) {
        Vector2f spritePos = new Vector2f();
        if (selectedObject != null) {
            selectedObject.getTransform().position.x = MouseInputs.getOrthoX() + 16;
            selectedObject.getTransform().position.y = MouseInputs.getOrthoY() + 16;
            // Snap to grid
            selectedObject.getTransform().position.x = (int)(selectedObject.getTransform().position.x / DEFAULT_GRID_WIDTH) * DEFAULT_GRID_WIDTH;
            selectedObject.getTransform().position.y = (int)(selectedObject.getTransform().position.y / DEFAULT_GRID_HEIGHT) * DEFAULT_GRID_HEIGHT;

            spritePos.set(selectedObject.getTransform().position.x, selectedObject.getTransform().position.y);

            if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                // TODO: Fix this
                selectedObject.getComponent(Sprite.class).getTransform().setPosition(spritePos);
                place();
            }
        }
    }
}
/*End of EditorMouseController class*/
