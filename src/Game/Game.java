package Game; /**
 * Created by Thomas VENNER on 17/07/2016.
 */
import Config.Config;
import Textures.Textures;
import Utils.Input;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;

import java.io.IOException;

public class Game {
    private static RenderWindow window;
    private static RenderWindow windowDebug;

    private static void initialize() throws IOException {
        Config.initialize();
        windowDebug = null;
        if (Config.DEBUG) {
            windowDebug = new RenderWindow();
            windowDebug.create(new VideoMode(512, 256, 32), "Light debug", WindowStyle.RESIZE);
            windowDebug.setFramerateLimit(60);
        }
        window = new RenderWindow();
        window.create(new VideoMode(780, 480, 32), "RPG", WindowStyle.RESIZE);
        window.setFramerateLimit(60);
//        OldTilesets.initialize();
        GameHandler.initialize(window.getSize().x, window.getSize().y);
    }

    private static void update() {
        GameHandler.update(window);
    }

    private static void draw() {
        GameHandler.draw(window, windowDebug);
    }

    public static void main(String[] args) throws IOException {
        initialize();

        while(window.isOpen()) {
            update();
            draw();
            window.display();
            window.pollEvent();
        }
    }
}
