package Editor;

import Editor.Map.EditorMap;
import Game.Map.Maps;
import Game.Map.Tilesets;
import Textures.Textures;
import Utils.Input;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Thomas VENNER on 11/12/2016.
 */
public class MapEditorHandler {
    private enum MapEditorState {
        TitleScreen, Editor
    }

    private static MapEditorState state;
    private static ArrayList<Long> frames;
    private static ArrayList<Integer> previousFPS;
    private static int currentFPS;

    private static Vector2f screenSize;

    public static Vector2f getScreenSize() {
        return screenSize;
    }

    public static void initialize(int screenWidth, int screenHeight) throws IOException {
        screenSize = new Vector2f(screenWidth, screenHeight);
        Textures.initialize();
        Tilesets.initialize();
        state = MapEditorState.TitleScreen;
        try {
            Maps.initialize(screenWidth, screenHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        MapEditorTitleScreen.initialize();
        MapEditorLayout.initialize();
        previousFPS = new ArrayList<>();
        frames = new ArrayList<>();
    }

    public static void createNewMap(String tilesetName, String mapName, int width, int height) {
        MapEditorLayout.setEditorMap(new EditorMap(Tilesets.get(tilesetName), mapName, width, height));
        state = MapEditorState.Editor;
    }

    public static void editMap(String mapName) {
        MapEditorLayout.setEditorMap(new EditorMap(mapName));
        state = MapEditorState.Editor;
    }

    public static void update(RenderWindow window) {
        Input.update(window);
        switch (state) {
            case TitleScreen:
                updateTitleScreen();
                break;
            case Editor:
                updateEditor();
                break;
            default:
                break;
        }
        //<editor-fold desc="Debug">
        if (Config.DEBUG) {
            long now = new Date().getTime();
            frames.add(now);
            while (now - frames.get(0) > 1000) {
                frames.remove(0);
            }
            currentFPS = frames.size();
            previousFPS.add(currentFPS);
            if (previousFPS.size() > 512) {
                previousFPS.remove(0);
            }
        }
        //</editor-fold>
    }

    private static void updateTitleScreen() {
        MapEditorTitleScreen.update();
    }

    private static void updateEditor() {
        MapEditorLayout.update();
    }

    public static void draw(RenderWindow window, RenderWindow windowDebug) {
        switch (state) {
            case TitleScreen:
                drawTitleScreen(window);
                break;
            case Editor:
                drawEditor(window);
                break;
            default:
                break;
        }
        window.display();

        //<editor-fold desc="Debug">
        if (Config.DEBUG && windowDebug != null) {
            RectangleShape rectangleShape = new RectangleShape();
            windowDebug.clear(Color.WHITE);
            Text text = new Text();
            text.setCharacterSize(18);
            text.setFont(Textures.getFontDebug());
            text.setColor(Color.BLACK);
            String debugString = "FPS: " + String.valueOf(currentFPS) + "\n";
            text.setString(debugString);
            windowDebug.draw(text);

            for (int i = 0; i < previousFPS.size(); i++) {
                rectangleShape.setSize(new Vector2f(1, 1));
                rectangleShape.setFillColor(Color.BLACK);
                rectangleShape.setPosition(i, 256 - previousFPS.get(i));
                windowDebug.draw(rectangleShape);
            }
            windowDebug.display();
        }
        //</editor-fold>
    }

    private static void drawTitleScreen(RenderWindow window) {
        window.clear(Color.BLACK);
        MapEditorTitleScreen.draw(window);
    }

    private static void drawEditor(RenderWindow window) {
        window.clear(Color.WHITE);
        MapEditorLayout.draw(window);
    }
}