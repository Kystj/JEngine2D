/*
 Title: RotateGizmo
 Date: 2024-03-26
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.debug.info.DebugLogger;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;

import static engine.utils.EConstants.*;

public class RotateGizmo extends Gizmo {

    public RotateGizmo(GameObject gameObject) {
        super(gameObject);
        masterSprite = gizmoSpriteSheet.getSprite(2);

        xPosGizmoSprite = new Sprite(masterSprite.getSpriteTexture(), masterSprite.getUvCoordinates());
        yPosGizmoSprite =  new Sprite(masterSprite.getSpriteTexture(), masterSprite.getUvCoordinates());

        xPosGizmoSprite.setColor(gizmoColorX);
        yPosGizmoSprite.setColor(gizmoColorY);

        posGizmoX = new GameObject(new Transform(posGizmoXPosition, GIZMO_GAME_OBJECT_SIZE, GIZMO_Z_INDEX), GIZMO_GAME_OBJECT_UID);
        posGizmoX.addComponent(xPosGizmoSprite);

        posGizmoY = new GameObject(new Transform(posGizmoYPosition, GIZMO_GAME_OBJECT_SIZE , GIZMO_Z_INDEX), GIZMO_GAME_OBJECT_UID);
        posGizmoY.getTransform().setRotation(-90);
        posGizmoY.addComponent(yPosGizmoSprite);
    }

    @Override
    protected void tick() {
        super.tick();

        if (xHasChanged) {
            DebugLogger.warning("PosGizmo X Activated");
            xHasChanged = false;
        }

        if (yHasChanged) {
            DebugLogger.warning("PosGizmo Y Activated");
            yHasChanged = false;
        }
    }
}
/*End of RotateGizmo class*/
