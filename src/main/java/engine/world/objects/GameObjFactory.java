/*
 Title: GameObjectFactory
 Date: 2024-02-28
 Author: Kyle St John
 */
package engine.world.objects;

import engine.world.components.Sprite;
import engine.world.components.Transform;
import org.joml.Vector2f;

public class GameObjFactory {

    public static GameObject generateGameObject(Sprite sprite, float width, float height) {
        GameObject object = new GameObject();
        object.setTransform( new Transform(new Vector2f(), new Vector2f(width, height)));
        object.addComponent(sprite);
        return object;
    }

    public static GameObject generateGameObject(String name, Sprite sprite, Transform transform) {
        GameObject object = new GameObject();
        object.setName(object.getName() + name);
        object.setTransform(transform);
        object.addComponent(sprite);
        return object;
    }
}
/*End of GameObjectFactory class*/
