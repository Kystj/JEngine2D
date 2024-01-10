/*
 Title: GameObject
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.objects;

import engine.components.BaseComponent;
import engine.components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final String name;
    private final List<BaseComponent> componentsList = new ArrayList<>();
    public Transform transform;

    /** Constructor for the GameObject class, assigns the and initializes the GameObject class variables */
    public GameObject(String name, Transform transform) {
        this.name = name;
        this.transform = transform;
    }

    /** Initializes the game object and its components then assigns its global ID value*/
    public void init(){
        for (int i=0; i < componentsList.size(); i++) {
            componentsList.get(i).init();
        }
        // TODO: Create a global object ID system and call it here, preform other initialization here
    }

    /** Update all the Game objects components*/
    public void update(float dt) {
        for (BaseComponent component : componentsList) {
            component.tick(dt);
        }
    }

    /** Retrieves a component of the specified type from the components list.*/
    public <T extends BaseComponent> T getComponent(Class<T> component) {
        for (BaseComponent comp : componentsList) {
            if (component.isInstance(comp)) {
                try {
                    return component.cast(comp);
                } catch (ClassCastException e) {
                    handleComponentCastException(e);
                }
            }
        }
        return null;
    }

    /** Removes a component of the specified type from the components list.*/
    public <T extends BaseComponent> void removeComponent(Class<T> component) {
        for (int i = 0; i < componentsList.size(); i++) {
            BaseComponent comp = componentsList.get(i);
            if (component.isAssignableFrom(comp.getClass())) {
                componentsList.remove(i);
                return;
            }
        }
    }

    public void addComponent(BaseComponent component) {
        System.out.println("Adding component " + component.getClass().getSimpleName() + " to GameObject " + name);
        this.componentsList.add(component);
        component.assignOwningObject(this);
    }

    /** Handles the case in which a cast exception occurs in either the getComponent() or
     * removeComponent() methods*/
    private void handleComponentCastException(ClassCastException e) {
        e.printStackTrace();
        assert false : "Error: Casting component.";
    }

    public String getName() {
        return name;
    }

    public List<BaseComponent> getComponentsList() {
        return componentsList;
    }

    public Transform getTransform() {
        return transform;
    }

    public Transform setTransform(Transform newTransform) {
        return transform;
    }
}
/*End of GameObject class*/
