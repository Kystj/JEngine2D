/*
 Title: SpriteSheetTypeAdapter
 Date: 2024-01-13
 Author: Kyle St John
 */
package engine.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.graphics.SpriteSheet;
import engine.managers.ResourceManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class SpriteSheetSerializer {

    private static final String assetDirectory = "assets/saved/";

    public static void saveSpriteSheet(SpriteSheet spriteSheet) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(SpriteSheet.class, new SpriteSheetAdapter())
                .create();

        String fileName = spriteSheet.getFilePathOfTexture().replace("spritesheets", "saved");
        fileName = fileName.replace("png","txt");
        System.out.println(fileName);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(gson.toJson(spriteSheet));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSpriteSheets(Map<SpriteSheet, String> spriteSheets) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(assetDirectory))) {
            for (Path filePath : directoryStream) {
                Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(SpriteSheet.class, new SpriteSheetAdapter())
                        .create();
                String data = "";
                try {
                    String path = filePath.toString();
                    data = new String(Files.readAllBytes(Paths.get(path)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SpriteSheet spriteSheet = gson.fromJson(data, SpriteSheet.class);
                ResourceManager.addSpriteSheet(spriteSheet.getFilePathOfTexture(), spriteSheet);
                spriteSheets.put(spriteSheet, spriteSheet.getAssetType());
            }
        } catch (IOException e) {
            System.out.println("No texture file found.");
        }
    }

}
/*End of SpriteSheetTypeAdapter class*/
