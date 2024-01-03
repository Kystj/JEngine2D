/*
 Title: BaseComponent
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.components;

import engine.objects.GameObject;

public abstract class BaseComponent {

    // Properties (data fields) representing the state of the entity
    private int entityId;

    public GameObject owningGameObject =  null;

    public GameObject getOwningGameObject() {
        return owningGameObject;
    }


    public void init() {
        // Initialize the component

        // Set the componentID
        setComponentID();

        // Attach to game object
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
