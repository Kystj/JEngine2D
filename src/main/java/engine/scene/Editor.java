/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.components.Transform;
import engine.graphics.OrthographicCamera;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.objects.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Scene {

    SpriteSheet spriteSheet;
    List<GameObject> gameObjectList = new ArrayList<>();

    @Override
    public void init() {
        super.init();

        this.orthoCamera = new OrthographicCamera(new Vector2f(-250,-250));

        loadSpriteSheets();
        createGameObjects();
        addGameObjToEditor();
    }

    private void createGameObjects() {
        GameObject test01 = new GameObject("Test01",
                new Transform(new Vector2f(100, 100), new Vector2f(128, 128)));
        test01.addComponent(spriteSheet.getSprite(0));
        gameObjectList.add(test01);

        GameObject test02 = new GameObject("Test02",
                new Transform(new Vector2f(100, 300), new Vector2f(128, 128)));
        test02.addComponent(spriteSheet.getSprite(1));
        gameObjectList.add(test02);

        GameObject test03 = new GameObject("Test03",
                new Transform(new Vector2f(100, 500), new Vector2f(128, 128)));
        test03.addComponent(spriteSheet.getSprite(2));
        gameObjectList.add(test03);
    }

    private void loadSpriteSheets() {
        // TODO: Switch spreadsheet creation to the resource manager
        this.spriteSheet = new SpriteSheet(
                new Texture("textures/testSpriteSheet.png"),
                16, 16,0,3);
    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }


    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        this.renderer.render();
    }

}
/*End of Editor class*/
