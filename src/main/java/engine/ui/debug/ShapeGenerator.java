/*
 Title: DrawShapes
 Date: 2024-02-14
 Author: Kyle St John
 */

package engine.ui.debug;

import engine.debug.draw.DebugDraw;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiColorEditFlags;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class ShapeGenerator {

    protected enum ShapeType {BOX, TRIANGLE, CIRCLE, LINE};

    protected ShapeType currentShape;

    protected Vector3f color;
    protected int lifeTime;
    protected float rotation;

    protected boolean bIsPersistent = false;

    public ShapeGenerator() {
        color = new Vector3f();
        lifeTime = 0;
        rotation = 0;
    }

    protected void drawUI() {

    }

    protected void drawCommonUI() {
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);
        ImGui.text("?");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("To draw a " + currentShape + " enter the fields.");
            ImGui.endTooltip();
        }

        ImGui.columns(2);
        ImGui.spacing();
        ImGui.setColumnWidth(0, ImGui.getWindowSizeX() / 3.0f);

        ImGui.setCursorPosY(ImGui.getCursorPosY());
        ImGui.text("Color");
        ImGui.text("Lifetime");
        ImGui.text("Rotation");
        ImGui.spacing();
        ImGui.setCursorPosY(ImGui.getCursorPosY()+ 10);
        ImGui.text("Persistent");

        ImGui.nextColumn();

        float[] imVecColor = {color.x, color.y, color.z};
        if (ImGui.colorEdit3("##Color", imVecColor, ImGuiColorEditFlags.DisplayRGB )) {
            color.set(imVecColor[0], imVecColor[1], imVecColor[2]);
        }

        int[] imFloatLifetime = {lifeTime};
        if (ImGui.dragInt("##Lifetime", imFloatLifetime)) {
            lifeTime = imFloatLifetime[0];
        }

        float[] imFloatRotation = {rotation};
        if (ImGui.dragFloat("##Rotation", imFloatRotation)) {
            rotation = imFloatRotation[0];
        }

        ImGui.spacing();
        if (ImGui.checkbox("##Persistent", bIsPersistent)) {
            bIsPersistent = !bIsPersistent;
        }
        ImGui.nextColumn();
    }

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

    protected void refreshFields() {
        color = new Vector3f();
        lifeTime = 0;
        rotation = 0;
        bIsPersistent = false;
    }
}

class BoxGenerator extends ShapeGenerator {

    private Vector2f size;
    private Vector2f position;

    public BoxGenerator() {
        size = new Vector2f();
        position = new Vector2f();
        currentShape = ShapeType.BOX;
    }

    @Override
    public void drawUI() {
        drawCommonUI();

        ImGui.text("Position");
        ImGui.text("Size");

        ImGui.nextColumn();

        float[] imVecPos = {position.x, position.y};
        if (ImGui.dragFloat2("##Position", imVecPos)) {
            position.set(imVecPos[0], imVecPos[1]);
        }

        float[] imVecSize = {size.x, size.y};
        if (ImGui.dragFloat2("##Size", imVecSize)) {
            size.set(imVecSize[0], imVecSize[1]);
        }

        ImGui.columns(1);
        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addBox(position, size, rotation, color, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addBox(position, size, rotation, color, lifeTime, false));
        }
    }

    @Override
    protected void refreshFields() {
        super.refreshFields();
        size = new Vector2f();
        rotation = 0;
        position = new Vector2f();
    }
}

class TriangleGenerator extends ShapeGenerator {

    private Vector2f v1;
    private Vector2f v2;
    private Vector2f v3;

    public TriangleGenerator() {
        v1 = new Vector2f();
        v2 = new Vector2f();
        v3 = new Vector2f();
        currentShape = ShapeType.TRIANGLE;
    }

