/*
 Title: Editor
 Date: 2024-04-01
 Author: Kyle St John
 */
package engine.testing;

import engine.debug.draw.DebugDraw;
import engine.debug.ui.DebugWindow;
import engine.editor.controls.EditorControls;
import engine.editor.controls.ObjectPicker;
import engine.editor.ui.AssetWindow;
import engine.editor.ui.DetailsWindow;
import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.EngineWindow;
import engine.graphics.Renderer;
import engine.utils.EConstants;
import engine.world.objects.GameObject;
import engine.world.scenes.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static engine.utils.EConstants.DEFAULT_CELL_SIZE;
import static org.lwjgl.opengl.GL11.*;

public class Editor implements EventListener {

    public Scene currentScene;
    private final AssetWindow defaultAssetWindow = new AssetWindow();
    private final DetailsWindow defaultDetailsWindow = new DetailsWindow();
    public  ObjectPicker objectPicker =
            new ObjectPicker( EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
    private EditorControls editorControls;


    @Override
    public void onEvent(Event event, GameObject gameObject) {

    }

    @Override
    public void onEvent(Event event, Scene scene) {
        if (event.getEventType() == EConstants.EventType.Load_New_Scene) {
            this.currentScene = scene;
        }
    }

    @Override
    public void onEvent(Event event) {

    }

    public void init() {
        EventDispatcher.addListener(EConstants.EventType.User, this);
        EventDispatcher.addListener(EConstants.EventType.New_Asset, this);
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);
        EventDispatcher.addListener(EConstants.EventType.Load_New_Scene, this);
    }

    public void loadScene(Scene scene) {
        currentScene = scene;
        scene.init();
        editorControls = new EditorControls(scene);
    }


    public void tick(float deltaTime) {
        currentScene.tick(deltaTime);
        editorControls.tick(deltaTime);
        renderEditor();
    }


    private void renderEditor() {
        glDisable(GL_BLEND);
        objectPicker.bind();

        glViewport(0, 0, EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.setPickingShader();
        currentScene.render();
        objectPicker.unbind();
        glEnable(GL_BLEND);
    }


    public void renderScene() {
        currentScene.render();
        drawGridLines();
    }



    public void imgui() {
        defaultAssetWindow.imgui();
        defaultDetailsWindow.imgui();
        currentScene.imgui();
        DebugWindow.imgui();
    }

    public void drawGridLines() {
        Vector2f cameraPos = currentScene.getOrthoCamera().position;

        // Calculate the size of the grid based on the camera's size
        Vector2f projectionSize = currentScene.getOrthoCamera().size;

        // Determine the number of rows and columns in the grid
        int numColumns = (int) (projectionSize.x / DEFAULT_CELL_SIZE);
        int numRows = (int) (projectionSize.y / DEFAULT_CELL_SIZE);

        // Set the color for the grid lines
        Vector3f color = new Vector3f(0.08f, 0.08f, 0.08f); // TODO: Make constants

        // Draw vertical grid lines
        for (int i = 0; i <= numColumns; i++) {
            float x = cameraPos.x + i * DEFAULT_CELL_SIZE;
            DebugDraw.addLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), color);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= numRows; i++) {
            float y = cameraPos.y + i * DEFAULT_CELL_SIZE;
            DebugDraw.addLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), color);
        }
    }
}
/*End of Editor class*/
