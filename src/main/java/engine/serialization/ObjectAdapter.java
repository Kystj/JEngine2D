/* Title: ObjectAdapter
 Date: 2024-04-08
 Author: Kyle St John*/


package engine.serialization;

import com.google.gson.*;
import engine.world.components.Component;
import engine.world.components.Sprite;
import engine.world.objects.GameObject;

import java.lang.reflect.Type;

public class ObjectAdapter implements JsonDeserializer<GameObject>  {

        @Override
        public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            JsonArray components = jsonObject.getAsJsonArray("componentsList");

            GameObject gameObject = new GameObject();
            for (JsonElement e : components) {
                Component c = context.deserialize(e, Component.class);
                gameObject.addComponent(c);
            }
            gameObject.setName(name);
            gameObject.setTransform(gameObject.getComponent(Sprite.class).getTransform());
            return gameObject;
        }
    }
// End of ObjectAdapter class

