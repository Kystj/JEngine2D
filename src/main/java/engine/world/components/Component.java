/*
 Title: BaseComponent
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.world.components;

import engine.debug.logger.DebugLogger;
import engine.world.objects.GameObject;

public abstract class Component {

    private static int GLOBAL_COMP_ID_COUNTER = -1;
    private int componentUID;
    public transient GameObject owningGameObject = null;

    public void init() {
        generateUniqueId();
        DebugLogger.warning("Component with UID: " + componentUID + " has been created");
    }

    public void tick(float deltaTime) {

    }

    private synchronized void generateUniqueId() {
        this.componentUID = ++GLOBAL_COMP_ID_COUNTER;
    }

    public void assignOwningObject(GameObject gameObject) {
        this.owningGameObject = gameObject;
    }

    public GameObject getOwningGameObject() {
        return owningGameObject;
    }

    public int getComponentID() {
        return componentUID;
    }
}
/*End of BaseComponent class*/
