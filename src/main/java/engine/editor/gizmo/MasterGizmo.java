/*
 Title: MasterGizmo
 Date: 2024-03-26
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.io.KeyInputs;
import engine.utils.EConstants;
import engine.world.objects.GameObject;
import engine.world.scenes.Scene;

import static engine.utils.EConstants.GIZMO_GAME_OBJECT_UID;
import static org.lwjgl.glfw.GLFW.*;

public class MasterGizmo implements EventListener {

    private Scene currentScene;
    private Gizmo activeGizmo;

    private boolean isGizmoActive = true;

    public MasterGizmo(Scene scene) {
        this.currentScene = scene;
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
    }


    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (activeGizmo != null) {
            this.currentScene.removeGameObject(GIZMO_GAME_OBJECT_UID);
            this.currentScene.removeGameObject(GIZMO_GAME_OBJECT_UID);

            if (activeGizmo.getClass().isAssignableFrom(PosGizmo.class)) {
                activeGizmo = new PosGizmo(gameObject);
            }
            if (activeGizmo.getClass().isAssignableFrom(ScaleGizmo.class)) {
                activeGizmo = new ScaleGizmo(gameObject);
            }
            if (activeGizmo.getClass().isAssignableFrom(RotateGizmo.class)) {
                activeGizmo = new RotateGizmo(gameObject);
            }
        } else {
            activeGizmo = new PosGizmo(gameObject);
        }
        if (isGizmoActive) {
            activeGizmo.addToScene();
        }
    }

    @Override
    public void onEvent(Event event, Scene scene) {

    }


    @Override
    public void onEvent(Event event) {

    }


    public void tick() {
        if (activeGizmo != null) {
            activeGizmo.tick();
        }
        switchGizmos();
    }


    private void switchGizmos() {
        // TODO: Add an explanation in the shortcuts window
        if (activeGizmo != null) {
            if (KeyInputs.keyPressed(GLFW_KEY_T)) {
                remove();
                isGizmoActive = true;
                activeGizmo = new PosGizmo(activeGizmo.activeGameObject);
                activeGizmo.addToScene();
            }

            if (KeyInputs.keyPressed(GLFW_KEY_E)) {
                remove();
                isGizmoActive = true;
                activeGizmo = new ScaleGizmo(activeGizmo.activeGameObject);
                activeGizmo.addToScene();
            }

            if (KeyInputs.keyPressed(GLFW_KEY_R)) {
                remove();
                isGizmoActive = true;
                activeGizmo = new RotateGizmo(activeGizmo.activeGameObject);
                activeGizmo.addToScene();
            }
        }
        if (KeyInputs.keyPressed(GLFW_KEY_Q)) {
            remove();
            isGizmoActive = false;
        }
    }


    public void remove() {
        this.currentScene.removeGameObject(activeGizmo.posGizmoX.getUID());
        this.currentScene.removeGameObject(activeGizmo.posGizmoY.getUID());
    }


    public Scene getCurrentScene() {
        return currentScene;
    }


    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }
}
/*End of MasterGizmo class*/
