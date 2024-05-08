/*
 Title: Collider
 Date: 2024-05-01
 Author: Kyle St John
 */
package engine.physics.components;

import engine.utils.EConstants;
import engine.world.components.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Collider extends Component {

    protected Vector2f localPosition = new Vector2f();
    protected Vector3f colliderColor = EConstants.V3_RED; // Defaults to red
    protected float colliderRotation = 0.0f;

    public Vector2f getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(Vector2f localPosition) {
        this.localPosition.set(localPosition);
    }

    public Vector3f getColliderColor() {
        return colliderColor;
    }

    public void setColliderColor(Vector3f colliderColor) {
        this.colliderColor.set(colliderColor);
    }

    public float getColliderRotation() {
        return colliderRotation;
    }

    public void setColliderRotation(float colliderRotation) {
        this.colliderRotation = colliderRotation;
    }
}
/*End of Collider class*/
