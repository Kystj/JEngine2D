/*
 Title: Scene
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.scene;

import engine.graphics.OrthographicCamera;
import engine.graphics.Renderer;
import engine.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    protected Renderer renderer = new Renderer();
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected OrthographicCamera orthoCamera;
    private boolean bIsSceneActive;

    public void init() {
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
            this.renderer.addSprite(gameObject);
        }
        bIsSceneActive = true;
    }

    public void tick(float deltaTime) {
        updateGameObjects(deltaTime);
    }

    public void addGameObject(GameObject gameObject) {
        if (!bIsSceneActive) {
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.init();
            this.renderer.addSprite(gameObject);
        }
    }

    private void updateGameObjects(float deltaTime) {
        for (GameObject go : this.gameObjects) {
            go.update(deltaTime);
        }
    }

    public void imgui() {

    }

    public void render() {
        renderer.render();
    }

    public void cleanup() {

    }

    public OrthographicCamera getOrthoCamera() {
        return orthoCamera;
    }
}
/*End of Scene class*/
