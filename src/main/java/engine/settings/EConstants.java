package engine.settings;

import imgui.ImVec4;
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
    Vector3f BLACK = new Vector3f(0, 0, 0);
    Vector4f INVISIBLE = new Vector4f(0, 0, 0, 0);

    // Popup Window constants
    Vector2f POPUP_WIN_SIZE = new Vector2f(100, 50);

    // Scene Ids
    String EDITOR_SCENE_ID = "editor_scene";
    String PLAY_SCENE_ID = "play_scene";
    String PAUSE_SCENE_ID = "pause_scene";

    ImVec4 IM_RED = new ImVec4(1.0f, 0.0f, 0.0f, 1.0f);
    ImVec4 IM_BLUE = new ImVec4(0.0f, 0.0f, 1.0f, 1.0f);

    // Event Types
    enum EventType {
        Play,
        Stop,
        Save,
        Load,
        User,
        FullPlay
    }


    // Physics constants
    enum BodyType {
        staticBody,
        dynamicBody,
        kineticBody
    }
}/*End of EConstants Interface*/
