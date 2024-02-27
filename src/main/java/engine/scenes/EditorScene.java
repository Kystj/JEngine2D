/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scenes;

import engine.debug.DebugDraw;
import engine.graphics.OrthographicCamera;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.ui.debug.DebugPanel;
import engine.ui.editor.AssetPanel;
import engine.utils.ResourceHandler;
import engine.world.components.Sprite;
import engine.world.components.Transform;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.ui.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.ui.settings.EConstants.DEFAULT_GRID_WIDTH;

public class EditorScene extends Scene {

    private final AssetPanel assetPanel = new AssetPanel();
    private GameObject obj1;
    float count = 0.0f;

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera();
        loadResources();
        addGameObjToEditor();

        obj1 = new GameObject("obj1", new Transform(new Vector2f(200, 200),
                new Vector2f(200,200) ,45), 1);

        obj1.addComponent(new Sprite(ResourceHandler.getSpriteSheet("assets/spritesheets/spritesheet.png").
                getSprite(0).getSpriteTexture()));

        addGameObject(obj1);
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        obj1.getComponent(Sprite.class).getTransform().setPosition(new Vector2f(300 + count, 300 + count));
        count += 0.01f;
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

    private void loadResources() {
        ResourceHandler.addSpriteSheet("assets/spritesheets/spritesheet.png",
                new SpriteSheet(new Texture("assets/spritesheets/spritesheet.png"),
                        16, 16,
                        0, 3, "test_01"));
    }


    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjects) {
            this.addGameObject(gameObject);
        }
    }

    public void drawGridLines() {
        Vector2f cameraPos = orthoCamera.position;
        Vector2f projectionSize = orthoCamera.size;

        // Calculate the starting positions of grid lines based on the camera position
        int firstX = ((int) cameraPos.x / DEFAULT_GRID_WIDTH) * DEFAULT_GRID_WIDTH;
        int firstY = ((int) cameraPos.y / DEFAULT_GRID_HEIGHT) * DEFAULT_GRID_HEIGHT;

        // Calculate the number of vertical and horizontal grid lines within the viewport
        int numVtLines = (int) (projectionSize.x / DEFAULT_GRID_WIDTH) + 1;
        int numHzLines = (int) (projectionSize.y / DEFAULT_GRID_HEIGHT) + 1;

        // Set the color for the grid lines
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

        // Draw vertical grid lines
        for (int i = 0; i < numVtLines; i++) {
            int x = firstX + (DEFAULT_GRID_WIDTH * i);
            DebugDraw.addLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), color);
        }

        // Draw horizontal grid lines
        for (int i = 0; i < numHzLines; i++) {
            int y = firstY + (DEFAULT_GRID_HEIGHT * i);
            DebugDraw.addLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), color);
        }
    }
}
/*End of Editor class*/
