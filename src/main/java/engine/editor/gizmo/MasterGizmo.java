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
import engine.utils.engine.EConstants;
import engine.world.levels.Level;
import engine.world.objects.GameObject;

import static engine.utils.engine.EConstants.GIZMO_GAME_OBJECT_UID;
import static org.lwjgl.glfw.GLFW.*;

public class MasterGizmo implements EventListener {

    private Level currentLevel;
    private Gizmo activeGizmo;

    private boolean isGizmoActive = true;

    public MasterGizmo() {
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
        EventDispatcher.addListener(EConstants.EventType.Save, this);
    }


    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (gameObject != null) {
            if (activeGizmo != null) {
                this.currentLevel.removeGameObject(GIZMO_GAME_OBJECT_UID);
                this.currentLevel.removeGameObject(GIZMO_GAME_OBJECT_UID);

                if (activeGizmo.getClass().isAssignableFrom(PosGizmo.class)) {
                    activeGizmo = new PosGizmo(gameObject);
                }
                if (activeGizmo.getClass().isAssignableFrom(ScaleGizmo.class)) {
                    activeGizmo = new ScaleGizmo(gameObject);
                }
            } else {
                activeGizmo = new PosGizmo(gameObject);
            }
            if (isGizmoActive) {
                activeGizmo.addToScene();
            }
        }
    }

    @Override
    public void onEvent(Event event, Level level) {

    }


    @Override
    public void onEvent(Event event) {
        if (event.getEventType() == EConstants.EventType.Save) {
            activeGizmo.activeGameObject = null;
            this.currentLevel.removeGameObject(GIZMO_GAME_OBJECT_UID);
            this.currentLevel.removeGameObject(GIZMO_GAME_OBJECT_UID);
        }
    }


    public void tick() {
        if (activeGizmo != null) {
            activeGizmo.tick();
        }
        switchGizmos();
    }


    private void switchGizmos() {
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
        }
        if (KeyInputs.keyPressed(GLFW_KEY_Q)) {
            remove();
            isGizmoActive = false;
        }
    }


    public void remove() {
        this.currentLevel.removeGameObject(activeGizmo.posGizmoX.getUID());
        this.currentLevel.removeGameObject(activeGizmo.posGizmoY.getUID());
    }


    public Level getCurrentScene() {
        return currentLevel;
    }


    public void setCurrentScene(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setLevel(Level level) {
        currentLevel = level;
    }
}
/*End of MasterGizmo class*/
