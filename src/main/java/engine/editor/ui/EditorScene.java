/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.editor.ui;

import engine.debug.draw.DebugDraw;
import engine.editor.controls.EditorControls;
import engine.graphics.OrthoCamera;
import engine.editor.controls.ObjectPicker;
import engine.graphics.Renderer;
import engine.debug.ui.DebugWindow;
import engine.graphics.EngineWindow;
import engine.world.objects.GameObject;
import engine.world.scenes.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class EditorScene extends Scene {

    private final AssetWindow defaultAssetWindow = new AssetWindow();
    private final DetailsWindow defaultDetailsWindow = new DetailsWindow();
    private static int cellSize = 16;
    private final EditorControls editorControls = new EditorControls();
    public static ObjectPicker objectPicker =
            new ObjectPicker( EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());

    @Override
    public void init() {
        super.init();
        this.orthoCamera = new OrthoCamera();
        loadResources();
        addGameObjToEditor();
    }


    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
        editorControls.tick(deltaTime);
        renderForPicking();
    }


    @Override
    public void imgui() {
        super.imgui();
        DebugWindow.imgui();
        defaultAssetWindow.imgui();
        defaultDetailsWindow.imgui();
    }


    @Override
    public void render() {
        super.render();
        drawGridLines();
    }


    public void renderForPicking() {
        glDisable(GL_BLEND);
        objectPicker.bind();

        glViewport(0, 0, EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.setPickingShader();
        render();
        objectPicker.unbind();
        glEnable(GL_BLEND);
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

        // Calculate the size of the grid based on the camera's size
        Vector2f projectionSize = orthoCamera.size;

        // Determine the number of rows and columns in the grid
        int numColumns = (int) (projectionSize.x / cellSize);
        int numRows = (int) (projectionSize.y / cellSize);

        // Set the color for the grid lines
        Vector3f color = new Vector3f(0.08f, 0.08f, 0.08f); // TODO: Make constants

        // Draw vertical grid lines
        for (int i = 0; i <= numColumns; i++) {
            float x = cameraPos.x + i * cellSize;
            DebugDraw.addLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), color);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= numRows; i++) {
            float y = cameraPos.y + i * cellSize;
            DebugDraw.addLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), color);
        }
    }


    public static void setCellSize(int cellSize) {
        EditorScene.cellSize = cellSize;
    }
}
/*End of Editor class*/