/*
 Title: DetailsWindow
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debugging.info.Logger;
import engine.editor.GameEditor;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.physics.components.BoxCollider;
import engine.physics.components.CircleCollider;
import engine.physics.components.Collider;
import engine.physics.components.RigidBody;
import engine.utils.engine.EConstants;
import engine.utils.imgui.ImGuiUtils;
import engine.world.components.Component;
import engine.world.components.Sprite;
import engine.world.levels.Level;
import engine.world.objects.GameObject;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static engine.utils.engine.EConstants.COLUMN_WIDTH_LARGE;
import static engine.utils.imgui.ImGuiUtils.setSectionName;
import static org.jbox2d.dynamics.BodyType.*;

public class DetailsWindow implements EventListener {

    private final ImBoolean bIsOpen = new ImBoolean(true);

    private GameObject activeGameObject;

    private int colliderValueIndex = 0;
    private int b2BodyTypeIndex = 0;

    private boolean hasRigidBody;
    private boolean hasCollider;
    private boolean attachRigidBody;

    private Vector2f initialScale = new Vector2f();
    private Vector2f initialPosition = new Vector2f();


    private final String[] colliders = {"None", "Box Collider", "Circle Collider"};
    private final String[] b2BodyTypes = {"Dynamic Body", "Static Body", "Kinematic Body"};


    public DetailsWindow() {
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
        EventDispatcher.addListener(EConstants.EventType.Stop, this);
        EventDispatcher.addListener(EConstants.EventType.Play, this);
        EventDispatcher.addListener(EConstants.EventType.Save, this);
    }

    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (event.getEventType() == EConstants.EventType.Active_Object) {
            bIsOpen.set(true);
            activeGameObject = gameObject;

            if (activeGameObject != null) {
                initialScale.set(activeGameObject.getTransform().getScale());
                initialPosition.set(activeGameObject.getTransform().getPosition());

                if (activeGameObject.getComponent(RigidBody.class) != null) {
                    if (activeGameObject.getComponent(RigidBody.class).getBodyType() == KINEMATIC) {
                        b2BodyTypeIndex = 2;
                    } else if (activeGameObject.getComponent(RigidBody.class).getBodyType() == STATIC) {
                        b2BodyTypeIndex = 1;
                    } else {
                        b2BodyTypeIndex = 0;
                    }
                } else {
                    b2BodyTypeIndex = 0;
                }


                if (activeGameObject.getComponent(BoxCollider.class) != null) {
                    colliderValueIndex = 1;
                } else if (activeGameObject.getComponent(CircleCollider.class) != null) {
                    colliderValueIndex = 2;
                } else {
                    colliderValueIndex = 0;
                }
            }
        }
    }

        @Override
        public void onEvent (Event event, Level level){

        }

        @Override
        public void onEvent (Event event){
            if (event.getEventType() == EConstants.EventType.Play) {
                bIsOpen.set(false);
            }
            if (event.getEventType() == EConstants.EventType.Stop) {
                bIsOpen.set(true);
            }
            if (event.getEventType() == EConstants.EventType.Save) {
                activeGameObject = null;
            }
        }


        public void imgui () {
            if (activeGameObject != null && bIsOpen.get()) {
                ImGui.begin("Details", bIsOpen);
                setTransformControlUI();
                setRigidBodyUI();
                setComponents();
                ImGui.end();
            }
        }

        private void setTransformControlUI () {
            int zIndex = activeGameObject.getZIndex();
            setSectionName("Transform");
            ImGuiUtils.renderVec2Sliders("Scale", activeGameObject.getTransform().getScale(), initialScale);
            ImGuiUtils.renderVec2Sliders("Position", activeGameObject.getTransform().getPosition(), initialPosition);


            activeGameObject.getTransform().setRotation(ImGuiUtils.renderFloatSlider("Rotation", activeGameObject.getTransform().getRotation()));
            activeGameObject.setZIndex(ImGuiUtils.renderIntSlider("Z-Index", activeGameObject.getZIndex()));
            activeGameObject.getComponent(Sprite.class).setColor(ImGuiUtils.renderColorPicker4f("Color", activeGameObject.getComponent(Sprite.class).getColor()));
            updateZIndex(zIndex);
        }

        private void updateZIndex ( int oldZIndex){
            if (activeGameObject.getZIndex() != oldZIndex) {
                GameEditor.CURRENT_LEVEL.removeGameObject(activeGameObject.getUID());
                GameEditor.CURRENT_LEVEL.addGameObject(activeGameObject);
            }
        }


        private void setRigidBodyUI () {
            setSectionName("Box2D");
            // Set up ImGui columns
            ImGui.columns(2);
            ImGui.setColumnWidth(0, COLUMN_WIDTH_LARGE);

            // Display RigidBody label
            ImGui.text("RigidBody");
            ImGui.nextColumn();

            // Get RigidBody and Collider components
            RigidBody rigidBodyComponent = activeGameObject.getComponent(RigidBody.class);
            Collider colliderComponent = activeGameObject.getComponent(Collider.class);

            // Check if the GameObject has RigidBody and Collider components
            hasRigidBody = rigidBodyComponent != null;
            hasCollider = colliderComponent != null;

            // Checkbox to attach RigidBody
            if (colliders[colliderValueIndex].equals("None")) {
                ImGui.beginDisabled();
            }

            attachRigidBody = ImGui.checkbox("##RigidBody", hasRigidBody);

            ImGui.columns();


            // If RigidBody should be attached, add it
            if (attachRigidBody && !hasRigidBody) {
                activeGameObject.addComponent(new RigidBody());
            }

            if (activeGameObject.getComponent(RigidBody.class) != null) {
                switch (b2BodyTypes[b2BodyTypeIndex]) {
                    case "Dynamic Body":
                        activeGameObject.getComponent(RigidBody.class).setBodyType(DYNAMIC);
                        break;
                    case "Static Body":
                        activeGameObject.getComponent(RigidBody.class).setBodyType(STATIC);
                        break;
                    case "Kinematic Body":
                        activeGameObject.getComponent(RigidBody.class).setBodyType(KINEMATIC);
                        break;
                }
            }


            if (attachRigidBody && hasRigidBody) {
                activeGameObject.removeComponent(RigidBody.class);

                if (activeGameObject.getComponent(BoxCollider.class) != null) {
                    activeGameObject.removeComponent(BoxCollider.class);
                } else if (activeGameObject.getComponent(CircleCollider.class) != null) {
                    activeGameObject.removeComponent(CircleCollider.class);
                }
            }
            setBodyTypeUI();
            setColliderUI();
        }


        private void setColliderUI () {
            // Display Colliders label
            ImGui.columns(2);
            ImGui.setColumnWidth(0, COLUMN_WIDTH_LARGE);
            ImGui.text("Colliders");
            ImGui.nextColumn();


            // Render combo box for selecting collider type
            if (ImGui.beginCombo("##Colliders ", colliders[colliderValueIndex])) {
                for (int i = 0; i < colliders.length; i++) {
                    boolean isSelected = (colliderValueIndex == i);
                    if (ImGui.selectable(colliders[i], isSelected)) {
                        colliderValueIndex = i;
                        if (colliderValueIndex == 1) {
                            activeGameObject.removeComponent(CircleCollider.class);
                            activeGameObject.addComponent(new BoxCollider());

                        } else if (colliderValueIndex == 2) {
                            activeGameObject.removeComponent(BoxCollider.class);
                            activeGameObject.addComponent(new CircleCollider());
                        } else {
                            activeGameObject.removeComponent(BoxCollider.class);
                            activeGameObject.removeComponent(CircleCollider.class);
                        }
                    }
                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }


            // If RigidBody should be attached and there's no Collider, add selected collider
            if (attachRigidBody && !hasCollider) {
                switch (colliders[colliderValueIndex]) {
                    case "Box Collider":
                        activeGameObject.addComponent(new BoxCollider());
                        Logger.info("Added a box collider to game object with UID:  " + activeGameObject.getUID());
                        break;
                    case "Circle Collider":
                        activeGameObject.addComponent(new CircleCollider());
                        Logger.info("Added a circle collider to game object with UID: " + activeGameObject.getUID());
                        break;
                    default:
                        Logger.info("No collider added");
                }
                GameEditor.CURRENT_LEVEL.getPhysics().add(activeGameObject);
            }

            // End ImGui columns
            ImGui.columns(1);
        }



        private void setBodyTypeUI () {
            // Set up ImGui columns
            ImGui.columns(2);
            ImGui.setColumnWidth(0, COLUMN_WIDTH_LARGE);

            // Display RigidBody label
            ImGui.text("Body Type");
            ImGui.nextColumn();

            if (ImGui.beginCombo("##BodyTypes", b2BodyTypes[b2BodyTypeIndex])) {
                for (int i = 0; i < b2BodyTypes.length; i++) {
                    boolean isSelected = (b2BodyTypeIndex == i);
                    if (ImGui.selectable(b2BodyTypes[i], isSelected)) {
                        b2BodyTypeIndex = i;
                    }
                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }


            if (colliders[colliderValueIndex].equals("None")) {
                ImGui.endDisabled();
            }

            ImGui.sameLine();
            ImGui.text("?");
            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.setTooltip("Dynamic by default");
                ImGui.endTooltip();
            }

            // End ImGui columns
            ImGui.columns(1);
        }

        private void setActiveObjComponentUI (Component component, Field field){
            try {
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient) {
                    return;
                }

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate) {
                    field.setAccessible(true);
                }

                Class<?> type = field.getType();
                Object value = field.get(component);
                String name = ImGuiUtils.camelCaseToWords(field.getName());

                if (type == int.class) {
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt("##" + name, imInt)) {
                        field.set(component, imInt[0]);
                    }
                } else if (type == float.class) {
                    ImGui.columns(2);
                    ImGui.text(name);
                    ImGui.nextColumn();
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat("##" + name, imFloat)) {
                        field.set(component, imFloat[0]);
                    }
                    ImGui.columns(1);
                } else if (type == boolean.class) {
                    ImGui.columns(2);
                    ImGui.text(name);
                    ImGui.nextColumn();
                    boolean val = (boolean) value;
                    if (ImGui.checkbox("##" + name, val)) {
                        field.set(component, !val);
                    }
                    ImGui.columns(1);
                } else if (type == Vector2f.class) {
                    ImGui.columns(2);
                    ImGui.text(name);
                    ImGui.nextColumn();
                    Vector2f val = (Vector2f) value;
                    float[] imVec = {val.x, val.y};
                    if (ImGui.dragFloat2("##" + name, imVec)) {
                        val.set(imVec[0], imVec[1]);
                    }
                    ImGui.columns(1);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3("##" + name, imVec)) {
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                }

                if (isPrivate) {
                    field.setAccessible(false);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        private void setComponents () {
            for (Component component : activeGameObject.getComponentsList()) {
                Field[] fields = component.getClass().getDeclaredFields();
                for (Field field : fields) {
                    setActiveObjComponentUI(component, field);
                }
            }
        }
    }
/*End of DetailsWindow class*/
