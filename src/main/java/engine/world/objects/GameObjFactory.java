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
        GameObject object = new GameObject("Game_Object_",
                            new Transform(new Vector2f(),
                            new Vector2f(width, height)));
        object.addComponent(sprite);
        object.setName(object.getName() + object.objectUID);
        return object;
    }

    public static GameObject generateGameObject(String name, Sprite sprite, Transform transform) {
        GameObject object = new GameObject("Game_Object_" + name,
                            transform);
        object.setName(object.getName() + object.objectUID);
        object.addComponent(sprite);
        return object;
    }
}
/*End of GameObjectFactory class*/
