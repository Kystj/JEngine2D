/*
 Title: Sprite
 Date: 2023-12-18
 Author: Kyle St John
 */
package engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;


public class Sprite extends BaseComponent {

    private Vector4f color;

    public Sprite(Vector4f color) {
        this.color = color;
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

   public Vector2f getSpriteSize() {
        return this.getOwningGameObject().getTransform().getScale();
   }

    public Vector2f getSpritePos() {
        return this.getOwningGameObject().getTransform().getPosition();
    }
}
/*End of Sprite class*/
