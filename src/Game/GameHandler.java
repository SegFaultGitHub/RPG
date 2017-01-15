package Game; /**
 * Created by Thomas VENNER on 21/07/2016.
 */

import Config.Config;
import Game.Map.Map;
import Game.Map.Maps;
import Game.Player.Player;
import Textures.Textures;
import Utils.Input;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;
import java.util.Date;

public class GameHandler {
    private static Map map;
    private static Player player;
    private static ArrayList<Long> frames;
    private static ArrayList<Integer> previousFPS;
    private static int currentFPS;

    public static void initialize(int screenWidth, int screenHeight) {
        Textures.initialize();
        Input.initialize();
        player = new Player();
        try {
            Maps.initialize(screenWidth, screenHeight);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        map = Maps.getMaps().get("map1");
        previousFPS = new ArrayList<>();
        frames = new ArrayList<>();
    }

    @Deprecated
    public static Map getMap() {
        return map;
    }

    public static void update(RenderWindow window) {
        Input.update(window);
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

    public static void draw(RenderWindow window, RenderWindow windowDebug) {
        window.clear(Color.WHITE);
        map.drawFirstLayer(window);
        //player.drawFirstLayer(window);
        map.drawRemainingLayers(window);
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
}
