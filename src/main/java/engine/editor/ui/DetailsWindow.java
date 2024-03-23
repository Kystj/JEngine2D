/*
 Title: DetailsWindow
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.utils.EConstants;
import engine.utils.ImGuiUtils;
import engine.world.components.Component;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DetailsWindow implements EventListener {

    private GameObject activeGameObject = null;
    private final ImBoolean bIsOpen = new ImBoolean(true);

    public DetailsWindow() {
        EventDispatcher.addListener(EConstants.EventType.User, this);
    }

    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (event.getEventType() == EConstants.EventType.User) {
            activeGameObject = gameObject;
            bIsOpen.set(true);
        }
    }

    @Override
    public void onEvent(Event event) {

    }


    public void imgui() {
        if (activeGameObject != null && bIsOpen.get()) {
            ImGui.begin("Details", bIsOpen);

            ImGui.spacing();
            ImGuiUtils.renderVec2Sliders("Scale", activeGameObject.getTransform().getScale(), activeGameObject.getTransform().getScale());
            ImGuiUtils.renderVec2Sliders("Position", activeGameObject.getTransform().getPosition(), activeGameObject.getTransform().getPosition());
            activeGameObject.getTransform().setRotation(ImGuiUtils.renderFloatSlider("Rotation", activeGameObject.getTransform().getRotation()));
            activeGameObject.setZIndex(ImGuiUtils.renderIntSlider("Z-Index", activeGameObject.getZIndex()));

            for (int i = 0; i < activeGameObject.getComponentsList().size(); i++) {
                try {
                    Component component = activeGameObject.getComponentsList().get(i);
                    Field[] fields = component.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        boolean isTransient = Modifier.isTransient(field.getModifiers());
                        if (isTransient) {
                            continue;
                        }

                        boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                        if (isPrivate) {
                            field.setAccessible(true);
                        }

                        Class<?> type = field.getType();
                        Object value = field.get(component);
                        String name = field.getName();

                        if (type == int.class) {
                            int val = (int) value;
                            int[] imInt = {val};
                            if (ImGui.dragInt(name + ": ", imInt)) {
                                field.set(component, imInt[0]);
                            }


                        } else if (type == float.class) {
                            float val = (float) value;
                            float[] imFloat = {val};
                            if (ImGui.dragFloat(name + ": ", imFloat)) {
                                field.set(component, imFloat[0]);
                            }


                        } else if (type == boolean.class) {
                            boolean val = (boolean) value;
                            if (ImGui.checkbox(name + ": ", val)) {
                                field.set(component, !val);
                            }


                        } else if (type == Vector3f.class) {
                            Vector3f val = (Vector3f) value;
                            float[] imVec = {val.x, val.y, val.z};
                            if (ImGui.dragFloat3(name + ": ", imVec)) {
                                val.set(imVec[0], imVec[1], imVec[2]);
                            }


                        } else if (type == Vector4f.class) {
                            activeGameObject.getComponent(Sprite.class).setColor(ImGuiUtils.renderColorPicker4f("Color",
                                    activeGameObject.getComponent(Sprite.class).getColor()));
                        }

                        if (isPrivate) {
                            field.setAccessible(false);
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            ImGui.end();
        }
    }
}
/*End of DetailsWindow class*/
