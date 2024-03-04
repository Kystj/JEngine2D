/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.world.scenes;

import engine.debug.DebugDraw;
import engine.graphics.OrthographicCamera;
import engine.ui.debug.DebugPanel;
import engine.ui.editor.AssetPanel;
import engine.world.objects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;

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

        // Set the desired grid cell size
        float cellSize = 16.0f;

        // Calculate the size of the grid based on the camera's size
        Vector2f projectionSize = orthoCamera.size;

        // Determine the number of rows and columns in the grid
        int numColumns = (int) (projectionSize.x / cellSize);
        int numRows = (int) (projectionSize.y / cellSize);

        // Calculate the actual size of each grid cell
        float cellWidth = projectionSize.x / numColumns;
        float cellHeight = projectionSize.y / numRows;

        System.out.println("Cell width: " + cellWidth);
        System.out.println("Cell height: " + cellHeight);

        // Set the color for the grid lines
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);

        // Draw vertical grid lines
        for (int i = 0; i <= numColumns; i++) {
            float x = cameraPos.x + i * cellWidth;
            DebugDraw.addLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), color);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= numRows; i++) {
            float y = cameraPos.y + i * cellHeight;
            DebugDraw.addLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), color);
        }
    }
}
/*End of Editor class*/
