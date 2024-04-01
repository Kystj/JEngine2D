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

import static org.lwjgl.glfw.GLFW.*;

public class MasterGizmo implements EventListener {

    private Scene currentScene;
    private Gizmo activeGizmo;
    private GameObject activeGameObject;

    public MasterGizmo(Scene scene) {
        this.currentScene = scene;
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
    }


    @Override
    public void onEvent(Event event, GameObject gameObject) {
        activeGameObject = gameObject;
        if (activeGizmo != null) {
            this.currentScene.removeGameObject(activeGizmo.getGizmoUIDs()[0]);
            this.currentScene.removeGameObject(activeGizmo.getGizmoUIDs()[0]);

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
        activeGizmo.addToScene();
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
        if (activeGameObject != null) {
            if (KeyInputs.keyPressed(GLFW_KEY_Q)) {
                remove();
                activeGizmo = new PosGizmo(activeGameObject);
                activeGizmo.addToScene();
            }

            if (KeyInputs.keyPressed(GLFW_KEY_E)) {
                remove();
                activeGizmo = new ScaleGizmo(activeGameObject);
                activeGizmo.addToScene();
            }

            if (KeyInputs.keyPressed(GLFW_KEY_R)) {
                remove();
                activeGizmo = new RotateGizmo(activeGameObject);
                activeGizmo.addToScene();
            }
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
