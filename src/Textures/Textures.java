package Textures;

import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by Thomas VENNER on 21/07/2016.
 */
public class Textures {
    private static Font font, fontDebug;
    private static HashMap<String, Texture> tilesets;
    private static Texture windowskin;

    public static Font getFont() {
        return font;
    }

    public static Font getFontDebug() {
        return fontDebug;
    }

    public static Texture getWindowskin() {
        return windowskin;
    }

    public static Texture getTileset(String name) {
        return tilesets.get(name);
    }

    public static void initialize() {
        tilesets = new HashMap<>();
        File tilesetsFolder = new File("graphics/tilesets");
        for (String fileName : tilesetsFolder.list()) {
            if (fileName.endsWith(".png")) {
                try {
                    Texture texture = new Texture();
                    texture.loadFromFile(Paths.get("graphics/tilesets/" + fileName, new String[0]));
                    tilesets.put(fileName, texture);
                } catch (Exception e) {
                    System.err.println("Cannot load texture " + fileName + ".");
                }
            }
        }

        font = new Font();
        try {
            font.loadFromFile(Paths.get("graphics/fonts/Calibri.ttf", new String[0]));
        } catch (Exception e) {
            System.err.println("Cannot load font Calibri.");
        }
        fontDebug = new Font();
        try {
            fontDebug.loadFromFile(Paths.get("graphics/fonts/DEC Terminal Modern.ttf", new String[0]));
        } catch (Exception e) {
            System.err.println("Cannot load font DEC Terminal Modern.");
        }

        windowskin = new Texture();
        try {
            windowskin.loadFromFile(Paths.get("graphics/windowskin.png", new String[0]));
        } catch (Exception e) {
            System.err.println("Cannot load texture windowskin.");
        }
    }
}
