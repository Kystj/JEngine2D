/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.components.Sprite;
import engine.components.Transform;
import engine.graphics.OrthographicCamera;
import engine.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Editor extends Scene {

    @Override
    public void init() {
        super.init();

        this.orthoCamera = new OrthographicCamera();

        orthoCamera.position.x = -250;
        orthoCamera.position.y = -250;

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;
        float padding = 3;

        for (int x=0; x < 100; x++) {
            for (int y=0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX) + (padding * x);
                float yPos = yOffset + (y * sizeY) + (padding * y);

                GameObject go = new GameObject("Obj" + x + "" + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new Sprite(new Vector4f(1, 1, 1, 1)));
                this.addGameObject(go);
            }
        }
    }


    @Override
    public void tick() {
        super.tick();
    }

}
/*End of Editor class*/
