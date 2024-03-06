/*
 Title: GameObject
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.world.objects;

import engine.world.components.Component;
import engine.world.components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private final String name;
    private final List<Component> componentsList = new ArrayList<>();
    private Transform transform;

    private int zIndex;

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public void init(){
        for (Component component : componentsList) {
            component.init();
        }
        // TODO: Create a global object ID system and call it here, preform other initialization here
    }

    public void update(float dt) {
        for (Component component : componentsList) {
            component.tick(dt);
        }
    }

    public <T extends Component> T getComponent(Class<T> component) {
        for (Component comp : componentsList) {
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


    public <T extends Component> void removeComponent(Class<T> component) {
        for (int i = 0; i < componentsList.size(); i++) {
            Component comp = componentsList.get(i);
            if (component.isAssignableFrom(comp.getClass())) {
                componentsList.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        component.assignOwningObject(this);
        this.componentsList.add(component);
    }


    private void handleComponentCastException(ClassCastException e) {
        e.printStackTrace();
        assert false : "Error: Casting component.";
    }


    public List<Component> getComponentsList() {
        return componentsList;
    }

    public Transform getTransform() {
        return transform;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
}
/*End of GameObject class*/
