package engine.editor.gizmo;

import engine.graphics.EngineWindow;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.io.MouseInputs;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static engine.utils.engine.EConstants.*;

public class Gizmo {

    Texture gizmoSpriteSheetTexture;
    protected SpriteSheet gizmoSpriteSheet;
    protected Sprite masterSprite;
    protected Sprite xPosGizmoSprite;
    protected Sprite yPosGizmoSprite;

    protected GameObject posGizmoX;
    protected GameObject posGizmoY;
    protected GameObject activeGameObject;

    protected boolean xActive = false;
    protected boolean yActive = false;
    protected boolean xHasChanged = false;
    protected boolean yHasChanged = false;

    protected Vector2f xGizmoPosition;
    protected Vector2f yGizmoPosition;

    public Gizmo(GameObject gameObject) {
        this.activeGameObject = gameObject;
        this.gizmoSpriteSheetTexture = new Texture("assets/spritesheets/gizmos.png");
        this.gizmoSpriteSheet = new SpriteSheet(gizmoSpriteSheetTexture, 32,32,0, "Gizmos");
        calculateGizmoPositions();
    }

    protected void tick() {
        calculateGizmoPositions();
        posGizmoX.getTransform().setPosition(xGizmoPosition);
        posGizmoY.getTransform().setPosition(yGizmoPosition);

        Vector2f mousePosition = new Vector2f(MouseInputs.getOrthoX(), MouseInputs.getOrthoY());

        if (isMouseHoveringX(mousePosition) && !yActive) {
            handleHover(xPosGizmoSprite, GIZMO_X_HOVER_COLOR);
            if (MouseInputs.isDragging()) {
                xHasChanged = true;
                yHasChanged = false;
            }
        } else if (isMouseHoveringY(mousePosition) && !xActive) {
            handleHover(yPosGizmoSprite, GIZMO_Y_HOVER_COLOR);
            if (MouseInputs.isDragging()) {
                xHasChanged = false;
                yHasChanged = true;
            }
        } else if (!MouseInputs.isDragging()) {
            resetGizmoStates();
        }
    }

    private void handleHover(Sprite sprite, Vector4f hoverColor) {
        sprite.setColor(hoverColor);
        xActive = sprite == xPosGizmoSprite;
        yActive = sprite == yPosGizmoSprite;
    }

    private void resetGizmoStates() {
        xActive = false;
        yActive = false;
        xHasChanged = false;
        yHasChanged = false;
        xPosGizmoSprite.setColor(GIZMO_X_COLOR);
        yPosGizmoSprite.setColor(GIZMO_Y_COLOR);
    }

    public void addToScene() {
        EngineWindow.Game_Editor.addToScene(posGizmoX);
        EngineWindow.Game_Editor.addToScene(posGizmoY);
    }

    public boolean isMouseHoveringX(Vector2f mousePosition) {
        return isMouseHoveringGizmo(posGizmoX, mousePosition);
    }

    public boolean isMouseHoveringY(Vector2f mousePosition) {
        return isMouseHoveringGizmo(posGizmoY, mousePosition);
    }

    private boolean isMouseHoveringGizmo(GameObject gizmo, Vector2f mousePosition) {
        Transform transform = gizmo.getTransform();
        Vector2f gizmoPosition = transform.getPosition();
        Vector2f gizmoSize = transform.getScale();

        float rotation = transform.getRotation();
        if (rotation != 0) {
            float cos = (float) Math.cos(Math.toRadians(rotation));
            float sin = (float) Math.sin(Math.toRadians(rotation));
            float dx = mousePosition.x - gizmoPosition.x;
            float dy = mousePosition.y - gizmoPosition.y;
            float rotatedX = dx * cos + dy * sin;
            float rotatedY = -dx * sin + dy * cos;

            return rotatedX >= -gizmoSize.x / 2f && rotatedX <= gizmoSize.x / 2f &&
                    rotatedY >= -gizmoSize.y / 2f && rotatedY <= gizmoSize.y / 2f;
        } else {
            return mousePosition.x >= gizmoPosition.x - gizmoSize.x / 2f && mousePosition.x <= gizmoPosition.x + gizmoSize.x / 2f &&
                    mousePosition.y >= gizmoPosition.y - gizmoSize.y / 2f && mousePosition.y <= gizmoPosition.y + gizmoSize.y / 2f;
        }
    }

    private void calculateGizmoPositions() {
        if (activeGameObject != null) {
            Transform transform = activeGameObject.getTransform();
            Vector2f gameObjectPosition = transform.getPosition();
            Vector2f gameObjectSize = transform.getScale();

            xGizmoPosition = new Vector2f(gameObjectPosition.x - gameObjectSize.x / 2.0f + GIZMO_OFFSET,
                    gameObjectPosition.y - gameObjectSize.y / 2.0f);

            yGizmoPosition = new Vector2f(gameObjectPosition.x - gameObjectSize.x / 2.0f,
                    gameObjectPosition.y - gameObjectSize.y / 2.0f + GIZMO_OFFSET);
        }
    }
}
