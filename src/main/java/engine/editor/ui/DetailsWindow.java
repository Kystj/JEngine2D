/*
 Title: DetailsWindow
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debug.info.DebugLogger;
import engine.editor.GameEditor;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.physics.components.BoxCollider;
import engine.physics.components.Collider;
import engine.physics.components.RigidBody;
import engine.utils.EConstants;
import engine.utils.ImGuiUtils;
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

public class DetailsWindow implements EventListener {

    private final ImBoolean bIsOpen = new ImBoolean(true);
    private GameObject activeGameObject;

    public DetailsWindow() {
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
    }

    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (event.getEventType() == EConstants.EventType.Active_Object) {
            bIsOpen.set(true);
            activeGameObject = gameObject;
        }
    }

    @Override
    public void onEvent(Event event, Level level) {

    }

    @Override
    public void onEvent(Event event) {


    }


    public void imgui() {
        if (activeGameObject != null && bIsOpen.get()) {
            int zIndex = activeGameObject.getZIndex();

            ImGui.begin("Details", bIsOpen);
            ImGui.spacing();
            renderTransformControls();
            renderSpriteProperties();
            updateZIndex(zIndex);
            renderRigidBodyControls();
            renderComponentProperties();
            ImGui.end();
        }
    }

    private void renderTransformControls() {
        ImGuiUtils.renderVec2Sliders("Scale", activeGameObject.getTransform().getScale(), activeGameObject.getTransform().getScale());
        ImGuiUtils.renderVec2Sliders("Position", activeGameObject.getTransform().getPosition(), activeGameObject.getTransform().getPosition());
        activeGameObject.getTransform().setRotation(ImGuiUtils.renderFloatSlider("Rotation", activeGameObject.getTransform().getRotation()));
        activeGameObject.setZIndex(ImGuiUtils.renderIntSlider("Z-Index", activeGameObject.getZIndex()));
    }

    private void renderSpriteProperties() {
        activeGameObject.getComponent(Sprite.class).setColor(ImGuiUtils.renderColorPicker4f("Color", activeGameObject.getComponent(Sprite.class).getColor()));
    }

    private void updateZIndex( int oldZIndex) {
        if (activeGameObject.getZIndex() != oldZIndex) {
            GameEditor.current_Level.removeGameObject(activeGameObject.getUID());
            GameEditor.current_Level.addGameObject(activeGameObject);
        }
    }

    private void renderRigidBodyControls() {
        ImGui.columns(2);
        ImGui.setColumnWidth(0, 160); // TODO: Should be dynamic
        ImGui.text("RigidBody");
        ImGui.nextColumn();

        RigidBody rigidBodyComponent = activeGameObject.getComponent(RigidBody.class);
        Collider colliderComponent = activeGameObject.getComponent(Collider.class);

        boolean hasRigidBody = rigidBodyComponent == null;

        String[] colliders = {"Box Collider", "Circle Collider"};
        int index = 0;


        boolean attachRigidBody = ImGui.checkbox("##RigidBody", !hasRigidBody);

        ImGui.columns(1);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, 160); // TODO: Should be dynamic
        ImGui.text("Colliders");
        ImGui.nextColumn();

        if (ImGui.beginCombo(" ", colliders[index])) {
            for (int i = 0; i < colliders.length; i++) {
                boolean isSelected = (index == i);
                if (ImGui.selectable(colliders[i], isSelected)) {
                    index = i;
                }
                if (isSelected) {
                    ImGui.setItemDefaultFocus();
                }
            }
            ImGui.endCombo();
        }
        DebugLogger.info(String.valueOf(index));
        if (attachRigidBody) {
            activeGameObject.addComponent(new RigidBody());
            activeGameObject.addComponent(new BoxCollider());
            GameEditor.current_Level.getPhysics().add(activeGameObject);


        }
        ImGui.columns(1);
    }

    private void renderComponentProperties() {
        for (Component component : activeGameObject.getComponentsList()) {
            Field[] fields = component.getClass().getDeclaredFields();
            for (Field field : fields) {
                renderComponentField(component, field);
            }
        }
    }

    private void renderComponentField(Component component, Field field) {
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
                if (ImGui.dragInt(name + ": ", imInt)) {
                    field.set(component, imInt[0]);
                }
            } else if (type == float.class) {
                ImGui.columns(2);
                ImGui.text(name);
                ImGui.nextColumn();
                float val = (float) value;
                float[] imFloat = {val};
                if (ImGui.dragFloat("##name", imFloat)) {
                    field.set(component, imFloat[0]);
                }
                ImGui.columns(1);
            } else if (type == boolean.class) {
                ImGui.columns(2);
                ImGui.text(name);
                ImGui.nextColumn();
                boolean val = (boolean) value;
                if (ImGui.checkbox("##name", val)) {
                    field.set(component, !val);
                }
                ImGui.columns(1);
            } else if (type == Vector2f.class) {
                ImGui.columns(2);
                ImGui.text(name);
                ImGui.nextColumn();
                Vector2f val = (Vector2f) value;
                float[] imVec = {val.x, val.y};
                if (ImGui.dragFloat2("##name", imVec)) {
                    val.set(imVec[0], imVec[1]);
                }
                ImGui.columns(1);
            } else if (type == Vector3f.class) {
                Vector3f val = (Vector3f) value;
                float[] imVec = {val.x, val.y, val.z};
                if (ImGui.dragFloat3("##name", imVec)) {
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
}
/*End of DetailsWindow class*/
