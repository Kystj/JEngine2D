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

    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionMatrix = new Matrix4f();
    protected Vector2f position;

    /**  Creates an orthographic projection with a custom camera position*/
    public OrthographicCamera(Vector2f position) {
        this.position = position;
        setOrthographicProjection();
    }

    /**  Creates an orthographic projection with a centred camera position*/
    public OrthographicCamera() {
        this.position = new Vector2f(0.0f, 0.0f);
        setOrthographicProjection();
    }

    public void setOrthographicProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f * 21.0f,
                0.0f, 100.0f);
        }


    public Matrix4f calculateViewMatrix() {
        this.viewMatrix.identity();
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f),
                cameraFront.add(position.x, position.y, 0.0f),
                cameraUp);
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
/*End of OrthographicCamera class*/
