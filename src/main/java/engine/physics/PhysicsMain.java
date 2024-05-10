/*
 Title: PhysicsControls
 Date: 2024-05-01
 Author: Kyle St John
 */
package engine.physics;

import engine.physics.components.BoxCollider;
import engine.physics.components.CircleCollider;
import engine.physics.components.Collider;
import engine.physics.components.RigidBody;
import engine.world.components.Transform;
import engine.world.objects.GameObject;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class PhysicsMain {

    private World b2World;
    private float physicsTime;
    private float physicsTimeStep;
    private int velocityIterations;
    private int positionIterations;


    public void init() {
        Vec2 worldGravity = new Vec2(0, -10.0f);
        b2World =  new World(worldGravity);
        physicsTime = 0.0f;
        physicsTimeStep = 1.0f / 60.f;
        velocityIterations = 10;
        positionIterations = 8;
        b2World.setDebugDraw(null);
        b2World.setAllowSleep(true);
    }

    public void tick(float deltaTime) {
        // Use double for time accumulation
        physicsTime += deltaTime;

        // Check if enough time has accumulated for at least one physics step
        while (physicsTime >= physicsTimeStep) {
            // Perform physics step
            b2World.step(physicsTimeStep, velocityIterations, positionIterations);

            // Subtract the time step from accumulated time
            physicsTime -= physicsTimeStep;
        }
    }

    public void add(GameObject go) {
        RigidBody rb = go.getComponent(RigidBody.class);
        if (rb != null && rb.getB2Body() == null) {
            Transform transform = go.getTransform();

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.getRotation());
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType()) {
                case KINEMATIC:
                    bodyDef.type = BodyType.KINEMATIC;
                    break;
                case STATIC:
                    bodyDef.type = BodyType.STATIC;
                    break;
                case DYNAMIC:
                    bodyDef.type = BodyType.DYNAMIC;
                    break;
            }

            Collider collider = go.getComponent(Collider.class);
            if (collider != null) {
                Shape shape;

                if (collider instanceof CircleCollider) {
                    CircleCollider circleCollider = (CircleCollider) collider;
                    shape = new CircleShape();
                    shape.setRadius(circleCollider.getColliderRadius());
                } else {

                    BoxCollider boxCollider = (BoxCollider) collider;
                    Vector2f halfSize = boxCollider.getDimensions().mul(0.5f);
                    Vector2f localPos = boxCollider.getLocalPosition();
                    Vector2f origin = boxCollider.getColliderOrigin();

                    shape = new PolygonShape();
                    ((PolygonShape) shape).setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);
                    bodyDef.position.addLocal(localPos.x, localPos.y);
                }

                Body body = this.b2World.createBody(bodyDef);
                rb.setB2Body(body);
                body.createFixture(shape, rb.getMass());
            }
        }
    }

    public void destroyPhysicsBody(GameObject gameObject) {
        RigidBody rigidBody= gameObject.getComponent(RigidBody.class);
        if (rigidBody.getB2Body() != null) {
            b2World.destroyBody(rigidBody.getB2Body());
            rigidBody.setB2Body(null);
        }
    }
}
/*End of PhysicsControls class*/
