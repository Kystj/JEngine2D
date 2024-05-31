/*
 Title: RigidBody
 Date: 2024-05-01
 Author: Kyle St John
 */
package engine.physics.components;

import engine.graphics.EngineWindow;
import engine.world.components.Component;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.joml.Vector2f;

import static engine.utils.engine.EConstants.EngineMode.GameMode;
import static engine.utils.engine.EConstants.EngineMode.LaunchMode;

public class RigidBody extends Component {

    private transient Body b2Body = null;
    private BodyType b2BodyType = BodyType.DYNAMIC; // Dynamic by default
    private Vector2f linearVelocity = new Vector2f(0.0f); // TODO: Consider doing this everywhere
    private float angularVelocity = 0.0f;
    private float linearDamping = 0.0f;
    private float angularDamping = 0.0f;
    private boolean allowSleep = true;
    private boolean isAwake = true;
    private boolean fixedRotation = false;
    private boolean continuousCollision = true;
    private float gravityScale = 1.0f;
    private float mass = 0;


    public void tick(float deltaTime) {
        if (b2Body != null &&
                (EngineWindow.Enabled_Engine_Mode == GameMode  || EngineWindow.Enabled_Engine_Mode == LaunchMode)) {
            this.owningGameObject.getTransform().getPosition().set(
                    b2Body.getPosition().x, b2Body.getPosition().y);
            this.owningGameObject.getTransform().setRotation((float) Math.toDegrees(b2Body.getAngle()));
        }
    }


    public Body getB2Body() {
        return b2Body;
    }

    public void setB2Body(Body b2Body) {
        this.b2Body = b2Body;
    }

    public BodyType getBodyType() {
        return b2BodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.b2BodyType = bodyType;
    }

    public Vector2f getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(Vector2f linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public float getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public boolean isAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public boolean isAwake() {
        return isAwake;
    }

    public void setAwake(boolean awake) {
        isAwake = awake;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinuousCollision() {
        return continuousCollision;
    }

    public void setContinuousCollision(boolean continuousCollision) {
        this.continuousCollision = continuousCollision;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}
/*End of RigidBody class*/
