/*
 Title: Editor
 Date: 2024-04-01
 Author: Kyle St John
 */
package engine.editor;

import engine.debug.draw.DebugDraw;
import engine.debug.info.DebugLogger;
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
import engine.serialization.LevelSerializer;
import engine.utils.EConstants.EventType;
import engine.editor.ui.ImportWindow;
import engine.world.objects.GameObject;
import engine.world.levels.Level;
import org.joml.Vector2f;

import static engine.utils.EConstants.DEFAULT_CELL_SIZE;
import static engine.utils.EConstants.GRID_COLOR;
import static org.lwjgl.opengl.GL11.*;

public class GameEditor implements EventListener {

    public static Level current_Level;
    private static final ImportWindow Import_Utils = new ImportWindow();
    private final AssetWindow defaultAssetWindow = new AssetWindow();
    private final DetailsWindow defaultDetailsWindow = new DetailsWindow();
    public  static ObjectPicker Object_Picker =
            new ObjectPicker( EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
    private EditorControls editorControls = new EditorControls();


    @Override
    public void onEvent(Event event, GameObject gameObject) {

    }

    @Override
    public void onEvent(Event event, Level level) {
        if (event.getEventType() == EventType.Load_New_Scene) {
            DebugLogger.info("Loaded '" + level.getName() + "'", true);
            loadNewScene(level);
        }
    }

    @Override
    public void onEvent(Event event) {

    }

    public void init() {
        EventDispatcher.addListener(EventType.Load_New_Scene, this);
    }

    public void loadNewScene(Level level) {
        LevelSerializer.load(level);
        current_Level = level;
        current_Level.init();
        editorControls.setLevel(level);
    }


    public void tick(float deltaTime) {
        current_Level.tick(deltaTime);
        editorControls.tick(deltaTime);
    }


    public void renderEditor() {
        glDisable(GL_BLEND);
        Object_Picker.bind();

        glViewport(0, 0, EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer.setPickingShader();
        current_Level.render();
        Object_Picker.unbind();
        glEnable(GL_BLEND);

        drawGridLines();
    }


    public void renderScene() {
        current_Level.render();
    }


    public void imgui() {
        defaultAssetWindow.imgui();
        defaultDetailsWindow.imgui();
        Import_Utils.imgui();
        DebugWindow.imgui();
    }


    public void addToScene(GameObject obj) {
        current_Level.addGameObject(obj);
    }



    public void drawGridLines() {
        Vector2f cameraPos = current_Level.getOrthoCamera().position;

        // Calculate the size of the grid based on the camera's size
        Vector2f projectionSize = current_Level.getOrthoCamera().size;

        // Determine the number of rows and columns in the grid
        int numColumns = (int) (projectionSize.x / DEFAULT_CELL_SIZE);
        int numRows = (int) (projectionSize.y / DEFAULT_CELL_SIZE);

        // Draw vertical grid lines
        for (int i = 0; i <= numColumns; i++) {
            float x = cameraPos.x + i * DEFAULT_CELL_SIZE;
            DebugDraw.addLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), GRID_COLOR);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= numRows; i++) {
            float y = cameraPos.y + i * DEFAULT_CELL_SIZE;
            DebugDraw.addLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), GRID_COLOR);
        }
    }
}
/*End of Editor class*/