    @Override
    public void drawUI() {
        drawCommonUI();
        ImGui.text("V1");
        ImGui.text("V2");
        ImGui.text("V3");

        ImGui.nextColumn();
        float[] imVecV1 = {v1.x, v1.y};
        if (ImGui.dragFloat2("##V1", imVecV1)) {
            v1.set(imVecV1[0], imVecV1[1]);
        }

        float[] imVecV2 = {v2.x, v2.y};
        if (ImGui.dragFloat2("##V2", imVecV2)) {
            v2.set(imVecV2[0], imVecV2[1]);
        }

        float[] imVecV3 = {v3.x, v3.y};
        if (ImGui.dragFloat2("##V3", imVecV3)) {
            v3.set(imVecV3[0], imVecV3[1]);
        }
        ImGui.columns(1);
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addTriangle(v1, v2, v3, color,
                    true, rotation));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addTriangle(v1, v2, v3, color, lifeTime,
                    false, rotation));
        }
    }

    @Override
    protected void refreshFields() {
        super.refreshFields();
        v1 = new Vector2f();
        v2 = new Vector2f();
        v3 = new Vector2f();
    }
}

class CircleGenerator extends ShapeGenerator {

    private Vector2f centre;
    private float radius;
    private int segments;

    public CircleGenerator() {
        centre = new Vector2f();
        radius = 0;
        segments = 36;
        currentShape = ShapeType.CIRCLE;
    }

    @Override
    public void drawUI() {
        drawCommonUI();
        ImGui.text("Centre");
        ImGui.text("Radius");
        ImGui.text("Segments");

        ImGui.nextColumn();
        float[] imVecFrom = {centre.x, centre.y};
        if (ImGui.dragFloat2("##Centre", imVecFrom)) {
            centre.set(imVecFrom[0], imVecFrom[1]);
        }

        float[] imVecTo = {radius};
        if (ImGui.dragFloat("##Radius", imVecTo)) {
            radius = imVecTo[0];
        }


        int[] imIntSegments = {segments};
        if (ImGui.dragInt("##Segments", imIntSegments)) {
            segments = imIntSegments[0];
        }
        ImGui.sameLine();
        ImGui.text("?");
        if (ImGui.isItemHovered()) {
            ImGui.beginTooltip();
            ImGui.setTooltip("Segments to 36");
            ImGui.endTooltip();
        }

        ImGui.columns(1);
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);
        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addCircle(centre, radius, rotation, color,
                    segments, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addCircle(centre, radius, rotation, color,
                    lifeTime, segments, false));
        }
    }

    @Override
    protected void refreshFields() {
        super.refreshFields();
        centre = new Vector2f();
        radius = 0;
        segments = 36;
    }
}

class LineGenerator extends ShapeGenerator {

    private Vector2f from;
    private Vector2f to;

    public LineGenerator() {
        from = new Vector2f();
        to = new Vector2f();
        currentShape = ShapeType.LINE;
    }

    @Override
    public void drawUI() {
        drawCommonUI();
        ImGui.text("From");
        ImGui.text("To");

        ImGui.nextColumn();

        float[] imVecFrom = {from.x, from.y};
        if (ImGui.dragFloat2("##From", imVecFrom)) {
            from.set(imVecFrom[0], imVecFrom[1]);
        }
        float[] imVecTo = {to.x, to.y};
        if (ImGui.dragFloat2("##To", imVecTo)) {
            to.set(imVecTo[0], imVecTo[1]);
        }
        ImGui.columns(1);
        ImGui.setCursorPosX(ImGui.getWindowSizeX() / 2);

        if (bIsPersistent) {
            drawButtonAndDrawShape(() -> DebugDraw.addLine(from, to, color, true));
        } else {
            drawButtonAndDrawShape(() -> DebugDraw.addLine(from, to, color, lifeTime));
        }
    }

    @Override
    protected void refreshFields() {
        super.refreshFields();
        from = new Vector2f();
        to = new Vector2f();
    }
}
/*End of ShapeGenerator class*/