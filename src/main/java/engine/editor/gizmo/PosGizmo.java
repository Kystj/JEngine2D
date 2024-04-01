/*
 Title: PosGizmo
 Date: 2024-03-26
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.debug.info.DebugLogger;
import engine.io.MouseInputs;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;

public class PosGizmo extends Gizmo {

    public PosGizmo(GameObject gameObject) {
        super(gameObject);
        masterSprite = gizmoSpriteSheet.getSprite(1);

        xPosGizmoSprite = new Sprite(masterSprite.getSpriteTexture(), masterSprite.getUvCoordinates());
        yPosGizmoSprite =  new Sprite(masterSprite.getSpriteTexture(), masterSprite.getUvCoordinates());

        xPosGizmoSprite.setColor(gizmoColorX);
        yPosGizmoSprite.setColor(gizmoColorY);

        posGizmoX = new GameObject(new Transform(posGizmoXPosition, scale), -1);
        posGizmoX.addComponent(xPosGizmoSprite);

        posGizmoY = new GameObject(new Transform(posGizmoYPosition, scale), -1);
        posGizmoY.getTransform().setRotation(-90);
        posGizmoY.addComponent(yPosGizmoSprite);
    }

    @Override
    protected void tick() {
        super.tick();

        if (MouseInputs.isDragging() && (xActive || yActive)) {
            DebugLogger.warning("Is Dragging");
        }
    }
}
/*End of PosGizmo class*/