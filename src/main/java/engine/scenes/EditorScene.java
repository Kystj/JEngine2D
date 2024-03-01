/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scenes;

import engine.debug.DebugDraw;
import engine.graphics.OrthographicCamera;
import engine.ui.debug.DebugPanel;
import engine.ui.editor.AssetPanel;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.ui.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.ui.settings.EConstants.DEFAULT_GRID_WIDTH;

public class EditorScene extends Scene {

    private final AssetPanel assetPanel = new AssetPanel();

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera();
        loadResources();
        addGameObjToEditor();

    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        assetPanel.tick(deltaTime);
        DebugPanel.tick();
    }

    @Override
    public void render() {
         super.render();
          drawGridLines();
    }

    @Override
    public void imgui() {
        assetPanel.imgui();
        DebugPanel.imgui();
    }

    private void loadResources() {

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
