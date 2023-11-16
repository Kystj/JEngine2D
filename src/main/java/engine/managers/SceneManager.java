/*
 Title: SceneManager
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.managers;

import engine.objects.GameObject;
import engine.scenes.Scene;

public class SceneManager {

    private Scene currentScene;

    public void changeScene(long glfwWindow) {
        currentScene = new Scene(glfwWindow);
        currentScene.init();
    }

    public void renderScene() {
        currentScene.render();
    }

    public void setCurrentScene(Scene scene) {
        if (currentScene != null) {
            currentScene.cleanup();
        }
        currentScene = scene;
    }

    public void addGameObject(GameObject gameObject) {
        //TODO: Implement
    }

    public void removeGameObject(GameObject gameObject) {
        //TODO: Implement
    }

    // Other scene management methods
}
/*End of SceneManager class*/
