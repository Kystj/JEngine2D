/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.components.Sprite;
import engine.components.Transform;
import engine.debug.DebugPanel;
import engine.debug.Draw;
import engine.editor.AssetPanel;
import engine.graphics.OrthographicCamera;
import engine.managers.ResourceManager;
import engine.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Scene {

    private final List<GameObject> gameObjectList = new ArrayList<>();
    private final AssetPanel assetPanel = new AssetPanel();

    private GameObject obj1;
    Sprite obj1Sprite;

    // TODO: DELETE
    float x = 200.0f;
    float y = 200.0f;

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera(new Vector2f(-250,-250));
        createGameObjects();
        addGameObjToEditor();
    }


    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);

        Draw.addBox2D(new Vector2f(x, y), new Vector2f(256, 256), 0f, new Vector3f(1, 0, 0), 2);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void imgui() {
        assetPanel.tick();
        DebugPanel.tick();
    }

    private void createGameObjects() {
        // FIRST GAME OBJECT
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
                new Vector2f(256, 256)));
        obj1Sprite = new Sprite(ResourceManager.getSpriteSheet("assets/spritesheets/test.png").getSprite(1).getSpriteTexture());
        obj1.addComponent(obj1Sprite);
        gameObjectList.add(obj1);
    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }

}
/*End of Editor class*/
