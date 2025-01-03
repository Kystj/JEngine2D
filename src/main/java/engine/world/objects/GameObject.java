package engine.world.objects;

import engine.world.components.Component;
import engine.world.components.Transform;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name = "GameObject_";
    private final List<Component> componentsList = new ArrayList<>();
    private transient Transform transform;
    private static int GLOBAL_OBJECT_ID_COUNTER = -1;
    protected int objectUID;


    public void init() {
        if (objectUID != -1) {
            generateUniqueId();
            name = name + objectUID;
        }
        for (Component component : componentsList) {
            component.init();
        }
    }

    public void update(float dt) {
        for (Component component : componentsList) {
            component.tick(dt);
        }
    }


    private synchronized void generateUniqueId() {
        this.objectUID = ++GLOBAL_OBJECT_ID_COUNTER;
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


    public void setName(String objName) {
        this.name = objName;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public int getUID() {
        return objectUID;
    }

    public String getName() {
        return name;
    }

    public Transform getTransform() {
        return transform;
    }


    public List<Component> getComponentsList() {
        return componentsList;
    }

    public void setZIndex(int zIndex) {
        this.transform.setZIndex(zIndex);
    }

    public int getZIndex() {
        return this.transform.getzIndex();
    }

    public int setUID(int newUID) {
        return this.objectUID = newUID;
    }
}