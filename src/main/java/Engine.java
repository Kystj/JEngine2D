/*
 Title: EngineMain
 Date: 2023-11-06
 Author: Kyle St John
 */

import engine.graphics.EngineWindow;

/**
 * The engines main class
 */
public class Engine {
    public static void main(String[] args) {
        EngineWindow engine = EngineWindow.get();
        engine.run();
    }
}
/*End of EngineMain class*/
