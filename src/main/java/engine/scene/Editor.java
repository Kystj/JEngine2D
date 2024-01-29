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

import static engine.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.settings.EConstants.DEFAULT_GRID_WIDTH;

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
        this.orthoCamera = new OrthographicCamera();
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

        drawGridLines();


    }

    @Override
    public void imgui() {
        assetPanel.tick();
        DebugPanel.tick();
    }

    private void createGameObjects() {
        // FIRST GAME OBJECT
        obj1 = new GameObject("Object 1", new Transform(new Vector2f(500, 500),
                new Vector2f(500, 500)));
        obj1Sprite = new Sprite(ResourceManager.getSpriteSheet("assets/spritesheets/test.png").getSprite(1).getSpriteTexture());
        obj1.addComponent(obj1Sprite);
        gameObjectList.add(obj1);
    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
            this.addGameObject(gameObject);
        }
    }

    public void drawGridLines() {

        Vector2f cameraPos = orthoCamera.position;
        Vector2f projectionSize = orthoCamera.size;

        int firstX = ((int)(cameraPos.x / DEFAULT_GRID_WIDTH) - 1) * DEFAULT_GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / DEFAULT_GRID_HEIGHT) - 1) * DEFAULT_GRID_HEIGHT;

        int numVtLines = (int)(projectionSize.x / DEFAULT_GRID_WIDTH) + 2;
        int numHzLines = (int)(projectionSize.y / DEFAULT_GRID_HEIGHT) + 2;

        int height = (int)projectionSize.y + DEFAULT_GRID_HEIGHT * 2;
        int width = (int)projectionSize.x + DEFAULT_GRID_WIDTH * 2;

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i=0; i < maxLines; i++) {
            int x = firstX + (DEFAULT_GRID_WIDTH * i);
            int y = firstY + (DEFAULT_GRID_HEIGHT * i);

            if (i < numVtLines) {
                Draw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                Draw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
/*End of Editor class*/
