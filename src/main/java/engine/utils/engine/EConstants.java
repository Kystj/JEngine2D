package engine.utils.engine;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public interface EConstants {

    // Colors
    Vector3f V3_RED = new Vector3f(1,0,0);

    // Gizmos
    int GIZMO_GAME_OBJECT_UID = -1;
    int GIZMO_Z_INDEX = 100;
    int GIZMO_OFFSET = 16;

    Vector4f GIZMO_X_HOVER_COLOR = new Vector4f(0, 1, 0, 1);
    Vector4f GIZMO_Y_HOVER_COLOR = new Vector4f(1, 0, 0, 1);
    Vector4f GIZMO_X_COLOR = new Vector4f(0,0.7f,0,0.5f);
    Vector4f GIZMO_Y_COLOR = new Vector4f(0.7f, 0, 0, 1);
    Vector2f GIZMO_GAME_OBJECT_SIZE = new Vector2f(26, 26);

    // ImGui
    int X_SPACING = 10;
    int COLUMN_WIDTH_LARGE = 160;

    Vector2f[] TEXTURE_COORDINATES = new Vector2f[]{
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };


    Vector4f GREEN_BUTTON = new Vector4f(0.0f, 1.0f, 0.0f, 0.5f);
    Vector4f RED_BUTTON = new Vector4f(1.0f, 0.0f, 0.0f, 0.5f);
    Vector4f YELLOW_BUTTON = new Vector4f(1.0f, 1.0f, 0.0f, 0.5f);

    // Event Types
    enum EventType {
        Play,
        Stop,
        Save,
        Load,
        User,
        Launch,
        Grid_Lock,
        Grid_Size,
        Wire_Frame,
        New_Asset,
        Active_Object,
        Load_New_Scene
    }

    // Engine mode
    enum EngineMode {
        GameMode,
        EditorMode,
        LaunchMode
    }

    // Window
    float DEFAULT_ASPECT_RATIO = 16.0f / 9.0f;

    // Grid
    int DEFAULT_GRID_WIDTH = 32;
    int DEFAULT_GRID_HEIGHT = 32;
    int DEFAULT_CELL_SIZE = 16;

    Vector3f GRID_COLOR = new Vector3f(0.08f, 0.08f, 0.08f);

    // Rendering
    int MAX_BATCH_SIZE = 1000;
    int MAX_DEBUG_LINES = 10000;

    float DEBUG_LINE_WIDTH = 3.0f;

    // ANSI escape codes for colors
    String RESET = "\u001B[0m";
    String BLACK = "\u001B[30m";
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String YELLOW = "\u001B[33m";
    String BLUE = "\u001B[34m";
    String PURPLE = "\u001B[35m";
    String CYAN = "\u001B[36m";
    String WHITE = "\u001B[37m";
}/*End of EConstants Interface*/
