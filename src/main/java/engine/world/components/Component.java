/*
 Title: BaseComponent
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.world.components;

import engine.world.objects.GameObject;

/** BaseComponent class representing the foundation for entity components. */
public abstract class Component {

    private int entityId; // Unique identifier for the component
    public GameObject owningGameObject = null; // Reference to the owning GameObject

    /**
     * Gets the owning GameObject of the component.
     *
     * @return The owning GameObject.
     */
    public GameObject getOwningGameObject() {
        return owningGameObject;
    }

    /**
     * Initializes the component.
     */
    public void init() {
        // Initialize the component

        // Set the componentID
        setComponentID();

        // Attach to the game object
    }

    /**
     * Updates the component.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    public void tick(float deltaTime) {
        // Update the component
    }

    /**
     * Sets a unique identifier for the component (local ID system).
     */
    private void setComponentID() {
        // TODO: Implement a local ID system for components
        entityId = 0;
    }

    /**
     * Assigns the owning GameObject to the component.
     *
     * @param gameObject The GameObject that owns this component.
     */
    public void assignOwningObject(GameObject gameObject) {
        this.owningGameObject = gameObject;
    }

    /**
     * Gets the unique identifier of the component.
     *
     * @return The component's unique identifier.
     */
    public int getComponentID() {
        return entityId;
    }
}
/*End of BaseComponent class*/
