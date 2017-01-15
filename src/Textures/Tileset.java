package Textures;

import Editor.Map.EditorMap;
import org.jsfml.graphics.Texture;

/**
 * Created by Thomas VENNER on 13/12/2016.
 */
public class Tileset {
    private String name;
    private Texture texture;
    private int width, height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public Tileset(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
        width = texture.getSize().x / EditorMap.SIZE;
        height = texture.getSize().y / EditorMap.SIZE;
    }
}
