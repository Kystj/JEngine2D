/*
 Title: BugTypeAdapter
 Date: 2023-11-09
 Author: Kyle St John
 */
package engine.typeadapters;

import com.google.gson.*;
import engine.debug.BugReport;

import java.lang.reflect.Type;

public class BugTypeAdapter implements JsonSerializer<BugReport>, JsonDeserializer<BugReport> {

    @Override
    public JsonElement serialize(BugReport bugReport, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Name: ", new JsonPrimitive(String.valueOf(bugReport.getBugID())));
        jsonObject.add("Description: ", new JsonPrimitive(String.valueOf(bugReport.getBugDescription())));
        jsonObject.add("Resolved: ", new JsonPrimitive(bugReport.isResolved()));
        return jsonObject;
    }

    @Override
    public BugReport deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("Name: ").getAsString();
        String description = jsonObject.get("Description: ").getAsString();
        Boolean resolved = jsonObject.get("Resolved: ").getAsBoolean();

        return new BugReport(name, description, resolved);
    }
}
/*End of BugTypeAdapter class*/
