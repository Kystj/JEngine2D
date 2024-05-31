/*
 Title: PosGizmo
 Date: 2024-03-26
 Author: Kyle St John
 */
package engine.editor.gizmo;

import engine.io.MouseInputs;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObjFactory;
import engine.world.objects.GameObject;

import static engine.utils.engine.EConstants.*;

public class PosGizmo extends Gizmo {

    public PosGizmo(GameObject gameObject) {
        super(gameObject);
        masterSprite = gizmoSpriteSheet.getSprite(1);

        xPosGizmoSprite = new Sprite();
        xPosGizmoSprite.setTexture(masterSprite.getSpriteTexture());
        xPosGizmoSprite.setUvCoordinates(masterSprite.getUvCoordinates());
        xPosGizmoSprite.setColor(GIZMO_X_COLOR);

        yPosGizmoSprite =  new Sprite();
        yPosGizmoSprite.setTexture(masterSprite.getSpriteTexture());
        yPosGizmoSprite.setUvCoordinates(masterSprite.getUvCoordinates());
        yPosGizmoSprite.setColor(GIZMO_Y_COLOR);

        posGizmoX = GameObjFactory.generateGameObject("Gizmo_Pos_X" , xPosGizmoSprite, new Transform(xGizmoPosition, GIZMO_GAME_OBJECT_SIZE,  GIZMO_Z_INDEX));
        posGizmoY = GameObjFactory.generateGameObject("Gizmo_Pos_Y" , yPosGizmoSprite, new Transform(yGizmoPosition, GIZMO_GAME_OBJECT_SIZE,  GIZMO_Z_INDEX));
        posGizmoX.setUID(-1);
        posGizmoY.setUID(-1);
        posGizmoX.getTransform().setRotation(-90);
    }

    @Override
    protected void tick() {
        super.tick();
        if (activeGameObject != null) {
            if (xHasChanged) {
                activeGameObject.getTransform().getPosition().x -= MouseInputs.getDx();
            } else if (yHasChanged) {
                activeGameObject.getTransform().getPosition().y += MouseInputs.getDy();
            }
        }
    }
}
/*End of PosGizmo class*/