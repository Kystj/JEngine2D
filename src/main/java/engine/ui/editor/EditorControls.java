/*
 Title: EditorMouseController
 Date: 2024-02-28
 Author: Kyle St John
 */
package engine.ui.editor;

import engine.debug.logger.DebugLogger;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.io.MouseInputs;
import engine.ui.engine.EngineWindow;
import engine.settings.EConstants;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;
import org.joml.Vector2f;

import static engine.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.settings.EConstants.DEFAULT_GRID_WIDTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorControls implements EventListener {

    private GameObject selectedObject = null;
    private boolean enableGridSnap = true;

    public EditorControls() {
        EventDispatcher.addListener(EConstants.EventType.Grid_Lock, this);
    }

    public void pickUp(GameObject gameObject) {
        selectedObject = gameObject;
        EngineWindow.get().getCurrentScene().addGameObject(selectedObject);
    }

    public void place() {
        DebugLogger.info("Game object has been added to the scene", true);
        selectedObject = null;
    }

    public void tick(float deltaTime) {
        Vector2f spritePos = new Vector2f();
        if (selectedObject != null) {
            selectedObject.getTransform().position.x = MouseInputs.getOrthoX() + 16;
            selectedObject.getTransform().position.y = MouseInputs.getOrthoY() + 16;

            // Implement snap to grid
            if (enableGridSnap) {
                selectedObject.getTransform().position.x = (int) (selectedObject.getTransform().position.x / DEFAULT_GRID_WIDTH) * DEFAULT_GRID_WIDTH;
                selectedObject.getTransform().position.y = (int) (selectedObject.getTransform().position.y / DEFAULT_GRID_HEIGHT) * DEFAULT_GRID_HEIGHT;
            }
            spritePos.set(selectedObject.getTransform().position.x, selectedObject.getTransform().position.y);

            if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                selectedObject.getComponent(Sprite.class).getTransform().setPosition(spritePos);
                place();
            }
        }
    }


    private void onButtonClick() {
        enableGridSnap = !enableGridSnap;
    }

    @Override
    public void onEvent(GameObject gameObject, Event event) {

    }

    @Override
    public void onEvent(Event event) {
        if (event.getEventType() == EConstants.EventType.Grid_Lock) {
           onButtonClick();
        }
    }
}
/*End of EditorMouseController class*/
