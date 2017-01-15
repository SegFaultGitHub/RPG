package Game.Map;

import Events.Event;
import Textures.Tileset;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

import java.util.ArrayList;

/**
 * Created by Thomas VENNER on 17/07/2016.
 */
public class Map {
    public static int SIZE = 32;

    private String name;
    private Tileset tileset;
    private Cell[][] cells;
    private int width, height;
    private ArrayList<Event> events;

    public String getName() {
        return name;
    }

    public Tileset getTileset() {
        return tileset;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Deprecated
    public ArrayList<Event> getEvents() {
        return events;
    }

    public Map(String name, Tileset tileset, Cell[][] cells, int width, int height, ArrayList<Event> events) {
        this.name = name;
        this.tileset = tileset;
        this.cells = cells;
        this.width = width;
        this.height = height;
        this.events = events;
    }

    public Vector2f getPixelDimensions() {
        return new Vector2f(Map.SIZE * width, Map.SIZE * height);
    }

    public Cell getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return null;
        return cells[x][y];
    }

    public void drawFirstLayer(RenderTarget target) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j].draw(target, tileset.getTexture(),i * SIZE, j * SIZE, 0);
            }
        }
    }

    public void drawRemainingLayers(RenderTarget target) {
        for (int k = 1; k < 10; k++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cells[i][j].draw(target, tileset.getTexture(), i * SIZE, j * SIZE, k);
                }
            }
        }
    }

    public void drawAllLayers(RenderTarget target, Vector2f offset) {
        for (int k = 0; k < 10; k++) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cells[i][j].draw(target, tileset.getTexture(), i * SIZE + (int) offset.x, j * SIZE + (int) offset.y, k);
                }
            }
        }
    }
}
