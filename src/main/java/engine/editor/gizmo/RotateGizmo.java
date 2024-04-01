/*
 Title: RotateGizmo
 Date: 2024-03-26
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;

public class RotateGizmo extends Gizmo {

    public RotateGizmo(GameObject gameObject) {
        super(gameObject);
        masterSprite = gizmoSpriteSheet.getSprite(2);

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
    }
}
/*End of RotateGizmo class*/
