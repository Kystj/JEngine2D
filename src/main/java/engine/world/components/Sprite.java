/*
 * Title: Sprite
 * Date: 2023-12-18
 * Author: Kyle St John
 */
package engine.world.components;

import engine.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;

/** Sprite class representing a 2D sprite component for game entities. */
public class Sprite extends Component {

    // Color of the sprite
    private final Vector4f color = new Vector4f(1, 1, 1, 1);

    // Texture used for the sprite
    private final Texture spriteTexture;

    // UV coordinates for texture mapping
    private final Vector2f[] uvCoordinates;

    // Flag indicating if the sprite has been modified
    private boolean bisModified = false;

    // Transform of the sprite
    private Transform spriteTransform;

    /** Constructor with default UV coordinates */
    public Sprite(Texture texture) {
        this.spriteTexture = texture;
        this.uvCoordinates  = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        init();
    }

    /** Constructor with custom UV coordinates */
    public Sprite(Texture texture, Vector2f[] textureCoordinate) {
        this.spriteTexture = texture;
        this.uvCoordinates = textureCoordinate;
        init();
    }

    /**
     * Initializes the sprite component.
     */
    @Override
    public void init() {
        super.init();
    }

    /**
     * Updates the sprite component.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        //updateTransform();
    }

    /**
     * Updates the transform of the sprite if it has been modified.
     */
    private void updateTransform() {
        if (!this.spriteTransform.equals(owningGameObject.transform)) {
            owningGameObject.setTransform(spriteTransform);
            bisModified = true;
        }
    }

    /**
     * Gets the color of the sprite.
     *
     * @return The color vector.
     */
    public Vector4f getColor() {
        return this.color;
    }

    /**
     * Gets the texture used for the sprite.
     *
     * @return The sprite texture.
     */
    public Texture getSpriteTexture() {
        return spriteTexture;
    }

    /**
     * Gets the UV coordinates for texture mapping.
     *
     * @return The array of UV coordinates.
     */
    public Vector2f[] getUvCoordinates() {
        return uvCoordinates;
    }

    /**
     * Gets the size of the sprite.
     *
     * @return The size vector.
     */
    public Vector2f getSpriteSize() {
        return this.getOwningGameObject().getTransform().getScale();
    }

    /**
     * Gets the position of the sprite.
     *
     * @return The position vector.
     */
    public Vector2f getSpritePos() {
        return this.getOwningGameObject().getTransform().getPosition();
    }

    /**
     * Checks if the sprite has been modified.
     *
     * @return True if the sprite has been modified, false otherwise.
     */
    public boolean isBisModified() {
        return bisModified;
    }

    /**
     * Gets the texture ID of the sprite.
     *
     * @return The texture ID, or -1 if the texture is null.
     */
    public int getTextureID() {
        return spriteTexture == null ? -1 : spriteTexture.getTextureID();
    }
}
/* End of Sprite class */
