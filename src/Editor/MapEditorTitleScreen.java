package Editor;

import Game.Map.Map;
import Game.Map.Maps;
import Game.Map.Tilesets;
import Textures.Textures;
import Utils.Input;
import Utils.Utils;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * Created by Thomas VENNER on 13/12/2016.
 */
public class MapEditorTitleScreen {
    private static int index1;
    private static int index2;

    private static int width = 10;
    private static int height = 10;

    private static int tilesetIndex = 0;
    private static String tilesetName;
    private static Sprite tilesetSprite;
    private static int mapIndex = 0;
    private static String mapName;
    private static Sprite mapSprite;
    private static int currentHeight;
    private static int maxHeight;
    private static int direction;

    private static Item[][] scenes;
    private static String[] titles;

    //<editor-fold desc="Test">
    //</editor-fold>

    public static void initialize() {
        scenes = new Item[100][];
        titles = new String[100];
        index1 = 0;
        index2 = 0;
        Item[] scene;
        scene = new Item[3];
        titles[0] = "Menu";
        scene[0] = new Item("Nouvelle map", MapEditorTitleScreen::newMap);
        scene[1] = new Item("Éditer une map", MapEditorTitleScreen::updateMap);
        scene[2] = new Item("Quitter", MapEditorTitleScreen::quit);
        scenes[0] = scene;

        scene = new Item[5];
        titles[1] = "Nouvelle map";
        scene[0] = new Item("Largeur : " + width, MapEditorTitleScreen::width);
        scene[1] = new Item("Hauteur : " + height, MapEditorTitleScreen::height);
        scene[2] = new Item("Tileset : ", MapEditorTitleScreen::chooseTileset);
        scene[3] = new Item("Créer", MapEditorTitleScreen::createMap);
        scene[4] = new Item("Retour", MapEditorTitleScreen::back);
        scenes[1] = scene;

        scene = new Item[3];
        titles[2] = "Éditer une map";
        scene[0] = new Item("Map : ", MapEditorTitleScreen::chooseMap);
        scene[1] = new Item("Éditer", MapEditorTitleScreen::editMap);
        scene[2] = new Item("Retour", MapEditorTitleScreen::back);
        scenes[2] = scene;
    }

    private static void setTileset(String key) {
        tilesetName = key;
        currentHeight = 0;
        direction = 4;
        Texture texture = Tilesets.get(key).getTexture();
        tilesetSprite = new Sprite();
        tilesetSprite.setTexture(texture);
        tilesetSprite.setOrigin(texture.getSize().x, 0);
        tilesetSprite.setPosition(MapEditorHandler.getScreenSize().x - 20, 20);
        maxHeight = texture.getSize().y - 40 - (int) MapEditorHandler.getScreenSize().y;
    }

    private static void setMap(String key) {
        mapName = key;
        RenderTexture texture = new RenderTexture();
        Map toDraw = Maps.get(mapName);
        Vector2f mapSize = toDraw.getPixelDimensions();
        try {
//            texture.create((int) mapSize.x + 2, (int) mapSize.y + 2);
            texture.create((int) mapSize.x, (int) mapSize.y);
        } catch (TextureCreationException e) {
            e.printStackTrace();
        }
        Vector2f screen = MapEditorHandler.getScreenSize();
        int offset = 50;
        int maxWidth = (int) screen.x - 400 - offset * 2;
        int maxHeight = (int) screen.y - offset * 2;
        float ratio = 1f;

        if (mapSize.x > maxWidth || mapSize.y > maxHeight) {
            float xRatio = maxWidth / mapSize.x;
            float yRatio = maxHeight / mapSize.y;
            ratio = xRatio > yRatio ? yRatio : xRatio;
        }

//        texture.clear(Color.WHITE);
        texture.clear();
//        Maps.get(mapName).drawAllLayers(texture, new Vector2f(1, 1));
        Maps.get(mapName).drawAllLayers(texture, new Vector2f(0, 0));
        texture.display();
        if (ratio > 1) {
            texture.setSmooth(true);
        }
        mapSprite = new Sprite();
        mapSprite.setTexture(texture.getTexture());
        mapSprite.setPosition(450 + (maxWidth - mapSize.x * ratio) / 2, 50 + (maxHeight - mapSize.y * ratio) / 2);
        mapSprite.setScale(ratio, ratio);
    }

