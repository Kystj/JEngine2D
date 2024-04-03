/*
 Title: Gizmo
 Date: 2024-03-31
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.graphics.EngineWindow;
import engine.graphics.SpriteSheet;
import engine.io.MouseInputs;
import engine.utils.ResourceUtils;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Gizmo {

    protected SpriteSheet gizmoSpriteSheet = ResourceUtils.getSpriteSheet("assets/spritesheets/gizmos.png");
    protected Sprite masterSprite;
    protected Sprite xPosGizmoSprite;
    protected Sprite yPosGizmoSprite;

    protected GameObject posGizmoX;
    protected GameObject posGizmoY;
    protected  GameObject activeGameObject;

    protected boolean xActive = false;
    protected boolean yActive = false;
    protected boolean xHasChanged = false;
    protected boolean yHasChanged = false;

    protected Vector4f gizmoColorX = new Vector4f(0,0.7f,0,0.5f); // TODO: Make constants
    protected Vector4f gizmoColorY = new Vector4f(0,0,0.7f,0.5f); // TODO: Make constants

    protected Vector2f posGizmoXPosition;
    protected Vector2f posGizmoYPosition;

    public Gizmo(GameObject gameObject) {
        // Assign the active game object
        this.activeGameObject = gameObject;

        // Get GameObject position and size
        Transform transform = gameObject.getTransform();
        Vector2f gameObjectPosition = transform.getPosition();
        Vector2f gameObjectSize = transform.getScale();

        // Calculate position for the gizmos
        posGizmoXPosition = new Vector2f(gameObjectPosition.x - gameObjectSize.x / 2f, gameObjectPosition.y);
        posGizmoYPosition = new Vector2f(gameObjectPosition.x, gameObjectPosition.y - gameObjectSize.y / 2f);

    }

    protected void tick() {
        if (MouseInputs.isDragging() && xActive) {
            xHasChanged = true;
        }

        else if (MouseInputs.isDragging() && yActive) {
            yHasChanged = true;
        }

        if (isMouseHoveringX(new Vector2f(MouseInputs.getOrthoX(), MouseInputs.getOrthoY())) && !yActive) {
            xPosGizmoSprite.setColor(new Vector4f(0, 1, 0, 1));
            xActive = true;
        }

        else if (isMouseHoveringY(new Vector2f(MouseInputs.getOrthoX(), MouseInputs.getOrthoY())) && !xActive) {
            yPosGizmoSprite.setColor(new Vector4f(0, 0, 1, 1));
            yActive = true;
        }
        else {
            xActive = false;
            yActive = false;
            xPosGizmoSprite.setColor(new Vector4f(0,0.7f,0,0.5f));
            yPosGizmoSprite.setColor(new Vector4f(0,0,0.7f,0.5f));
        }
    }


    public void addToScene() {
        EngineWindow.Game_Editor.addToScene(posGizmoX);
        EngineWindow.Game_Editor.addToScene(posGizmoY);
    }

    public boolean isMouseHoveringX(Vector2f mousePosition) {
        // Check if the mouse position is within the bounds of either gizmo
        return isMouseHoveringGizmo(posGizmoX, mousePosition);
    }

    public boolean isMouseHoveringY(Vector2f mousePosition) {
        // Check if the mouse position is within the bounds of either gizmo
        return isMouseHoveringGizmo(posGizmoY, mousePosition);
    }

    private boolean isMouseHoveringGizmo(GameObject gizmo, Vector2f mousePosition) {
        // Get gizmo position and size
        Transform transform = gizmo.getTransform();
        Vector2f gizmoPosition = transform.getPosition();
        Vector2f gizmoSize = transform.getScale();


        // If the gizmo has rotation
        float rotation = transform.getRotation();
        if (rotation != 0) {
            // Calculate the local mouse position considering the gizmo's rotation
            float cos = (float) Math.cos(Math.toRadians(rotation));
            float sin = (float) Math.sin(Math.toRadians(rotation));
            float dx = mousePosition.x - gizmoPosition.x;
            float dy = mousePosition.y - gizmoPosition.y;
            float rotatedX = dx * cos + dy * sin; // Corrected the calculation here
            float rotatedY = -dx * sin + dy * cos; // Corrected the calculation here

            // Check if the local mouse position is within the bounds of the gizmo
            return rotatedX >= -gizmoSize.x / 2f && rotatedX <= gizmoSize.x / 2f &&
                    rotatedY >= -gizmoSize.y / 2f && rotatedY <= gizmoSize.y / 2f;
        } else {
            // If the gizmo has no rotation, we can directly check against its global bounds
            return mousePosition.x >= gizmoPosition.x - gizmoSize.x / 2f && mousePosition.x <= gizmoPosition.x + gizmoSize.x / 2f &&
                    mousePosition.y >= gizmoPosition.y - gizmoSize.y / 2f && mousePosition.y <= gizmoPosition.y + gizmoSize.y / 2f;
        }
    }
}
/*End of Gizmo class*/
