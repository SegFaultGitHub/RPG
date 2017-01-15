package Editor;

import Config.Config;
import Textures.Textures;
import Utils.Input;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;

import java.io.IOException;

/**
 * Created by Thomas VENNER on 26/07/2016.
 */
public class MapEditor {
    private static RenderWindow window, windowDebug;

    private static void initialize() throws IOException {
        Config.initialize();
        windowDebug = null;
        if (Config.DEBUG) {
            windowDebug = new RenderWindow();
            windowDebug.create(new VideoMode(512, 256, 32), "RPG - Map Editor (debug)", WindowStyle.RESIZE);
            windowDebug.setFramerateLimit(60);
        }
        window = new RenderWindow();
        window.create(new VideoMode(1369, 800, 32), "RPG - Map Editor", WindowStyle.RESIZE);
        window.setFramerateLimit(60);
        MapEditorHandler.initialize(window.getSize().x, window.getSize().y);
    }

    private static void update() {
        MapEditorHandler.update(window);
    }

    private static void draw() {
        MapEditorHandler.draw(window, windowDebug);
    }

    public static void main(String[] args) throws IOException {
        initialize();

        while(window.isOpen()) {
            update();
            draw();
            window.pollEvent();
        }
    }
}
