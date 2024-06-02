/*
 Title: Editor
 Date: 2024-04-01
 Author: Kyle St John
 */
package engine.editor;

import engine.debugging.draw.DebugDraw;
import engine.debugging.draw.DebugRenderer;
import engine.debugging.info.Logger;
import engine.debugging.ui.DebugPanel;
import engine.editor.controls.EditorControls;
import engine.editor.controls.ObjectPicker;
import engine.editor.ui.AssetWindow;
import engine.editor.ui.ContentWindow;
import engine.editor.ui.DetailsWindow;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.EngineWindow;
import engine.graphics.Renderer;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.serialization.LevelSerializer;
import engine.utils.engine.EConstants;
import engine.utils.engine.ResourceUtils;
import engine.world.levels.Level;
import engine.world.objects.GameObject;
import org.joml.Vector2f;

import static engine.utils.engine.EConstants.DEFAULT_CELL_SIZE;
import static engine.utils.engine.EConstants.GRID_COLOR;
import static org.lwjgl.opengl.GL11.*;

public class GameEditor implements EventListener {

    public static Level current_Level;
    private static final AssetWindow AssetWindow = new AssetWindow();
    public static final ContentWindow defaultContentWindow = new ContentWindow();
    private final DetailsWindow defaultDetailsWindow = new DetailsWindow();
    public  static ObjectPicker Object_Picker =
            new ObjectPicker( EngineWindow.get().getWindowWidth(), EngineWindow.get().getWindowHeight());
    private final EditorControls editorControls = new EditorControls();
    private boolean showPopup = false;


    @Override
    public void onEvent(Event event, GameObject gameObject) {

    }

    @Override
    public void onEvent(Event event, Level level) {
        if (event.getEventType() == EConstants.EventType.Load_New_Scene) {
            Logger.info("Loaded '" + level.getName() + "'", true);
            loadNewLevel(level);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event.getEventType() == EConstants.EventType.Save) {
            showPopup = true;
        }
    }

    public void init() {
        EventDispatcher.addListener(EConstants.EventType.Load_New_Scene, this);
        EventDispatcher.addListener(EConstants.EventType.Save, this);

        ResourceUtils.addSpriteSheet("run", new SpriteSheet(
                new Texture("assets/spritesheets/runVFX.png") , 19, 25, 0, "Player")
        );


        DebugRenderer.init();
    }

    public void loadNewLevel(Level level) {
        LevelSerializer.load(level);
        current_Level = level;
        current_Level.init();
        editorControls.setLevel(level);
    }


    public void tick(float deltaTime) {
        current_Level.tick(deltaTime);
        editorControls.tick(deltaTime);
    }

    public void physicsTick(float deltaTime) {
        current_Level.getPhysics().tick(deltaTime);
    }


    public void renderEditor() {
        glDisable(GL_BLEND);

        Object_Picker.bind();
        Renderer.setPickingShader();
        current_Level.render();
        Object_Picker.unbind();

        glEnable(GL_BLEND);

        drawGridLines();
    }


    public void renderLevel() {
        current_Level.render();
    }


    public void imgui() {
        defaultContentWindow.imgui();
        defaultDetailsWindow.imgui();
        AssetWindow.imgui();
        AnimationEditor.imgui();
        DebugPanel.imgui();
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
            DebugDraw.addDebugLine(new Vector2f(x, cameraPos.y), new Vector2f(x, cameraPos.y + projectionSize.y), GRID_COLOR);
        }

        // Draw horizontal grid lines
        for (int i = 0; i <= numRows; i++) {
            float y = cameraPos.y + i * DEFAULT_CELL_SIZE;
            DebugDraw.addDebugLine(new Vector2f(cameraPos.x, y), new Vector2f(cameraPos.x + projectionSize.x, y), GRID_COLOR);
        }
    }
}
/*End of Editor class*/
