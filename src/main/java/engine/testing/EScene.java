/*
 Title: Editor
 Date: 2023-12-28
 Author: Kyle St John
 */
package engine.testing;

import engine.eventsystem.Event;
import engine.eventsystem.EventDispatcher;
import engine.eventsystem.EventListener;
import engine.graphics.OrthoCamera;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;
import engine.utils.EConstants;
import engine.utils.ResourceUtils;
import engine.world.objects.GameObject;
import engine.world.scenes.Scene;

public class EScene extends Scene implements EventListener {

    @Override
    public void onEvent(Event event, GameObject gameObject) {
        if (event.getEventType() == EConstants.EventType.Active_Object) {
            activeGameObject = gameObject;
        }
    }

    @Override
    public void onEvent(Event event, Scene scene) {

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
        addGameObjToEditor();

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
        Texture gizmoSpriteSheetTexture = new Texture("assets/spritesheets/gizmos.png");
        ResourceUtils.addSpriteSheet("assets/spritesheets/gizmos.png",
                new SpriteSheet(gizmoSpriteSheetTexture, 32,32,0, "Gizmos"));
    }


    private void addGameObjToEditor() {
        for (GameObject gameObject : gameObjects) {
            this.addGameObject(gameObject);
        }
    }
}
/*End of Editor class*/