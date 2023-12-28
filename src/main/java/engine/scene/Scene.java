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

    public void init() {
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
        }
    }

    public void tick() {

    }

    public void addGameObject(GameObject gameObject) {
        gameObjects.add(gameObject);
        gameObject.init();
        this.renderer.addSprite(gameObject);
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
