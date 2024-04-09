/*
 Title: ComponentAdapter
 Date: 2024-04-08
 Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.*;
import engine.world.components.Component;

import java.lang.reflect.Type;

public class ComponentAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("Class").getAsString();
        JsonElement element = jsonObject.get("Class properties");

        try {
            return context.deserialize(element, Class.forName(type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown element. Cannot deserialize: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("Class", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("Class properties", context.serialize(src, src.getClass()));
        return result;
    }
}
/*End of ComponentAdapter class*/
