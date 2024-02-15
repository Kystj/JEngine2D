/*
 Title: OrthoCamera
 Date: 2023-12-01
 Author: Kyle St John
 */
package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * The OrthographicCamera class represents an orthographic camera used for 2D rendering.
 */
public class OrthographicCamera {

    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();
    public Vector2f position;
    public Vector2f size = new Vector2f(32.0f * 40.0f, 32.0f * 20.0f);

    /**
     * Creates an orthographic projection with a custom camera position.
     *
     * @param position The initial position of the camera.
     */
    public OrthographicCamera(Vector2f position) {
        this.position = position;
        setOrthographicProjection();
    }

    /**
     * Creates an orthographic projection with a centered camera position.
     */
    public OrthographicCamera() {
        this.position = new Vector2f(0.0f, 0.0f);
        setOrthographicProjection();
    }

    /**
     * Sets up the orthographic projection matrix based on the camera size.
     */
    public void setOrthographicProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, size.x, 0.0f, size.y, 0.0f, 50.0f);
    }

    /**
     * Calculates and returns the view matrix for the camera.
     *
     * @return The view matrix.
     */
    public Matrix4f calculateViewMatrix() {
        this.viewMatrix.identity();
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);

        return this.viewMatrix;
    }

    /**
     * Gets the projection matrix of the camera.
     *
     * @return The projection matrix.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
/*End of OrthographicCamera class*/