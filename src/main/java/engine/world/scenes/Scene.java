/*
 Title: Scene
 Date: 2023-11-06
 Author: Kyle St John
 */
package engine.world.scenes;

import engine.graphics.OrthographicCamera;
import engine.graphics.Renderer;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.List;

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
    protected OrthographicCamera orthoCamera;

    // Flag indicating whether the scene is active
    private boolean bIsSceneActive;

    /**
     * Initializes the scene by initializing game objects and the renderer.
     */
    public void init() {
        // Initialize each game object and add it to the renderer
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
            this.renderer.addSprite(gameObject);
        }
        // Set the scene as active
        bIsSceneActive = true;
    }

    /**
     * Updates the game objects in the scene based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    public void tick(float deltaTime) {
        updateGameObjects(deltaTime);
    }

    /**
     * Adds a game object to the scene.
     *
     * @param gameObject The game object to be added.
     */
    public void addGameObject(GameObject gameObject) {
        // Check if the scene is active
        if (!bIsSceneActive) {
            // If not active, simply add the game object to the list
            gameObjects.add(gameObject);
        } else {
            // If active, add the game object, initialize it, and add it to the renderer
            gameObjects.add(gameObject);
            gameObject.init();
            this.renderer.addSprite(gameObject);
        }
    }

    /**
     * Updates all game objects in the scene based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    private void updateGameObjects(float deltaTime) {
        for (GameObject go : this.gameObjects) {
            // Update each game object in the scene
            go.update(deltaTime);
        }
    }

    /**
     * Placeholder method for ImGui integration for scene editing.
     */
    public void imgui() {
        // TODO: Implement ImGui integration for scene editing
    }

    /**
     * Renders the scene using the renderer.
     */
    public void render() {
        // Render the scene using the renderer
        renderer.render();
    }

    /**
     * Placeholder method for scene cleanup.
     */
    public void cleanup() {
        // TODO: Implement scene cleanup
    }

    /**
     * Retrieves the orthographic camera used in the scene.
     *
     * @return The orthographic camera.
     */
    public OrthographicCamera getOrthoCamera() {
        return orthoCamera;
    }
}
/*End of Scene class*/

