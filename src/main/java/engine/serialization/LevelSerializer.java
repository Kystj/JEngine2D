/*
 Title: LevelSerialization
 Date: 2024-04-08
 Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.debugging.info.Logger;
import engine.world.components.Component;
import engine.world.levels.Level;
import engine.world.objects.GameObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LevelSerializer {

    public static void save(Level level) {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(GameObject.class, new ObjectAdapter())
                .create();

        try {
            FileWriter writer = new FileWriter("levels/level.txt");
            List<GameObject> objToSerialize = new ArrayList<>();
            // Only serialize game objects than want to be serialized
            for (GameObject obj : level.getGameObjects()) {
                if (obj.getUID() != -1) {
                    objToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objToSerialize));
            writer.close();
            Logger.info("Saved level '" + level.getName() + "'", true);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: The level should be initialized by the filepath belonging to the level passed a a parameter.
    //  It is using a hardcoded value for development currently. Change this.
    public static void load(Level level) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentAdapter())
                .registerTypeAdapter(GameObject.class, new ObjectAdapter())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("C:\\Dev\\StellarSprite2D\\JEngine2D\\levels\\level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.equals("")) {

            GameObject[] gameObjects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject gameObject : gameObjects) {
                level.addGameObject(gameObject);
            }
        }
    }
}
/*End of LevelSerialization class*/
