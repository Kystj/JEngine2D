package engine.ui.settings;

import org.joml.Vector2f;

/**
 * This class contains all the constants used throughout the engine
 */
public interface EConstants {

    // Popup Window constants
    Vector2f POPUP_WIN_SIZE = new Vector2f(300, 50);

    // Aspect ratios


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
        FullPlay,
        Grid_Lock
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
