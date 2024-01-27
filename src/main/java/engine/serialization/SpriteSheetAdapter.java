/*
 Title: SpriteSheetAdapter
 Date: 2024-01-13
 Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.*;
import engine.graphics.SpriteSheet;
import engine.graphics.Texture;

import java.lang.reflect.Type;

public class SpriteSheetAdapter implements JsonSerializer<SpriteSheet>, JsonDeserializer<SpriteSheet> {

    @Override
    public JsonElement serialize(SpriteSheet spriteSheet, Type type,
                                 JsonSerializationContext jsonSerializationContext) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("SpriteWidth", new JsonPrimitive(spriteSheet.getSpriteWidth()));
        jsonObject.add("SpriteHeight", new JsonPrimitive(spriteSheet.getSpriteHeight()));
        jsonObject.add("NumOfSprites", new JsonPrimitive(spriteSheet.numOfSprites()));
        jsonObject.add("FilePathOfTexture", new JsonPrimitive(spriteSheet.getFilePathOfTexture()));
        jsonObject.add("SpriteSpacing", new JsonPrimitive(spriteSheet.getSpriteSpacing()));
        jsonObject.add("AssetType", new JsonPrimitive(spriteSheet.getAssetType()));


        return jsonObject;
    }

    @Override
    public SpriteSheet deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        int spriteWidth = jsonObject.get("SpriteWidth").getAsInt();
        int spriteHeight = jsonObject.get("SpriteHeight").getAsInt();
        int numOfSprites = jsonObject.get("NumOfSprites").getAsInt();
        String filePath = jsonObject.get("FilePathOfTexture").getAsString();
        String assetType = jsonObject.get("AssetType").getAsString();
        int spacing = jsonObject.get("SpriteSpacing").getAsInt();

        return new SpriteSheet(new Texture(filePath),
                spriteWidth,
                spriteHeight,
                spacing,
                numOfSprites,
                assetType);
    }
}
/*End of SpriteSheetAdapter class*/