    public static void update() {
        if (Input.isKeyPressedOnce(Keyboard.Key.DOWN)) {
            index2++;
        } else if (Input.isKeyPressedOnce(Keyboard.Key.UP)) {
            index2--;
        }
        if (index2 < 0) {
            index2 += scenes[index1].length;
        }
        index2 %= scenes[index1].length;
        scenes[index1][index2].runMethod();

        if (index1 == 1) {
            currentHeight += direction;
            tilesetSprite.setOrigin(tilesetSprite.getOrigin().x, currentHeight);
            if (currentHeight > maxHeight || currentHeight < 0) {
                direction *= -1;
            }
        }
    }

    private static void newMap() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        index2 = 0;
        index1 = 1;
        String key = Tilesets.getTilesets().keySet().toArray()[tilesetIndex].toString();
        setTileset(key);
        scenes[1][2].setText("Tileset : " + Tilesets.getTilesets().get(key).getName());
        chooseTileset();
    }

    private static void updateMap() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        index2 = 0;
        index1 = 2;
        String key = Maps.getMaps().keySet().toArray()[mapIndex].toString();
        setMap(key);
        scenes[2][0].setText("Map : " + Maps.getMaps().get(key).getName());
        chooseMap();
    }

    private static void quit() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        System.exit(0);
    }

    private static void width() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            if (width > 10) {
                width--;
            }
            scenes[index1][index2].setText("Largeur : " + width);
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            if (width < 100) {
                width++;
            }
            scenes[index1][index2].setText("Largeur : " + width);
        }
    }

    private static void height() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            if (height > 10) {
                height--;
            }
            scenes[index1][index2].setText("Hauteur : " + height);
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            if (height < 100) {
                height++;
            }
            scenes[index1][index2].setText("Hauteur : " + height);
        }
    }

    private static void chooseTileset() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            tilesetIndex--;
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            tilesetIndex++;
        } else {
            return;
        }
        if (tilesetIndex < 0) {
            tilesetIndex += Tilesets.getTilesets().size();
        }
        tilesetIndex %= Tilesets.getTilesets().size();
        String key = Tilesets.getTilesets().keySet().toArray()[tilesetIndex].toString();
        scenes[index1][index2].setText("Tileset : " + Tilesets.getTilesets().get(key).getName());
        setTileset(key);
    }

    private static void chooseMap() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            mapIndex--;
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            mapIndex++;
        } else {
            return;
        }
        if (mapIndex < 0) {
            mapIndex += Maps.getMaps().size();
        }
        mapIndex %= Maps.getMaps().size();
        String key = Maps.getMaps().keySet().toArray()[mapIndex].toString();
        scenes[index1][index2].setText("Map : " + Maps.getMaps().get(key).getName());
        setMap(key);
    }

    private static void createMap() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        MapEditorHandler.createNewMap(tilesetName, Utils.getRandomString(10), width, height);
    }

    private static void editMap() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        MapEditorHandler.editMap(mapName);
    }

    private static void back() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        switch (index1) {
            case 1:
            case 2:
                index2 = 0;
                index1 = 0;
                break;
            default:
                index2 = 0;
                index1 = 0;
        }
    }

    public static void draw(RenderWindow window) {
        if (index1 == 1) {
            window.draw(tilesetSprite);
        } else if (index1 == 2) {
            window.draw(mapSprite);
        }

        Text text = new Text();
        text.setCharacterSize(32);
        text.setFont(Textures.getFont());
        text.setColor(new Color(192, 192, 192));
        text.setString(titles[index1]);
        window.draw(text);
        text.setColor(Color.WHITE);
        for (int i = 0; i < scenes[index1].length; i++) {
            String item = scenes[index1][i].getText();
            text.setCharacterSize(28);
            text.setString(item);
            text.setPosition(i == index2 ? 30 : 0, (i + 1) * 30);
            window.draw(text);
        }
    }
}
