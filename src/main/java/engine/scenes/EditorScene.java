/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.scenes;

import engine.UI.debug.DebugPanel;
import engine.UI.editor.AssetPanel;
import engine.debug.DebugDraw;
import engine.graphics.OrthographicCamera;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static engine.UI.settings.EConstants.DEFAULT_GRID_HEIGHT;
import static engine.UI.settings.EConstants.DEFAULT_GRID_WIDTH;

public class EditorScene extends Scene {

    private final List<GameObject> gameObjectList = new ArrayList<>();
    private final AssetPanel assetPanel = new AssetPanel();

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthographicCamera();
        loadResources();
        addGameObjToEditor();

        //TODO: Delete. Test code
  /*      DebugDraw.addBox(new Vector2f(50.0f, 50.0f),
                new Vector2f(50.0f, 50.0f), 0, new Vector3f(0, 0, 1), 100000000);

        DebugDraw.addBox(new Vector2f(55.0f, 55.0f),
                new Vector2f(60.0f, 60.0f),0, new Vector3f(0, 0, 1), 100000000);*/
    }

    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
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

    }

    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjectList) {
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
