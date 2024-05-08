/*
 Title: BoxCollider
 Date: 2024-05-01
 Author: Kyle St John
 */
package engine.physics.components;

import engine.debug.draw.DebugDraw;
import org.joml.Vector2f;

public class BoxCollider extends Collider {

    private final Vector2f dimensions = new Vector2f(1);
    private final Vector2f colliderOrigin = new Vector2f();

    @Override
    public void tick(float dt) {
        drawBoxCollider();
    }

    private void drawBoxCollider() {
        // Calculate the center position of the collider in world space
        Vector2f center = new Vector2f(this.owningGameObject.getTransform().getPosition()).add(this.localPosition);
        // Use DebugDraw to visualize the collider
        DebugDraw.addBox(center, this.dimensions, this.owningGameObject.getTransform().getRotation(), colliderColor, true);
    }

    public Vector2f getDimensions() {
        return dimensions;
    }

    public void setDimensions(Vector2f dimensions) {
        this.dimensions.set(dimensions);
    }

    public Vector2f getColliderOrigin() {
        return colliderOrigin;
    }

    public void setOrigin(Vector2f origin) {
        colliderOrigin.set(origin);
    }
}
/*End of BoxCollider class*/
