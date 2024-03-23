/*
 * Title: BugTypeAdapter
 * Date: 2023-11-09
 * Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.*;
import engine.debug.info.BugReport;

import java.lang.reflect.Type;

/** BugAdapter class for custom Gson serialization and deserialization of BugReport objects. */
public class ReportAdapter implements JsonSerializer<BugReport>, JsonDeserializer<BugReport> {

    /**
     * Serializes a BugReport object to a JSON representation.
     *
     * @param bugReport                  The BugReport object to be serialized.
     * @param type                       The type of the object.
     * @param jsonSerializationContext  The serialization context.
     * @return                           The serialized JSON element.
     */
    @Override
    public JsonElement serialize(BugReport bugReport, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        // Adding BugReport properties to the JSON object
        jsonObject.add("Name: ", new JsonPrimitive(String.valueOf(bugReport.getBugID())));
        jsonObject.add("Description: ", new JsonPrimitive(String.valueOf(bugReport.getBugDescription())));
        jsonObject.add("Resolved: ", new JsonPrimitive(bugReport.isResolved()));
        return jsonObject;
    }

    /**
     * Deserializes a JSON element to a BugReport object.
     *
     * @param json          The JSON element to be deserialized.
     * @param typeOfT       The type of the object.
     * @param context       The deserialization context.
     * @return              The deserialized BugReport object.
     * @throws JsonParseException If an error occurs during deserialization.
     */
    @Override
    public BugReport deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        // Retrieving BugReport properties from the JSON object
        String name = jsonObject.get("Name: ").getAsString();
        String description = jsonObject.get("Description: ").getAsString();
        Boolean resolved = jsonObject.get("Resolved: ").getAsBoolean();

        // Creating a new BugReport object with the retrieved properties
        return new BugReport(name, description, resolved);
    }
}
/* End of BugTypeAdapter class */