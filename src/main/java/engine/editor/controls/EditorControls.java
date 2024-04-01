/*
 Title: EditorMouseController
 Date: 2024-02-28
 Author: Kyle St John
 */
package engine.editor.controls;

import engine.debug.info.DebugLogger;
import engine.editor.gizmo.MasterGizmo;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.EngineWindow;
import engine.io.KeyInputs;
import engine.io.MouseInputs;
import engine.utils.EConstants;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;
import engine.world.scenes.Scene;
import org.joml.Vector2f;

import static engine.editor.ui.EditorScene.objectPicker;
import static engine.utils.EConstants.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class EditorControls implements EventListener {

    private static GameObject active = null;
    private boolean enableGridSnap = true;
    private float debounce = 0.5f;
    private Scene currentScene;
    private final MasterGizmo masterGizmo;

    public EditorControls(Scene scene) {
        this.currentScene = scene;
        this.masterGizmo = new MasterGizmo(scene);
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
        masterGizmo.tick();
        handleObjectSelection(deltaTime);
        deleteGameObject();

        Vector2f spritePos = new Vector2f();
        if (active != null) {
            active.getTransform().position.x = MouseInputs.getOrthoX() + 16;
            active.getTransform().position.y = MouseInputs.getOrthoY() + 16;

            // Implement snap to grid
            if (enableGridSnap) {
                active.getTransform().position.x = (int) (active.getTransform().position.x / DEFAULT_GRID_WIDTH) * DEFAULT_GRID_WIDTH;
                active.getTransform().position.y = (int) (active.getTransform().position.y / DEFAULT_GRID_HEIGHT) * DEFAULT_GRID_HEIGHT;
            }
            spritePos.set(active.getTransform().position.x, active.getTransform().position.y);

            if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                active.getComponent(Sprite.class).getTransform().setPosition(spritePos);
                place();
            }
        }
    }


    public void place() {
        DebugLogger.warning("Game Object with UID: " + active.getUID() +" has been added to the scene", true);
        active = null;
    }


    public void deleteGameObject() {
        if (this.currentScene.getActiveGameObject() != null && KeyInputs.keyPressed(GLFW_KEY_DELETE)) {
            this.currentScene.removeGameObject(this.currentScene.getActiveGameObject().getUID());
            masterGizmo.remove();
            active = null;
        }
    }


    public static void selectAsset(GameObject gameObject) {
        active = gameObject;
        EngineWindow.get().getCurrentScene().addGameObject(active);
        EventDispatcher.dispatchEvent(new Event(EConstants.EventType.New_Asset), gameObject);
    }


    public void handleObjectSelection(float dt) {
        debounce -= dt;

        if (MouseInputs.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseInputs.getScreenX();
            int y = (int) MouseInputs.getScreenY();

            int gameObjectId = objectPicker.readObjectIDByPixel(x, y);
            GameObject pickedObj = EngineWindow.get().getCurrentScene().getGameObject(gameObjectId);

            if (pickedObj != null && gameObjectId != GIZMO_GAME_OBJECT_UID) {
                EventDispatcher.dispatchEvent(new Event(EConstants.EventType.Active_Object), pickedObj);
            }
            this.debounce = 0.5f;
        }
    }
}
/*End of EditorMouseController class*/
