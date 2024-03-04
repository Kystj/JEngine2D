/*
 Title: OrthoCamera
 Date: 2023-12-01
 Author: Kyle St John
 */
package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class OrthographicCamera {

    private final Matrix4f viewMatrix = new Matrix4f();
    private final Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f inverseProjection = new Matrix4f();
    private Matrix4f inverseView = new Matrix4f();

    public Vector2f position;
    public Vector2f size = new Vector2f(32.0f * 40.0f, 32.0f * 20.0f);

    public OrthographicCamera(Vector2f position) {
        this.position = position;
        setOrthographicProjection();
    }

    public OrthographicCamera() {
        this.position = new Vector2f(0.0f, 0.0f);
        setOrthographicProjection();
    }

    public void setOrthographicProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, size.x, 0.0f, size.y, 0.0f, 50.0f);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f calculateViewMatrix() {
        this.viewMatrix.identity();
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);

        this.viewMatrix.invert(inverseView);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getInverseProjection() {
        return inverseProjection;
    }

    public Matrix4f getInverseView() {
        return inverseView;
    }

}
/*End of OrthographicCamera class*/