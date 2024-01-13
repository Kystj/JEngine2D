/*
 Title: SpriteSheetAdapter
 Date: 2024-01-13
 Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.*;
import engine.graphics.SpriteSheet;

import java.lang.reflect.Type;

public class SpriteSheetAdapter implements JsonSerializer<SpriteSheet> {

    @Override
    public JsonElement serialize(SpriteSheet spriteSheet, Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("SpriteWidth", new JsonPrimitive(spriteSheet.spriteWidth()));
        jsonObject.add("SpriteHeight", new JsonPrimitive(spriteSheet.getSpriteHeight()));
        jsonObject.add("NumOfSprites", new JsonPrimitive(spriteSheet.numOfSprites()));
        jsonObject.add("FilePathOfTexture", new JsonPrimitive(spriteSheet.getFilePathOfTexture()));
        jsonObject.add("SpriteSpacing", new JsonPrimitive(spriteSheet.getSpriteSpacing()));

        return jsonObject;
    }
}
/*End of SpriteSheetAdapter class*/
