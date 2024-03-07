package engine.settings;

import org.joml.Vector2f;

/**
 * This class contains all the constants used throughout the engine
 */
public interface EConstants {

    // Popup Window constants
    Vector2f POPUP_WIN_SIZE = new Vector2f(300, 50);

    // ImGui
    int X_SPACING = 10;

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
        Wire_Frame
    }

    // Grid width
    int DEFAULT_GRID_WIDTH = 32;
    int DEFAULT_GRID_HEIGHT = 32;

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

    // Physics constants
    enum BodyType {
        staticBody,
        dynamicBody,
        kineticBody
    }
}/*End of EConstants Interface*/
