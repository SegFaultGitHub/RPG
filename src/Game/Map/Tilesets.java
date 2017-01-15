package Game.Map;

import Textures.Textures;
import Textures.Tileset;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Thomas VENNER on 13/12/2016.
 */
public class Tilesets {
    private static HashMap<String, Tileset> tilesets;

    public static HashMap<String, Tileset> getTilesets() {
        return tilesets;
    }

    public static Tileset get(String name) {
        return tilesets.get(name);
    }

    public static void initialize() {
        tilesets = new HashMap<>();
        File tilesetsFolder = new File("graphics/tilesets");
        for (String fileName : tilesetsFolder.list()) {
            if (fileName.endsWith(".png")) {
                String key = fileName.substring(0, fileName.length() - 4);
                tilesets.put(key, new Tileset(key, Textures.getTileset(fileName)));
            }
        }
    }
}
