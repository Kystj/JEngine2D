/*
 Title: AnimationEditor
 Date: 2024-05-27
 Author: Kyle St John
 */
/*
 Title: AnimationEditor
 Date: 2024-05-27
 Author: Kyle St John
 */
package engine.editor;

import engine.statemachine.animations.Animation;
import engine.statemachine.states.State;
import engine.utils.engine.ResourceUtils;
import engine.utils.imgui.ImGuiUtils;
import engine.world.components.Sprite;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.joml.Vector2f;

import static engine.utils.engine.EConstants.TEXTURE_COORDINATES;
import static engine.utils.imgui.GConstants.ANIMATION_VIEWPORT_SIZE;
import static engine.utils.imgui.ImGuiUtils.renderRightClickContext;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class AnimationEditor {

    private static final Animation editableAnimation = new Animation();
    private static final ImBoolean IS_OPEN = new ImBoolean(true);
    private static final ImBoolean IS_LOOPING= new ImBoolean(false);
    private static ImString name = new ImString();

    private static final Vector2f VIEWPORT_POS = new Vector2f();
    private static final State editableState = new State("Test",
            () -> System.out.println("Entering Test State"),
            () -> System.out.println("Exiting Test State"),
            () -> System.out.println("Updating Test State")
    );



    public void tick(float deltaTime) {

    }


    public static void imgui() {
        if (IS_OPEN.get()) {
            ImGui.begin("Animation Editor", IS_OPEN);
            ImGui.columns(2);
            ImGui.spacing();
            float COLUMN_WIDTH = 160.0f;
            ImGui.setColumnWidth(0, COLUMN_WIDTH);
            ImGui.text("State Name: " + editableState.getName());
            ImGui.nextColumn();
            ImGui.spacing();
            float ITEM_WIDTH = 160.0f;
            ImGui.setNextItemWidth(ITEM_WIDTH);
            ImGui.inputText("##name", name);
            editableState.setName(name.get());
            ImGui.columns();

            ImGui.columns(2);
            ImGui.spacing();
            ImGui.setColumnWidth(0, COLUMN_WIDTH);
            ImGui.text("Frame Duration (ms)");
            float[] duration = {editableAnimation.getFrameDuration()};
            ImGui.nextColumn();
            ImGui.spacing();
            ImGui.setNextItemWidth(ITEM_WIDTH);
            ImGui.sliderFloat("##duration", duration, 0, 1000);
            editableAnimation.setFrameDuration(duration[0]);
            ImGui.columns();

            ImGui.columns(2);
            ImGui.setColumnWidth(0, COLUMN_WIDTH);
            ImGui.text("Loop?");
            ImGui.nextColumn();
            ImGui.spacing();
            ImGui.setNextItemWidth(ITEM_WIDTH);
            if (ImGui.checkbox("##isLooping", IS_LOOPING)) {
                editableAnimation.shouldLoop(!IS_LOOPING.get());
            }


            if (ImGui.button("Add Frame")) {
               // IS_OPEN_FRAME.set(true);
                Sprite sprite = new Sprite();
                sprite.setTexture(ResourceUtils.getSpriteSheet("run").getSprite(0).getSpriteTexture());
                editableAnimation.addFrame(sprite);
            }

            ImGui.columns();


            // Render frames
            for (Sprite frame : editableAnimation.getFrames()) {
                ImGui.sameLine();
                ImGui.image(frame.getTextureID(), 32, 32,
                        TEXTURE_COORDINATES[0].x, TEXTURE_COORDINATES[2].y, TEXTURE_COORDINATES[2].x, TEXTURE_COORDINATES[0].y);
                renderRightClickContext("Delete");
            }

            ImVec2 viewportSize = new ImVec2(ANIMATION_VIEWPORT_SIZE.x, ANIMATION_VIEWPORT_SIZE.y);
            ImVec2 viewportPos = getCenteredPositionForViewport(viewportSize);

            ImGui.setCursorPos(viewportPos.x, viewportPos.y);

            ImVec2 topLeft = new ImVec2();
            ImGui.getCursorScreenPos(topLeft);
            topLeft.x -= ImGui.getScrollX();
            topLeft.y -= ImGui.getScrollY();


           renderAnimation(20, viewportSize);

            ImGui.spacing();

            VIEWPORT_POS.set(topLeft.x, topLeft.y);

            ImGui.spacing();
            ImGui.setCursorPosX(viewportPos.x + (viewportSize.x / 2.2f));
            ImGuiUtils.renderPlayButton();
            ImGui.sameLine();
            ImGuiUtils.renderStopButton();

            int[] index = {editableAnimation.getCurrentFrameIndex()};
            ImGui.spacing();
            ImGui.setCursorPosX(viewportPos.x);
            ImGui.setNextItemWidth(ANIMATION_VIEWPORT_SIZE.x);
            ImGui.sliderInt("##index", index, 0, editableAnimation.getFrames().size());
            ImGui.sameLine();
            ImGui.text("?");
            if (ImGui.isItemHovered()) {
                ImGui.beginTooltip();
                ImGui.setTooltip("SET THIS TOOL TIP");
                ImGui.endTooltip();
            }
            editableAnimation.setCurrentFrameIndex(index[0]);
            ImGui.columns();

            ImGui.end();
        }
    }

    public static void renderAnimation(int textureId, ImVec2 size) {
        glBindTexture(GL_TEXTURE_2D, textureId);
        ImGui.image(textureId, size.x, size.y, 0, 1, 1, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }


    private static ImVec2 getCenteredPositionForViewport(ImVec2 aspectSize) {
        ImVec2 windowSize = findWindowSize();
        float viewportX = (windowSize.x - aspectSize.x) / 2.0f;
        float viewportY = (windowSize.y - aspectSize.y) / 2.0f;
        return new ImVec2(viewportX, viewportY);
    }

    private static ImVec2 findWindowSize() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        return windowSize;
    }

    public static void setIsOpen(boolean isOpen) {
        IS_OPEN.set(isOpen);
    }
}
/*End of AnimationEditor class*/