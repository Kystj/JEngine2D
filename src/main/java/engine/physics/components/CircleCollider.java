/*
 Title: CircleCollider
 Date: 2024-05-01
 Author: Kyle St John
 */
package engine.physics.components;

import engine.debug.draw.DebugDraw;
import org.joml.Vector2f;

public class CircleCollider extends Collider {

    private float colliderRadius = 1;

    @Override
    public void tick(float dt) {
        drawBoxCollider();
    }

    private void drawBoxCollider() {
        // Calculate the center position of the collider in world space
        Vector2f center = new Vector2f(this.owningGameObject.getTransform().getPosition()).add(this.localPosition);
        // Use DebugDraw to visualize the collider
        DebugDraw.addCircle(center, colliderRadius,
                this.owningGameObject.getTransform().getRotation(), colliderColor, 36, true);
    }

    public float getColliderRadius() {
        return colliderRadius;
    }

    public void setColliderRadius(float colliderRadius) {
        this.colliderRadius = colliderRadius;
    }
}
/*End of CircleCollider class*/
