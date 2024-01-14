/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.components.Transform;
import engine.editor.AssetPanel;
import engine.graphics.OrthographicCamera;
import engine.graphics.SpriteSheet;
import engine.managers.ResourceManager;
import engine.objects.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Scene {

    List<GameObject> gameObjectList = new ArrayList<>();
    AssetPanel assetPanel;

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera(new Vector2f(-550,-550));
        assetPanel = new AssetPanel();

        createGameObjects();
        addGameObjToEditor();
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
    }

    @Override
    public void imgui() {
        assetPanel.tick();
    }

    private void createGameObjects() {
        SpriteSheet spriteSheet = ResourceManager.getSpriteSheet("assets/spritesheets/test2.png");

        GameObject test01 = new GameObject("Test01",
                new Transform(new Vector2f(200, 200), new Vector2f(128, 128)));
        test01.addComponent(spriteSheet.getSprite(1));
        gameObjectList.add(test01);
    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }
}
/*End of Editor class*/
