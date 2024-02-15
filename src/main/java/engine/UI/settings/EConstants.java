package engine.UI.settings;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * This class contains all the constants used throughout the engine
 */
public interface EConstants {

    // Color constants
    Vector3f RED = new Vector3f(1, 0, 0);
    Vector3f BLUE = new Vector3f(0, 0, 1);
    Vector3f GREEN = new Vector3f(0, 1, 0);
    Vector3f WHITE = new Vector3f(1, 1, 1);
    Vector4f BLACK = new Vector4f(0, 0, 0, 1);
    Vector4f INVISIBLE = new Vector4f(0, 0, 0, 0);

    // Popup Window constants
    Vector2f POPUP_WIN_SIZE = new Vector2f(300, 50);

    // ImGui
    int X_SPACING = 10;
    int Y_SPACING = 10;

    // Event Types
    enum EventType {
        Play,
        Stop,
        Save,
        Load,
        User,
        FullPlay
    }

    // Grid width
    int DEFAULT_GRID_WIDTH = 32;
    int DEFAULT_GRID_HEIGHT = 32;

    // Rendering
    int MAX_BATCH_SIZE = 1000;
    int MAX_DEBUG_LINES = 10000;
    float DEBUG_LINE_WIDTH = 3.0f;

    // Physics constants
    enum BodyType {
        staticBody,
        dynamicBody,
        kineticBody
    }
}/*End of EConstants Interface*/
