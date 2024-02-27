/*
 Title: BaseComponent
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.world.components;

import engine.world.objects.GameObject;

public abstract class Component {

    private int entityId; // Unique identifier for the component
    public transient GameObject owningGameObject = null; // Reference to the owning GameObject

    public GameObject getOwningGameObject() {
        return owningGameObject;
    }

    public void init() {
        // Initialize the component

        // Set the componentID
        setComponentID();

        // Attach to the game object
    }

    public void tick(float deltaTime) {
        // Update the component
    }

    private void setComponentID() {
        // TODO: Implement a local ID system for components
        entityId = 0;
    }

    public void assignOwningObject(GameObject gameObject) {
        this.owningGameObject = gameObject;
    }

    public int getComponentID() {
        return entityId;
    }
}
/*End of BaseComponent class*/
