package Editor.Map;

import Game.Map.Maps;
import Textures.Tileset;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.system.Vector2f;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by Thomas VENNER on 14/12/2016.
 */
public class EditorMap {
    public static int SIZE = 32;
    public static float SCALE = 1f;

    public static int getCellSize() {
        return (int)(SIZE * SCALE);
    }

    private String name;
    private Tileset tileset;
    private EditorCell[][] editorCells;
    private int width, height;

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

    public EditorMap(Tileset tileset, String name, int width, int height) {
        this.name = name;
        this.tileset = tileset;
        this.editorCells = new EditorCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Integer[] content = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
                editorCells[i][j] = new EditorCell(content, true);
            }
        }
        this.width = width;
        this.height = height;
    }

    public EditorMap(String mapName) {
        Game.Map.Map map = Maps.get(mapName);
        this.name = map.getName();
        this.tileset = map.getTileset();
        this.width = map.getWidth();
        this.height = map.getHeight();
        this.editorCells = new EditorCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Integer[] content = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
                Game.Map.Cell cell = map.getCell(i, j);
                for (int k = 0; k < cell.getContent().size(); k++) {
                    content[k] = cell.getContent().get(k);
                }
                editorCells[i][j] = new EditorCell(content, cell.getWalkable());
            }
        }
    }

    public EditorCell getCell(int i, int j) {
        if (i < 0 || i >= width || j < 0 || j >= height)
            return null;
        return editorCells[i][j];
    }

    public void setCellContent(int i, int j, int content, int layer) {
        if (i < width && j < height) {
            editorCells[i][j].setContent(content, layer);
        }
    }

//    public void drawFirstLayer(RenderWindow window) {
//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//                cells[i][j].draw(window, tileset.getTexture(),i * SIZE, j * SIZE, 0, (i + j) % 2 == 0);
//            }
//        }
//    }
//
//    public void drawRemainingLayers(RenderWindow window) {
//        for (int k = 1; k < 10; k++) {
//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//                    cells[i][j].draw(window, tileset.getTexture(), i * SIZE, j * SIZE, k, (i + j) % 2 == 0);
//                }
//            }
//        }
//    }

    public void drawAllLayers(RenderTarget target, Vector2f offset, int minX, int minY, int maxX, int maxY, int layer) {
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                for (int k = 0; k < 10; k++) {
                    editorCells[i][j].draw(target, tileset.getTexture(), (int)(i * SIZE * SCALE) + (int) offset.x, (int)(j * SIZE * SCALE) + (int) offset.y, k, (i + j) % 2 == 0, k - layer);
                }
            }
        }
    }

    public void drawAllLayers(RenderTarget target, Vector2f offset, int minX, int minY, int maxX, int maxY) {
        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                for (int k = 0; k < 10; k++) {
                    editorCells[i][j].draw(target, tileset.getTexture(), (int)(i * SIZE * SCALE) + (int) offset.x, (int)(j * SIZE * SCALE) + (int) offset.y, k, (i + j) % 2 == 0);
                }
                if (!editorCells[i][j].isWalkable()) {
                    RectangleShape rectangleShape = new RectangleShape();
                    rectangleShape.setSize(new Vector2f(SIZE * SCALE, SIZE * SCALE));
                    rectangleShape.setPosition((int)(i * SIZE * SCALE) + (int) offset.x, (int)(j * SIZE * SCALE) + (int) offset.y);
                    rectangleShape.setFillColor(new Color(255, 0, 0, 64));
                    target.draw(rectangleShape);
                }
            }
        }
    }

    public JSONObject toJSON(String name) {
        JSONObject result = new JSONObject();

        result.put("name", name);
        result.put("tileset", tileset.getName());
        result.put("width", width);
        result.put("height", height);
        JSONArray contentJSON = new JSONArray();
        for (int j = 0; j < height; j++) {
            String line = "";
            for (int i = 0; i < width; i++) {
                if (i != 0) {
                    line += " ";
                }
                line += editorCells[i][j].getContentString();
            }
            contentJSON.add(j, line);
        }
        result.put("content", contentJSON);
        JSONArray walkJSON = new JSONArray();
        for (int j = 0; j < height; j++) {
            String line = "";
            for (int i = 0; i < width; i++) {
                if (i != 0) {
                    line += " ";
                }
                line += editorCells[i][j].isWalkable() ? "1" : "0";
            }
            walkJSON.add(j, line);
        }
        result.put("walk", walkJSON);
        return result;
    }

    public void setDimension(int width, int height) {
        EditorCell[][] cells = new EditorCell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Integer[] content = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
                boolean walk = true;
                if (i < this.width && j < this.height) {
                    for (int k = 0; k < 10; k++) {
                        content[k] = this.getCell(i, j).getContent()[k];
                    }
                    walk = this.getCell(i, j).isWalkable();
                }
                cells[i][j] = new EditorCell(content, walk);
            }
        }
        this.editorCells = cells.clone();
        this.width = width;
        this.height = height;
    }
}
