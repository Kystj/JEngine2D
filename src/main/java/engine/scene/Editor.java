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
import engine.graphics.Texture;
import engine.managers.ResourceManager;
import engine.objects.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Scene {

    List<SpriteSheet> spriteSheets = new ArrayList<>();
    List<GameObject> gameObjectList = new ArrayList<>();
    AssetPanel assetPanel;

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera(new Vector2f(-550,-550));
        loadSpriteSheets();
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
        GameObject test01 = new GameObject("Test01",
                new Transform(new Vector2f(100, 100), new Vector2f(128, 128)));
        test01.addComponent(spriteSheets.get(0).getSprite(1));
        gameObjectList.add(test01);

        GameObject test02 = new GameObject("Test02",
                new Transform(new Vector2f(400, 400), new Vector2f(128, 128)));
        test02.addComponent(spriteSheets.get(1).getSprite(2));
        gameObjectList.add(test02);
    }

    //TODO: Clean this up
    private void loadSpriteSheets() {
        ResourceManager.addSpriteSheet("textures/test.png",
                new SpriteSheet( new Texture("textures/test.png"),
                        16, 16,0,3));

        ResourceManager.addSpriteSheet("textures/test2.png",
                new SpriteSheet( new Texture("textures/test2.png"),
                        16, 16,0,3));

        this.spriteSheets.add(ResourceManager.getSpriteSheet("textures/test.png"));
        this.spriteSheets.add(ResourceManager.getSpriteSheet("textures/test2.png"));
        assetPanel = new AssetPanel(spriteSheets);
    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }
}
/*End of Editor class*/
