/*
 Title: Sprite
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.components;

import engine.graphics.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class Sprite extends BaseComponent {

    private Vector4f color;

    private final Texture spriteTexture;
    private final Vector2f[] uvCoordinates;

    public Sprite(Texture texture) {
        this.spriteTexture = texture;
        this.color = new Vector4f(1, 1, 1, 1);
        this.uvCoordinates  = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        init();
    }

    public Sprite(Texture texture, Vector2f[] textureCoordinate) {
        this.spriteTexture = texture;
        this.uvCoordinates = textureCoordinate;
        init();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
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
}
/*End of Sprite class*/
