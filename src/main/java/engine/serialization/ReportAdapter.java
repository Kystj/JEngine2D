/*
 * Title: BugTypeAdapter
 * Date: 2023-11-09
 * Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.*;
import engine.debugging.info.ErrorForm;

import java.lang.reflect.Type;


public class ReportAdapter implements JsonSerializer<ErrorForm>, JsonDeserializer<ErrorForm> {


    @Override
    public JsonElement serialize(ErrorForm errorForm, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        // Adding BugReport properties to the JSON object
        jsonObject.add("Name: ", new JsonPrimitive(String.valueOf(errorForm.getErrorID())));
        jsonObject.add("Description: ", new JsonPrimitive(String.valueOf(errorForm.getErrorDescription())));
        return jsonObject;
    }


    @Override
    public ErrorForm deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Retrieving BugReport properties from the JSON object
        String name = jsonObject.get("Name: ").getAsString();
        String description = jsonObject.get("Description: ").getAsString();
        Boolean resolved = jsonObject.get("Resolved: ").getAsBoolean();

        // Creating a new BugReport object with the retrieved properties
        return new ErrorForm(name, description);
    }
}
/* End of BugTypeAdapter class */