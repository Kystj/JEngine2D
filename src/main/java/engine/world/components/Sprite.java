/*
 * Title: Sprite
 * Date: 2023-12-18
 * Author: Kyle St John
 */
package engine.world.components;

import engine.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Sprite extends Component {

    // Color of the sprite
    private final Vector4f color = new Vector4f(1, 1, 1, 1);

    // Texture used for the sprite
    private Texture spriteTexture;

    // UV coordinates for texture mapping
    private Vector2f[] uvCoordinates;

    // Transform of the sprite
    private Transform transform;
    private transient boolean bisModified = false;

    public Sprite() {
        this.uvCoordinates  = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
    }


    @Override
    public void init() {
        super.init();
        this.transform = owningGameObject.getTransform().copy();
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        updateTransform();
    }


    private void updateTransform() {
        if (!this.transform.equals(this.owningGameObject.getTransform())) {
            this.transform.copy(this.owningGameObject.getTransform());
            bisModified = true;
        }
    }

    public Vector4f getColor() {
        return this.color;
    }

    public Texture getSpriteTexture() {
        return spriteTexture;
    }

    public Vector2f[] getUvCoordinates() {
        return uvCoordinates;
    }

    public Vector2f getSpriteSize() {
        return this.getOwningGameObject().getTransform().getScale();
    }

    public Vector2f getSpritePos() {
        return this.getOwningGameObject().getTransform().getPosition();
    }

    public float getSpriteRotation() {
        return this.getOwningGameObject().getTransform().getRotation();
    }

    public boolean isBisModified() {
        return bisModified;
    }

    public void setModified(boolean isModified) {
        bisModified = isModified;
    }

    public int getTextureID() {
        return spriteTexture == null ? -1 : spriteTexture.getTextureID();
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTexture(Texture texture) {
        spriteTexture = texture;
    }

    public void setUvCoordinates(Vector2f[] uvCoordinates) {
        this.uvCoordinates = uvCoordinates;
    }


    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.bisModified = true;
            this.color.set(color);
        }
    }
}
/* End of Sprite class */
