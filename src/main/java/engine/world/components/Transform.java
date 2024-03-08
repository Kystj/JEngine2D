/*
 Title: Transform
 Date: 2023-11-22
 Author: Kyle St John
 */
package engine.world.components;

import org.joml.Vector2f;

public class Transform {

    public Vector2f position;
    public Vector2f scale;
    private float rotation = 0.0f;

    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform(Vector2f position, Vector2f scale, int rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
    }


    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void setPosX(float deltaX) {
        this.scale.x = deltaX;
    }

    public void setPosY(float deltaY) {
        this.scale.y = deltaY;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public void setScaleZ(float deltaZ) {
        this.position.x = deltaZ;
    }

    public void setScaleY(float deltaY) {
        this.position.y = deltaY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }


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
                transform.rotation == this.rotation;
    }
}
/*End of Transform class*/