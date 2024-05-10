package engine.world.levels;

import engine.graphics.OrthoCamera;
import engine.graphics.Renderer;
import engine.physics.PhysicsMain;
import engine.physics.components.RigidBody;
import engine.world.objects.GameObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class Level {

    protected Renderer renderer;
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected OrthoCamera orthoCamera;
    private boolean bIsSceneActive;
    protected GameObject activeGameObject = new GameObject();
    protected String levelName = "Default_Level";

    private PhysicsMain physics;

    // Constructor to inject dependencies
    public Level(Renderer renderer, PhysicsMain physics) {
        this.renderer = renderer;
        this.physics = physics;
    }

    public void init() {
        physics.init();
        // Initialize each game object and add it to the renderer
        for (GameObject gameObject : gameObjects) {
            gameObject.init();
            renderer.addGameObject(gameObject);
            if (gameObject.getComponent(RigidBody.class) != null) physics.add(gameObject);
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
            this.renderer.addGameObject(gameObject);
            gameObject.init();
        }
    }

    public void removeGameObject(int gameObjectUID) {
        // Use an Iterator for safe removal while iterating
        Iterator<GameObject> iterator = gameObjects.iterator();

        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject.getUID() == gameObjectUID) {
                // Remove the sprite from the renderer
                renderer.removeSpriteFromRenderer(gameObjectUID);

                // If the GameObject has a RigidBody component, destroy its physics body
                if (gameObject.getComponent(RigidBody.class) != null)  physics.destroyPhysicsBody(gameObject);

                // Remove the GameObject from the list
                iterator.remove();
                return; // Exit the method after removal
            }
        }
    }

    private void updateGameObjects(float deltaTime) {
        for (GameObject go : this.gameObjects) {
            go.update(deltaTime);
        }
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

    public GameObject getGameObject(int gameObjectId) {
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUID() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public String getName() {
        return levelName;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public PhysicsMain getPhysics() {
        return physics;
    }
}
