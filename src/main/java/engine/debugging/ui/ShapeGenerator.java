/*
 Title: ErrorForm
 Date: 2024-03-07
 Author: Kyle St John
 */
package engine.debugging.ui;

import engine.debugging.draw.DebugDraw;
import engine.utils.ImGuiUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * Generates shapes for debugging and rendering purposes.
 */
public class ShapeGenerator {

    // Variables

    protected enum ShapeType {BOX, TRIANGLE, CIRCLE, LINE};
    protected ShapeType currentShape;
    protected Vector3f color;
    protected int lifeTime;
    protected float rotation;
    protected boolean bIsPersistent = false;

    // Constructor

    /**
     * Constructs a ShapeGenerator with default values.
     */
    public ShapeGenerator() {
        color = new Vector3f();
        lifeTime = 0;
        rotation = 0;
    }

    // Protected methods

    /**
     * Renders the user interface for shape generation.
     */
    protected void renderUI() {

    }

    /**
     * Draws common UI elements for shape generation.
     */
    protected void drawCommonUI() {
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);
        ImGui.text("?");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("To draw a " + currentShape + " enter the fields.");
            ImGui.endTooltip();
        }

        color = ImGuiUtils.renderColorPicker3f("Color", color);
        lifeTime = ImGuiUtils.renderIntSlider("Life", lifeTime);
        ImGui.sameLine();
        if (ImGui.checkbox("##Persist", bIsPersistent)) {
            bIsPersistent = !bIsPersistent;
        }
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Persist?");
            ImGui.endTooltip();
        }
        rotation = ImGuiUtils.renderFloatSlider("Rotation", rotation);
    }

    /**
     * Draws a button and the associated shape.
     *
     * @param drawShape The action to draw the shape.
     */
    protected void drawButtonAndDrawShape(Runnable drawShape) {
        ImGui.spacing();
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 1.0f, 0.0f, 0.5f); // RGB color
        if (ImGui.button("Draw Shape")) {
            drawShape.run();
            refreshFields();
        }
        ImGui.popStyleColor();
    }

    /**
     * Refreshes the fields of the shape generator.
     */
    protected void refreshFields() {
        color = new Vector3f();
        lifeTime = 0;
        rotation = 0;
        bIsPersistent = false;
    }
}/* End of ShapeGenerator class */

/**
 * Generates boxes for debugging and rendering purposes.
 */
class BoxGenerator extends ShapeGenerator {

    // Variables

    private Vector2f size;
    private Vector2f position;

    // Constructor

    /**
     * Constructs a BoxGenerator with default values.
     */
    public BoxGenerator() {
        size = new Vector2f();
        position = new Vector2f();
        currentShape = ShapeType.BOX;
    }

    // Public methods

    @Override
    public void renderUI() {
        drawCommonUI();
        ImGuiUtils.renderVec2Sliders("Position", position, position);
        ImGuiUtils.renderVec2Sliders("Size", size, size);

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addBox(position, size, rotation, color, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addBox(position, size, rotation, color, lifeTime, false));
        }
    }

    // Protected methods

    @Override
    protected void refreshFields() {
        super.refreshFields();
        size = new Vector2f();
        rotation = 0;
        position = new Vector2f();
    }
}/* End of BoxGenerator class */

/**
 * Generates triangles for debugging and rendering purposes.
 */
class TriangleGenerator extends ShapeGenerator {

    // Variables

    private Vector2f v1;
    private Vector2f v2;
    private Vector2f v3;

    // Constructor

    /**
     * Constructs a TriangleGenerator with default values.
     */
    public TriangleGenerator() {
        v1 = new Vector2f();
        v2 = new Vector2f();
        v3 = new Vector2f();
        currentShape = ShapeType.TRIANGLE;
    }

    // Public methods

    @Override
    public void renderUI() {
        drawCommonUI();
        ImGuiUtils.renderVec2Sliders("V1", v1, v1);
        ImGuiUtils.renderVec2Sliders("V2", v2, v2);
        ImGuiUtils.renderVec2Sliders("V3", v3, v3);

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addTriangle(v1, v2, v3, color,
                    true, rotation));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addTriangle(v1, v2, v3, color, lifeTime,
                    false, rotation));
        }
    }

    // Protected methods

    @Override
    protected void refreshFields() {
        super.refreshFields();
        v1 = new Vector2f();
        v2 = new Vector2f();
        v3 = new Vector2f();
    }
}/* End of TriangleGenerator class */

/**
 * Generates circles for debugging and rendering purposes.
 */
class CircleGenerator extends ShapeGenerator {

    // Variables

    private Vector2f centre;
    private float radius;
    private int segments;

    // Constructor

    /**
     * Constructs a CircleGenerator with default values.
     */
    public CircleGenerator() {
        centre = new Vector2f();
        radius = 0;
        segments = 36;
        currentShape = ShapeType.CIRCLE;
    }

    // Public methods

    @Override
    public void renderUI() {
        drawCommonUI();
        ImGuiUtils.renderVec2Sliders("Centre", centre, centre);
        radius = ImGuiUtils.renderFloatSlider("Radius", radius);
        segments = ImGuiUtils.renderIntSlider("Segments", segments);

        ImGui.sameLine();
        ImGui.text("?");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Segments to 36");
            ImGui.endTooltip();
        }

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addCircle(centre, radius, rotation, color,
                    segments, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addCircle(centre, radius, rotation, color,
                    lifeTime, segments, false));
        }
    }

    // Protected methods

    @Override
    protected void refreshFields() {
        super.refreshFields();
        centre = new Vector2f();
        radius = 0;
        segments = 36;
    }
}/* End of CircleGenerator class */

/**
 * Generates lines for debugging and rendering purposes.
 */
class LineGenerator extends ShapeGenerator {

    // Variables

    private Vector2f from;
    private Vector2f to;

    // Constructor

    /**
     * Constructs a LineGenerator with default values.
     */
    public LineGenerator() {
        from = new Vector2f();
        to = new Vector2f();
        currentShape = ShapeType.LINE;
    }

    // Public methods

    @Override
    public void renderUI() {
        drawCommonUI();
        ImGuiUtils.renderVec2Sliders("From", from, from);
        ImGuiUtils.renderVec2Sliders("To", to, to);

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addDebugLine(from, to, color, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addDebugLine(from, to, color, lifeTime));
        }
    }

    // Protected methods

    @Override
    protected void refreshFields() {
        super.refreshFields();
        from = new Vector2f();
        to = new Vector2f();
    }
}/* End of LineGenerator class */