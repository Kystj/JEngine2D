/*
 Title: Transform
 Date: 2023-11-22
 Author: Kyle St John
 */
package engine.components;

import org.joml.Vector2f;

/**
 * The Transform class represents the  properties of a game object,
 * such as its position, scale, rotation, and drawing order.
 * It is an essential component for positioning and rendering objects in a 2D space.
 */
public class Transform {

    public Vector2f position;
    public Vector2f scale;
    private float rotation = 0;
    private int zIndex;

    /**
     * Constructs a Transform object with the specified initial position and scale.
     *
     * @param position The initial position of the object.
     * @param scale    The initial scale of the object.
     */
    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    /**
     * Constructs a Transform object with the specified initial position, scale, and zIndex.
     *
     * @param position The initial position of the object.
     * @param scale    The initial scale of the object.
     * @param zIndex   The initial zIndex (drawing order) of the object.
     */
    public Transform(Vector2f position, Vector2f scale, int zIndex) {
        this.position = position;
        this.scale = scale;
        this.zIndex = zIndex;
    }

    /**
     * Gets the current position of the object.
     *
     * @return The current position.
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * Sets the position of the object.
     *
     * @param position The new position to set.
     */
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    /**
     * Sets the X component of the object's scale.
     *
     * @param deltaX The change in the X component of the scale.
     */
    public void setPosX(float deltaX) {
        this.scale.x = deltaX;
    }

    /**
     * Sets the Y component of the object's scale.
     *
     * @param deltaY The change in the Y component of the scale.
     */
    public void setPosY(float deltaY) {
        this.scale.y = deltaY;
    }

    /**
     * Gets the current scale of the object.
     *
     * @return The current scale.
     */
    public Vector2f getScale() {
        return scale;
    }

    /**
     * Sets the scale of the object.
     *
     * @param scale The new scale to set.
     */
    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    /**
     * Sets the Z component of the object's position.
     *
     * @param deltaZ The change in the Z component of the position.
     */
    public void setScaleZ(float deltaZ) {
        this.position.x = deltaZ;
    }

    /**
     * Sets the Y component of the object's position.
     *
     * @param deltaY The change in the Y component of the position.
     */
    public void setScaleY(float deltaY) {
        this.position.y = deltaY;
    }

    /**
     * Gets the current rotation of the object.
     *
     * @return The current rotation.
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation of the object.
     *
     * @param rotation The new rotation to set.
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Gets the zIndex (drawing order) of the object.
     *
     * @return The zIndex of the object.
     */
    public int getZIndex() {
        return zIndex;
    }

    /**
     * Sets the zIndex (drawing order) of the object.
     *
     * @param zIndex The new zIndex to set.
     */
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * Overrides the default equals method to compare this Transform object with another object for equality.
     * Two Transform objects are considered equal if their positions, scales, rotations, and zIndex values are identical.
     * This method ensures that the provided object is of type Transform before making the comparison.
     *
     * @param t2 The transform to compare for equality.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object t2) {
        if (t2 == null) {
            return false;
        }
        if (!(t2 instanceof Transform)) {
            return false;
        }
        Transform transform = (Transform) t2;
        return transform.position.equals(this.position) && transform.scale.equals(this.scale) &&
                transform.rotation == this.rotation && transform.zIndex == this.zIndex;
    }
}
/*End of Transform class*/
