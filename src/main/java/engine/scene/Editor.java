/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.components.Sprite;
import engine.components.Transform;
import engine.graphics.OrthographicCamera;
import engine.graphics.Texture;
import engine.objects.GameObject;
import org.joml.Vector2f;

public class Editor extends Scene {

    @Override
    public void init() {
        super.init();

        this.orthoCamera = new OrthographicCamera(new Vector2f(-250,-250));

        GameObject test01 = new GameObject("Test01",
                new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        test01.addComponent(new Sprite(new Texture("textures/trippy.png")));
        this.addGameObject(test01);
    }


    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        this.renderer.render();
    }

}
/*End of Editor class*/
