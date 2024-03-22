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
import engine.settings.EConstants;
import engine.ui.engine.EngineWindow;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;
import org.joml.Vector2f;

import static engine.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.settings.EConstants.DEFAULT_GRID_WIDTH;
import static engine.ui.editor.EditorScene.objectPicker;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorControls implements EventListener {

    private static GameObject activeObject = null;
    private boolean enableGridSnap = true;
    private float debounce = 0.8f;

    public EditorControls() {
        EventDispatcher.addListener(EConstants.EventType.Grid_Lock, this);
    }


    @Override
    public void onEvent(Event event, GameObject gameObject) {

    }

    @Override
    public void onEvent(Event event) {
        if (event.getEventType() == EConstants.EventType.Grid_Lock) {
            onButtonClick();
        }
    }


    private void onButtonClick() {
        enableGridSnap = !enableGridSnap;
    }



    public void tick(float deltaTime) {
        handleObjectSelection(deltaTime);
        Vector2f spritePos = new Vector2f();
        if (activeObject != null) {
            activeObject.getTransform().position.x = MouseInputs.getOrthoX() + 16;
            activeObject.getTransform().position.y = MouseInputs.getOrthoY() + 16;

            // Implement snap to grid
            if (enableGridSnap) {
                activeObject.getTransform().position.x = (int) (activeObject.getTransform().position.x / DEFAULT_GRID_WIDTH) * DEFAULT_GRID_WIDTH;
                activeObject.getTransform().position.y = (int) (activeObject.getTransform().position.y / DEFAULT_GRID_HEIGHT) * DEFAULT_GRID_HEIGHT;
            }
            spritePos.set(activeObject.getTransform().position.x, activeObject.getTransform().position.y);

            if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                activeObject.getComponent(Sprite.class).getTransform().setPosition(spritePos);
                place();
            }
        }
    }


    public void place() {
        DebugLogger.warning("Game Object with UID: " + activeObject.getUID() +" has been added to the scene", true);
        activeObject = null;
    }


    public static void pickUp(GameObject gameObject) {
        activeObject = gameObject;
        EngineWindow.get().getCurrentScene().addGameObject(activeObject);
        EventDispatcher.dispatchEvent(new Event(EConstants.EventType.User), gameObject);
    }


    public void handleObjectSelection(float dt) {
        debounce -= dt;

        if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseInputs.getScreenX();
            int y = (int) MouseInputs.getScreenY();

            int gameObjectId = objectPicker.readObjectIDByPixel(x, y);

            GameObject pickedObj = EngineWindow.get().getCurrentScene().getGameObject(gameObjectId);
            if (pickedObj != null) {
                EventDispatcher.dispatchEvent(new Event(EConstants.EventType.User), pickedObj);
            }
            this.debounce = 0.8f;
        }
    }
}
/*End of EditorMouseController class*/
