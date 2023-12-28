/*
 Title: SceneManager
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.managers;

import engine.scene.Scene;

public class SceneManager {

    private static Scene currentScene;

    public static void changeScene() {
        currentScene = new Scene();
        currentScene.init();
    }

    public static void renderScene() {
        currentScene.render();
    }

    public static void setCurrentScene(Scene scene) {
        if (currentScene != null) {
            currentScene.cleanup();
        }
        currentScene = scene;
    }
}
/*End of SceneManager class*/
