/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scene;

import engine.editor.AssetPanel;
import engine.graphics.OrthographicCamera;
import engine.objects.GameObject;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Editor extends Scene {

    private final List<GameObject> gameObjectList = new ArrayList<>();
    private final AssetPanel assetPanel = new AssetPanel();

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera(new Vector2f(-550,-550));
        createGameObjects();
        addGameObjToEditor();
    }

    @Override
    public void tick(float deltaTime) {
        // Calling super.tick() will update the scenes game objects
        super.tick(deltaTime);
    }

    @Override
    public void imgui() {
        assetPanel.tick();
    }

    private void createGameObjects() {

    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }
}
/*End of Editor class*/
