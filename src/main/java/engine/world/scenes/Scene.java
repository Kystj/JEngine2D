/*
 Title: Scene
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.world.scenes;

import engine.graphics.OrthoCamera;
import engine.graphics.Renderer;
import engine.world.components.Transform;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The Scene class represents a scene in the game, containing game objects and managing rendering.
 *
 * Responsibilities:
 * - Initialization of game objects and renderer
 * - Updating game objects in each frame
 * - Adding game objects to the scene
 * - Rendering the scene
 * - Handling cleanup
 */
public class Scene {

    // Renderer for rendering sprites in the scene
    protected Renderer renderer = new Renderer();

    // List to store game objects in the scene
    protected List<GameObject> gameObjects = new ArrayList<>();

    // Orthographic camera for the scene
    protected OrthoCamera orthoCamera;

    // Flag indicating whether the scene is active
    private boolean bIsSceneActive;

    protected GameObject activeGameObject = new GameObject("Active", new Transform(), 1);


    public void init() {
        // Initialize each game object and add it to the renderer
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
            this.renderer.addGameObject(gameObject);
        }
        // Set the scene as active
        bIsSceneActive = true;
    }


    public void tick(float deltaTime) {
        updateGameObjects(deltaTime);
    }


    public void addGameObject(GameObject gameObject) {
        // Check if the scene is active
        if (!bIsSceneActive) {
            // If not active, simply add the game object to the list
            gameObjects.add(gameObject);
        } else {
            // If active, add the game object, initialize it, and add it to the renderer
            gameObjects.add(gameObject);
            gameObject.init();
            this.renderer.addGameObject(gameObject);
        }
    }


    public void removeGameObject(int gameObjectUID) {
        // Iterate through the gameObjects list
        for (int i = 0; i < gameObjects.size(); i++) {
            GameObject gameObject = gameObjects.get(i);
            // If the UID of the current gameObject matches the given UID, remove it from the scene
            if (gameObject.getUID() == gameObjectUID) {
                renderer.removeSpriteFromRenderer(gameObjectUID);
                gameObjects.remove(i);
                break;
            }
        }
    }


    private void updateGameObjects(float deltaTime) {
        for (GameObject go : this.gameObjects) {
            go.update(deltaTime);
        }
    }


    public void imgui() {
        // TODO: Implement ImGui integration for scene editing
    }



    public void render() {
        // Render the scene using the renderer
        renderer.render();
    }


    public void cleanup() {
        // TODO: Implement scene cleanup
    }


    public OrthoCamera getOrthoCamera() {
        return orthoCamera;
    }


    public GameObject getActiveGameObject() {
        return activeGameObject;
    }


    public List<GameObject> getGameObjects() {
        return gameObjects;
    }


    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUID() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }
}
/*End of Scene class*/

