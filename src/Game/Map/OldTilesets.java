package Game.Map;

import Utils.Utils;
import org.jsfml.graphics.Texture;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by Thomas VENNER on 25/07/2016.
 */
public class OldTilesets {
    public enum Direction {
        up, left, right, down
    }

    private static HashMap<String, HashMap<Direction, Boolean>[]> directions;
    public static HashMap<String, HashMap<Direction, Boolean>[]> getDirections() {
        return directions;
    }

    public static void setDirections(String tileset, int index, HashMap<Direction, Boolean> directions) {
        OldTilesets.directions.get(tileset)[index] = directions;
    }

    public static void initialize() throws IOException {
        directions = new HashMap<>();
        JSONObject tilesetsJson = Utils.readJSON("datas/tilesets.json");
        File tilesetsFolder = new File("graphics/tilesets");
        for (String fileName : tilesetsFolder.list()) {
            if (fileName.endsWith(".png")) {
                Texture texture = new Texture();
                texture.loadFromFile(Paths.get("graphics/tilesets/" + fileName, new String[0]));
                int width = texture.getSize().x / 32;
                int height = texture.getSize().y / 32;
                int dimension = width * height;
                HashMap<Direction, Boolean>[] dir = new HashMap[dimension];
                if (tilesetsJson.containsKey(fileName)) {
                    JSONObject tilesetJson = (JSONObject) tilesetsJson.get(fileName);
                    for (int i = 0; i < dimension; i++) {
                        dir[i] = new HashMap<>();
                        if (tilesetJson.containsKey(String.valueOf(i))) {
                            JSONObject dirs = (JSONObject) tilesetJson.get(String.valueOf(i));
                            if (dirs.containsKey("up")) {
                                dir[i].put(Direction.up, (boolean) dirs.get("up"));
                            } else {
                                dir[i].put(Direction.up, true);
                            }
                            if (dirs.containsKey("down")) {
                                dir[i].put(Direction.down, (boolean) dirs.get("down"));
                            } else {
                                dir[i].put(Direction.down, true);
                            }
                            if (dirs.containsKey("left")) {
                                dir[i].put(Direction.left, (boolean) dirs.get("left"));
                            } else {
                                dir[i].put(Direction.left, true);
                            }
                            if (dirs.containsKey("right")) {
                                dir[i].put(Direction.right, (boolean) dirs.get("right"));
                            } else {
                                dir[i].put(Direction.right, true);
                            }
                        } else {
                            dir[i].put(Direction.up, true);
                            dir[i].put(Direction.down, true);
                            dir[i].put(Direction.left, true);
                            dir[i].put(Direction.right, true);
                        }
                    }
                } else {
                    for (int i = 0; i < dimension; i++) {
                        dir[i] = new HashMap<>();
                        dir[i].put(Direction.up, true);
                        dir[i].put(Direction.down, true);
                        dir[i].put(Direction.left, true);
                        dir[i].put(Direction.right, true);
                    }
                }
                directions.put(fileName, dir);
            }
        }
    }
}
