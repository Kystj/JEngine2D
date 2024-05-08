/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.world.levels;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.OrthoCamera;
import engine.graphics.Renderer;
import engine.physics.PhysicsMain;
import engine.utils.EConstants;
import engine.world.objects.GameObject;

public class TestLevel extends Level implements EventListener {

    public TestLevel(Renderer renderer, PhysicsMain physics) {
        super(renderer, physics);
    }

    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (event.getEventType() == EConstants.EventType.Active_Object) {
            activeGameObject = gameObject;
        }
    }

    @Override
    public void onEvent(Event event, Level level) {

    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void init() {
        super.init();
        EventDispatcher.addListener(EConstants.EventType.User, this);
        EventDispatcher.addListener(EConstants.EventType.New_Asset, this);
        EventDispatcher.addListener(EConstants.EventType.Active_Object, this);

        loadResources();
        this.orthoCamera = new OrthoCamera();
    }


    @Override
    public void tick(float deltaTime) {
        super.tick(deltaTime);
    }


    @Override
    public void render() {
        super.render();

    }


    private void loadResources() {

    }


    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjects) {
            this.addGameObject(gameObject);
        }
    }
}
/*End of Editor class*/