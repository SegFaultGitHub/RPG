package Editor;

import Editor.Map.EditorMap;
import Textures.Textures;
import Utils.Input;
import Utils.Utils;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Thomas VENNER on 18/12/2016.
 */
public class MapEditorLayout {
    private enum Tool {
        Pen, Eraser, Move, Walkable, Paint
    }

    //<editor-fold desc="Variables">
    private static int width, height;
    private static Vector2f mapOffset, minMapOffset, maxMapOffset;
    private static EditorMap editorMap;
    private static Tool tool;
    private static int layer;
    private static ArrayList<Button> buttonsLayer;
    private static Button eraserButton, penButton, moveButton, walkableButton, paintButton, resizeButton, zoom1Button, zoom2Button, zoom4Button, zoom8Button;
    private static int editorWidth, editorHeight;
    private static boolean smallWidth, smallHeight;

    //<editor-fold desc="Variables tool move">
    private static Vector2i startDragMove;
    private static Vector2f startOffsetMove;
    private static boolean draggingMove;
    //</editor-fold>

    //<editor-fold desc="Variable tool pen">
    private static Vector2f startSelectPen;
    private static ArrayList<int[]> contentPen;
    private static boolean selectingPen;
    private static Vector2f startDrawPen;
    //</editor-fold>

    //<editor-fold desc="Variable tool walkable">
    private static boolean newStateWalkable;
    //</editor-fold>

    //<editor-fold desc="Variable tileset">
    private static Vector2i startDragTileset;
    private static Vector2f startOffsetTileset;
    private static Vector2f startSelectTileset;
    private static boolean selectingTileset;
    private static boolean draggingTileset;
    private static Vector2f tilesetOffset, minTilesetOffset, maxTilesetOffset;
    //</editor-fold>

    //<editor-fold desc="Save">
    private static int menuIndex = 0;
    private static Item[] resizeMenu;
    private static boolean resizing;
    //</editor-fold>
    //</editor-fold>

    public static void initialize() throws IOException {
        EditorMap.SCALE = 1f;
        contentPen = new ArrayList<>();
        int[] array = { 0 };
        contentPen.add(array);
        tool = Tool.Pen;
        layer = 0;
        buttonsLayer = new ArrayList<>();
        startDrawPen = Vector2f.ZERO;
        startSelectPen = Vector2f.ZERO;
        startDragMove = Vector2i.ZERO;
        startOffsetMove = Vector2f.ZERO;
        startDragTileset = Vector2i.ZERO;
        startOffsetTileset = Vector2f.ZERO;
        startSelectTileset = Vector2f.ZERO;
        tilesetOffset = Vector2f.ZERO;
        draggingMove = false;
        selectingPen = false;
        draggingTileset = false;
        newStateWalkable = false;
        resizing = false;
        for (int i = 0; i < 10; i++) {
            Texture texture = new Texture();
            texture.loadFromFile(Paths.get("graphics/editor/" + (i + 1) + ".png"));
            Sprite sprite = new Sprite();
            sprite.setTexture(texture);
            final int l = i;
            Button button = new Button(sprite,
                    () -> selectLayer(l),
                    new Vector2f((i % 2) * 37 + 5, (i / 2) * 37 + 37), new Vector2f(32, 32));
            buttonsLayer.add(button);
        }

        Texture texturePen = new Texture();
        texturePen.loadFromFile(Paths.get("graphics/editor/pen.png"));
        Sprite spritePen = new Sprite();
        spritePen.setTexture(texturePen);
        penButton = new Button(spritePen, MapEditorLayout::selectPen, new Vector2f(5, 257), new Vector2f(32, 32));
        Texture textureEraser = new Texture();
        textureEraser.loadFromFile(Paths.get("graphics/editor/eraser.png"));
        Sprite spriteEraser = new Sprite();
        spriteEraser.setTexture(textureEraser);
        eraserButton = new Button(spriteEraser, MapEditorLayout::selectEraser, new Vector2f(42, 257), new Vector2f(32, 32));
        Texture textureMove = new Texture();
        textureMove.loadFromFile(Paths.get("graphics/editor/move.png"));
        Sprite spriteMove = new Sprite();
        spriteMove.setTexture(textureMove);
        moveButton = new Button(spriteMove, MapEditorLayout::selectMove, new Vector2f(5, 294), new Vector2f(32, 32));
        Texture textureWalkable = new Texture();
        textureWalkable.loadFromFile(Paths.get("graphics/editor/walkable.png"));
        Sprite spriteWalkable = new Sprite();
        spriteWalkable.setTexture(textureWalkable);
        walkableButton = new Button(spriteWalkable, MapEditorLayout::selectWalkable, new Vector2f(42, 294), new Vector2f(32, 32));
        Texture texturePaint = new Texture();
        texturePaint.loadFromFile(Paths.get("graphics/editor/paint.png"));
        Sprite spritePaint = new Sprite();
        spritePaint.setTexture(texturePaint);
        paintButton = new Button(spritePaint, MapEditorLayout::selectPaint, new Vector2f(5, 331), new Vector2f(32, 32));

        Texture textureResize = new Texture();
        textureResize.loadFromFile(Paths.get("graphics/editor/resize.png"));
        Sprite spriteResize = new Sprite();
        spriteResize.setTexture(textureResize);
        resizeButton = new Button(spriteResize, MapEditorLayout::selectResize, new Vector2f(5, 403), new Vector2f(32, 32));

        Texture textureZoom1 = new Texture();
        textureZoom1.loadFromFile(Paths.get("graphics/editor/zoom1.png"));
        Sprite spriteZoom1 = new Sprite();
        spriteZoom1.setTexture(textureZoom1);
        zoom1Button = new Button(spriteZoom1, () -> selectZoom(1f), new Vector2f(5, 475), new Vector2f(32, 32));
        Texture textureZoom2 = new Texture();
        textureZoom2.loadFromFile(Paths.get("graphics/editor/zoom2.png"));
        Sprite spriteZoom2 = new Sprite();
        spriteZoom2.setTexture(textureZoom2);
        zoom2Button = new Button(spriteZoom2, () -> selectZoom(1f / 2f), new Vector2f(42, 475), new Vector2f(32, 32));
        Texture textureZoom4 = new Texture();
        textureZoom4.loadFromFile(Paths.get("graphics/editor/zoom4.png"));
        Sprite spriteZoom4 = new Sprite();
        spriteZoom4.setTexture(textureZoom4);
        zoom4Button = new Button(spriteZoom4, () -> selectZoom(1f / 4f), new Vector2f(5, 512), new Vector2f(32, 32));
        Texture textureZoom8 = new Texture();
        textureZoom8.loadFromFile(Paths.get("graphics/editor/zoom8.png"));
        Sprite spriteZoom8 = new Sprite();
        spriteZoom8.setTexture(textureZoom8);
        zoom8Button = new Button(spriteZoom8, () -> selectZoom(1f / 8f), new Vector2f(42, 512), new Vector2f(32, 32));

        resizeMenu = new Item[3];
        resizeMenu[0] = new Item("Largeur : " + width, MapEditorLayout::width);
        resizeMenu[1] = new Item("Hauteur : " + height, MapEditorLayout::height);
        resizeMenu[2] = new Item("Valider", MapEditorLayout::resizeMap);

        selectLayer(0);
        selectMove();
    }

    private static void width() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            width--;
            resizeMenu[0].setText("Largeur : " + width);
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            width++;
            resizeMenu[0].setText("Largeur : " + width);
        }
    }

    private static void height() {
        if (Input.isKeyPressedOnce(Keyboard.Key.LEFT)) {
            height--;
            resizeMenu[1].setText("Hauteur : " + height);
        } else if (Input.isKeyPressedOnce(Keyboard.Key.RIGHT)) {
            height++;
            resizeMenu[1].setText("Hauteur : " + height);
        }
    }

    private static void setZoom(float scale) {
        EditorMap.SCALE = scale;
        setEditorMap(editorMap);
    }

    //<editor-fold desc="Utils">
    public static void setEditorMap(EditorMap m) {
        editorMap = m;
        mapOffset = Vector2f.ZERO;
        minMapOffset = Vector2f.ZERO;
        maxMapOffset = Vector2f.ZERO;
        editorWidth = (int) MapEditorHandler.getScreenSize().x - 84 - EditorMap.SIZE * 8 - 5;
        editorHeight = (int) MapEditorHandler.getScreenSize().y;
        width = m.getWidth();
        height = m.getHeight();
        int mapWidth = editorMap.getWidth() * EditorMap.getCellSize();
        int mapHeight = editorMap.getHeight() * EditorMap.getCellSize();
        if (mapWidth < editorWidth) {
            mapOffset = new Vector2f((editorWidth - mapWidth) / 2, mapOffset.y);
            minMapOffset = new Vector2f(mapOffset.x, minMapOffset.y);
            maxMapOffset = new Vector2f(mapOffset.x, maxMapOffset.y);
            smallWidth = true;
        } else {
            minMapOffset = new Vector2f(mapWidth - editorWidth, minMapOffset.y);
        }
        if (mapHeight < editorHeight) {
            mapOffset = new Vector2f(mapOffset.x, (editorHeight - mapHeight) / 2);
            minMapOffset = new Vector2f(minMapOffset.x, mapOffset.y);
            maxMapOffset = new Vector2f(maxMapOffset.x, mapOffset.y);
            smallHeight = true;
        } else {
            minMapOffset = new Vector2f(minMapOffset.x, mapHeight - editorHeight);
        }
        mapOffset = new Vector2f(mapOffset.x + 84, mapOffset.y);
        maxMapOffset = new Vector2f(maxMapOffset.x + 84, maxMapOffset.y);
        minMapOffset = new Vector2f(
                mapWidth < editorWidth ? minMapOffset.x + 84 : 84 - minMapOffset.x,
                mapHeight < editorHeight ? minMapOffset.y : -minMapOffset.y
        );

        minTilesetOffset = Vector2f.ZERO;
        Vector2i tilesetSize = editorMap.getTileset().getTexture().getSize();
        maxTilesetOffset = new Vector2f(tilesetSize.x - EditorMap.SIZE * 8, tilesetSize.y - MapEditorHandler.getScreenSize().y);

        resizeMenu[0].setText("Largeur : " + width);
        resizeMenu[1].setText("Hauteur : " + height);
        System.out.println("mapOffset =        " + mapOffset);
        System.out.println("minMapOffset =     " + minMapOffset);
        System.out.println("maxMapOffset =     " + maxMapOffset);
        System.out.println("minTilesetOffset = " + minTilesetOffset);
        System.out.println("maxTilesetOffset = " + maxTilesetOffset);
        System.out.println("editorWidth =      " + editorWidth);
        System.out.println("editorHeight =     " + editorHeight);

    }

    private static void selectEraser() {
        tool = Tool.Eraser;
        eraserButton.setSelected(true);
        penButton.setSelected(false);
        moveButton.setSelected(false);
        walkableButton.setSelected(false);
        paintButton.setSelected(false);
    }

    private static void selectPen() {
        tool = Tool.Pen;
        eraserButton.setSelected(false);
        penButton.setSelected(true);
        moveButton.setSelected(false);
        walkableButton.setSelected(false);
        paintButton.setSelected(false);
    }

    private static void selectMove() {
        tool = Tool.Move;
        eraserButton.setSelected(false);
        penButton.setSelected(false);
        moveButton.setSelected(true);
        walkableButton.setSelected(false);
        paintButton.setSelected(false);
    }

    private static void selectWalkable() {
        tool = Tool.Walkable;
        eraserButton.setSelected(false);
        penButton.setSelected(false);
        moveButton.setSelected(false);
        walkableButton.setSelected(true);
        paintButton.setSelected(false);
    }

    private static void selectPaint() {
        tool = Tool.Paint;
        eraserButton.setSelected(false);
        penButton.setSelected(false);
        moveButton.setSelected(false);
        walkableButton.setSelected(false);
        paintButton.setSelected(true);
    }

    private static void selectResize() {
        menuIndex = 0;
        width = editorMap.getWidth();
        height = editorMap.getHeight();
        resizing = true;
    }

    private static void selectZoom(float scale) {
        zoom1Button.setSelected(false);
        zoom2Button.setSelected(false);
        zoom4Button.setSelected(false);
        zoom8Button.setSelected(false);
        if (scale == 1f / 2f) {
            zoom2Button.setSelected(true);
        } else if (scale == 1f / 4f) {
            zoom4Button.setSelected(true);
        } else if (scale == 1f / 8f) {
            zoom8Button.setSelected(true);
        } else {
            zoom1Button.setSelected(true);
        }
        setZoom(scale);
    }

    private static void resizeMap() {
        if (!Input.isKeyPressedOnce(Keyboard.Key.RETURN)) return;
        editorMap.setDimension(width, height);
        setEditorMap(editorMap);
        resizing = false;
    }

    private static void selectLayer(int n) {
        layer = n;
        for (int i = 0; i < 10; i++) {
            buttonsLayer.get(i).setSelected(false);
        }
        buttonsLayer.get(n).setSelected(true);
    }

    private static boolean isMouseOnMap() {
        int x = 84;
        int y = 0;
        int w = editorWidth;
        int h = editorHeight;
        if (smallWidth) {
            x = (int) minMapOffset.x;
            w = EditorMap.getCellSize() * editorMap.getWidth();
        }
        if (smallHeight) {
            y = (int) minMapOffset.y;
            h = EditorMap.getCellSize() * editorMap.getHeight();
        }
        return Input.isMouseInRect(x, y, w, h);
    }

    private static boolean isMouseOnTileset() {
        return Input.isMouseInRect(
                (int) MapEditorHandler.getScreenSize().x - EditorMap.SIZE * 8,
                0,
                EditorMap.SIZE * 8,
                (int) MapEditorHandler.getScreenSize().y
        );
    }

    private static Vector2f getMapPositionUnderMouse() {
        int x = Input.getCurrentMousePosition().x - (int) mapOffset.x;
        int y = Input.getCurrentMousePosition().y - (int) mapOffset.y;
        x /= EditorMap.getCellSize();
        y /= EditorMap.getCellSize();
        return new Vector2f(x, y);
    }

    private static Vector2f getTilesetPositionUnderMouse() {
        int x = Input.getCurrentMousePosition().x - (84 + editorWidth + 5) + (int) tilesetOffset.x;
        int y = Input.getCurrentMousePosition().y + (int) tilesetOffset.y;
        x /= EditorMap.SIZE;
        y /= EditorMap.SIZE;
        return new Vector2f(x, y);
    }
    //</editor-fold>

    //<editor-fold desc="Update">
    public static void update() {
        if (!resizing) {
            updateButtons();
            switch (tool) {
                case Pen:
                    //<editor-fold desc="Pen">
                    if (isMouseOnMap()) {
                        if (Input.isKeyPressed(Keyboard.Key.LSHIFT) || Input.isKeyPressed(Keyboard.Key.RSHIFT)) {
                            if (Input.isMousePressedOnce()) {
                                selectingPen = true;
                                startSelectPen = getMapPositionUnderMouse();
                            } else if (Input.isMouseReleased()) {
                                Vector2f endSelect = getMapPositionUnderMouse();
                                int minX = (int) Math.min(endSelect.x, startSelectPen.x);
                                int minY = (int) Math.min(endSelect.y, startSelectPen.y);
                                int maxX = (int) Math.max(endSelect.x, startSelectPen.x);
                                int maxY = (int) Math.max(endSelect.y, startSelectPen.y);
                                Vector2i start = new Vector2i(minX, minY);
                                Vector2i end = new Vector2i(maxX, maxY);
                                contentPen = new ArrayList<>();
                                for (int j = start.y; j <= end.y; j++) {
                                    int[] content = new int[end.x - start.x + 1];
                                    for (int i = start.x; i <= end.x; i++) {
                                        content[i - start.x] = editorMap.getCell(i, j).getContent()[layer];
                                    }
                                    contentPen.add(content);
                                }
                                selectingPen = false;
                            }
                        } else {
                            selectingPen = false;
                            if (Input.isMousePressedOnce()) {
                                startDrawPen = getMapPositionUnderMouse();
                            } else if (Input.isMousePressed()) {
                                Vector2f pos = getMapPositionUnderMouse();
                                for (int i = 0; i < contentPen.get(0).length; i++) {
                                    for (int j = 0; j < contentPen.size(); j++) {
                                        int x = ((int) pos.x + i - (int) startDrawPen.x);
                                        while (x < 0) {
                                            x += contentPen.get(0).length;
                                        }
                                        x %= contentPen.get(0).length;
                                        int y = ((int) pos.y + j - (int) startDrawPen.y);
                                        while (y < 0) {
                                            y += contentPen.size();
                                        }
                                        y %= contentPen.size();
                                        editorMap.setCellContent(i + (int) pos.x, j + (int) pos.y, contentPen.get(y)[x], layer);
                                    }
                                }
                            }
                        }
                    } else {
                        selectingPen = false;
                    }
                    //</editor-fold>
                    break;
                case Move:
                    //<editor-fold desc="Move">
                    if (isMouseOnMap() && Input.isMousePressedOnce()) {
                        startDragMove = Input.getCurrentMousePosition();
                        startOffsetMove = mapOffset;
                        draggingMove = true;
                    } else if (Input.isMousePressed() && draggingMove) {
                        Vector2f dragOffset = new Vector2f(startDragMove.x - Input.getCurrentMousePosition().x, startDragMove.y - Input.getCurrentMousePosition().y);
                        mapOffset = Vector2f.sub(startOffsetMove, dragOffset);
                        mapOffset = new Vector2f(Utils.constrain(mapOffset.x, minMapOffset.x, maxMapOffset.x), Utils.constrain(mapOffset.y, minMapOffset.y, maxMapOffset.y));
                    } else {
                        draggingMove = false;
                    }
                    //</editor-fold>
                    break;
                case Eraser:
                    //<editor-fold desc="Eraser">
                    if (isMouseOnMap() && Input.isMousePressed()) {
                        Vector2f pos = getMapPositionUnderMouse();
                        editorMap.setCellContent((int) pos.x, (int) pos.y, -1, layer);
                    }
                    //</editor-fold>
                    break;
                case Walkable:
                    //<editor-fold desc="Walkable">
                    if (isMouseOnMap()) {
                        if (Input.isMousePressedOnce()) {
                            Vector2f pos = getMapPositionUnderMouse();
                            newStateWalkable = !editorMap.getCell((int) pos.x, (int) pos.y).isWalkable();
                            editorMap.getCell((int) pos.x, (int) pos.y).setWalkable(newStateWalkable);
                        } else if (Input.isMousePressed()) {
                            Vector2f pos = getMapPositionUnderMouse();
                            editorMap.getCell((int) pos.x, (int) pos.y).setWalkable(newStateWalkable);
                        }
                    }
                    //</editor-fold>
                    break;
                case Paint:
                    //<editor-fold desc="Paint">
                    if (contentPen.size() != 1 || contentPen.get(0).length != 1) {
                        selectPen();
                    } else {
                        if (Input.isMousePressedOnce() && isMouseOnMap()) {
                            Vector2f[] neighbours = {
                                    new Vector2f(1, 0),
                                    new Vector2f(-1, 0),
                                    new Vector2f(0, 1),
                                    new Vector2f(0, -1)
                            };
                            ArrayList<Vector2f> treated = new ArrayList<>();
                            ArrayList<Vector2f> cells = new ArrayList<>();
                            Vector2f pos = getMapPositionUnderMouse();
                            int content = editorMap.getCell((int) pos.x, (int) pos.y).getContent()[layer];
                            cells.add(pos);
                            for (Vector2f v : neighbours) {
                                Vector2f neighbour = Vector2f.add(v, pos);
                                if (neighbour.x >= 0 && neighbour.y >= 0 && neighbour.x < editorMap.getWidth() && neighbour.y < editorMap.getHeight()
                                        && editorMap.getCell((int) neighbour.x, (int) neighbour.y).getContent()[layer] == content) {
                                    cells.add(neighbour);
                                }
                            }
                            while (!cells.isEmpty()) {
                                for (int i = 0; i < cells.size(); i++) {
                                    Vector2f cellPos = cells.get(i);
                                    if (cellPos.x < 0 || cellPos.y < 0 || cellPos.x >= editorMap.getWidth() || cellPos.y >= editorMap.getHeight()) {
                                        cells.remove(cellPos);
                                        continue;
                                    }
                                    if (editorMap.getCell((int) cellPos.x, (int) cellPos.y).getContent()[layer] == content) {
                                        editorMap.setCellContent((int) cellPos.x, (int) cellPos.y, contentPen.get(0)[0], layer);
                                    }
                                    cells.remove(cellPos);
                                    treated.add(cellPos);
                                    for (Vector2f v : neighbours) {
                                        Vector2f neighbour = Vector2f.add(v, cellPos);
                                        if (neighbour.x >= 0 && neighbour.y >= 0 && neighbour.x < editorMap.getWidth() && neighbour.y < editorMap.getHeight()
                                                && editorMap.getCell((int) neighbour.x, (int) neighbour.y).getContent()[layer] == content
                                                && !treated.contains(neighbour)) {
                                            cells.add(neighbour);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //</editor-fold>
                    break;
                default:
                    break;
            }
            //<editor-fold desc="Tileset">
            selectingTileset = !Input.isKeyPressed(Keyboard.Key.LSHIFT) && !Input.isKeyPressed(Keyboard.Key.RSHIFT);
            if (selectingTileset) {
                if (Input.isMousePressedOnce() && isMouseOnTileset()) {
                    startSelectTileset = getTilesetPositionUnderMouse();
                } else if (Input.isMouseReleased() && isMouseOnTileset()) {
                    Vector2f endSelect = getTilesetPositionUnderMouse();
                    int minX = (int) Math.min(endSelect.x, startSelectTileset.x);
                    int minY = (int) Math.min(endSelect.y, startSelectTileset.y);
                    int maxX = (int) Math.max(endSelect.x, startSelectTileset.x);
                    int maxY = (int) Math.max(endSelect.y, startSelectTileset.y);
                    Vector2i start = new Vector2i(minX, minY);
                    Vector2i end = new Vector2i(maxX, maxY);
                    contentPen = new ArrayList<>();
                    for (int j = start.y; j <= end.y; j++) {
                        int[] content = new int[end.x - start.x + 1];
                        for (int i = start.x; i <= end.x; i++) {
                            content[i - start.x] = i + j * 8;
                        }
                        contentPen.add(content);
                    }
                    draggingTileset = false;
                }
            } else {
                if (isMouseOnTileset() && Input.isMousePressedOnce()) {
                    startDragTileset = Input.getCurrentMousePosition();
                    startOffsetTileset = tilesetOffset;
                    draggingTileset = true;
                } else if (Input.isMousePressed() && draggingTileset) {
                    Vector2f dragOffset = new Vector2f(startDragTileset.x - Input.getCurrentMousePosition().x, startDragTileset.y - Input.getCurrentMousePosition().y);
                    tilesetOffset = Vector2f.add(dragOffset, startOffsetTileset);
                    tilesetOffset = new Vector2f(Utils.constrain(tilesetOffset.x, minTilesetOffset.x, maxTilesetOffset.x), Utils.constrain(tilesetOffset.y, minTilesetOffset.y, maxTilesetOffset.y));
                } else {
                    draggingTileset = false;
                }
            }
            //</editor-fold>

            //<editor-fold desc="Save">
            if (Input.isKeyPressedOnce(Keyboard.Key.S)) {
                String mapName = editorMap.getName();
                JSONObject jsonObject = Utils.readJSON("datas/maps.json");
                JSONArray maps = (JSONArray) jsonObject.get("maps");
                for (int i = 0; i < maps.size(); i++) {
                    JSONObject mapJSON = (JSONObject) maps.get(i);
                    if (mapJSON.get("name").equals(mapName)) {
                        maps.remove(i);
                        break;
                    }
                }
                maps.add(editorMap.toJSON(mapName));
                try {
                    FileWriter file = new FileWriter("datas/maps.json");
                    file.write(Utils.reformatJSON(jsonObject, 0));
                    file.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //</editor-fold>
        } else {
            if (Input.isKeyPressedOnce(Keyboard.Key.DOWN)) {
                menuIndex++;
            } else if (Input.isKeyPressedOnce(Keyboard.Key.UP)) {
                menuIndex--;
            }
            if (menuIndex < 0) {
                menuIndex += resizeMenu.length;
            }
            menuIndex %= resizeMenu.length;
            resizeMenu[menuIndex].runMethod();
        }
    }

    private static void updateButtons() {
        for (int i = 0; i < 10; i++) {
            buttonsLayer.get(i).update();
        }
        penButton.update();
        eraserButton.update();
        moveButton.update();
        walkableButton.update();
        paintButton.update();

        resizeButton.update();

        zoom1Button.update();
        zoom2Button.update();
        zoom4Button.update();
        zoom8Button.update();
    }
    //</editor-fold>

    //<editor-fold desc="Draw">
    private static void drawRectangle(RenderWindow window, int x, int y, int width, int height) {
        float strokeWeight = 4;
        if (EditorMap.SCALE == 1f / 2f) {
            strokeWeight = 3;
        } else if (EditorMap.SCALE == 1f / 4f) {
            strokeWeight = 2;
        } else if (EditorMap.SCALE == 1f / 8f) {
            strokeWeight = 1;
        }

        RectangleShape rectangleShape = new RectangleShape();
        rectangleShape.setFillColor(new Color(0, 0, 0, 192));
        rectangleShape.setSize(new Vector2f(width, strokeWeight));
        rectangleShape.setPosition(x, y);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(strokeWeight, height - strokeWeight));
        rectangleShape.setPosition(x, y + strokeWeight);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(width - strokeWeight, strokeWeight));
        rectangleShape.setPosition(x + strokeWeight, y + height - strokeWeight);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(strokeWeight, height - strokeWeight * 2));
        rectangleShape.setPosition(x + width - strokeWeight, y + strokeWeight);
        window.draw(rectangleShape);
    }

    private static void drawRectangle(RenderWindow window, int x, int y, int width, int height, int strokeWeight) {
        RectangleShape rectangleShape = new RectangleShape();
        rectangleShape.setFillColor(new Color(0, 0, 0, 192));
        rectangleShape.setSize(new Vector2f(width, strokeWeight));
        rectangleShape.setPosition(x, y);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(strokeWeight, height - strokeWeight));
        rectangleShape.setPosition(x, y + strokeWeight);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(width - strokeWeight, strokeWeight));
        rectangleShape.setPosition(x + strokeWeight, y + height - strokeWeight);
        window.draw(rectangleShape);

        rectangleShape.setSize(new Vector2f(strokeWeight, height - strokeWeight * 2));
        rectangleShape.setPosition(x + width - strokeWeight, y + strokeWeight);
        window.draw(rectangleShape);
    }

    public static void draw(RenderWindow window) {
        RectangleShape rectangleShape = new RectangleShape();

        //<editor-fold desc="Map">
        rectangleShape.setSize(Vector2f.sub(MapEditorHandler.getScreenSize(), new Vector2f(84 + EditorMap.getCellSize() * 8, 0)));
        rectangleShape.setFillColor(new Color(200, 200, 200));
        rectangleShape.setPosition(84, 0);
        window.draw(rectangleShape);
        int x = (int) -mapOffset.x + 84;
        int y = (int) -mapOffset.y;
        int minX = Math.max(x / EditorMap.getCellSize(), 0);
        int minY = Math.max(y / EditorMap.getCellSize(), 0);
        int maxX = Math.min((x + editorWidth) / EditorMap.getCellSize() + 1, editorMap.getWidth());
        int maxY = Math.min((y + editorHeight) / EditorMap.getCellSize() + 1, editorMap.getHeight());
        switch (tool) {
            case Walkable:
                editorMap.drawAllLayers(window, mapOffset, minX, minY, maxX, maxY);
                break;
            default:
                editorMap.drawAllLayers(window, mapOffset, minX, minY, maxX, maxY, layer);
                break;
        }
        //</editor-fold>

        if (!resizing) {
            //<editor-fold desc="Tools">
            switch (tool) {
                case Pen:
                    if (isMouseOnMap()) {
                        if (!selectingPen) {
                            int w = contentPen.get(0).length;
                            int h = contentPen.size();
                            Vector2f pos = getMapPositionUnderMouse();
                            if (pos.x + w >= editorMap.getWidth()) {
                                w = editorMap.getWidth() - (int) pos.x;
                            }
                            if (pos.y + h >= editorMap.getHeight()) {
                                h = editorMap.getHeight() - (int) pos.y;
                            }
                            x = (int) pos.x;
                            y = (int) pos.y;
                            x *= EditorMap.getCellSize();
                            y *= EditorMap.getCellSize();
                            x += mapOffset.x;
                            y += mapOffset.y;
                            drawRectangle(window, x, y, w * EditorMap.getCellSize(), h * EditorMap.getCellSize());
                        } else {
                            if (Input.isMousePressed()) {
                                Vector2f currentSelect = getMapPositionUnderMouse();
                                minX = (int) Math.max(0, Math.min(currentSelect.x, startSelectPen.x));
                                minY = (int) Math.max(0, Math.min(currentSelect.y, startSelectPen.y));
                                maxX = (int) Math.min(editorMap.getWidth() - 1, Math.max(currentSelect.x, startSelectPen.x));
                                maxY = (int) Math.min(editorMap.getHeight() - 1, Math.max(currentSelect.y, startSelectPen.y));
                                Vector2f corner = new Vector2f(minX, minY);
                                int w = maxX - minX + 1;
                                int h = maxY - minY + 1;
                                x = (int) corner.x;
                                y = (int) corner.y;
                                x *= EditorMap.getCellSize();
                                y *= EditorMap.getCellSize();
                                x += mapOffset.x;
                                y += mapOffset.y;
                                drawRectangle(window, x, y, w * EditorMap.getCellSize(), h * EditorMap.getCellSize());
                            } else {
                                Vector2f pos = getMapPositionUnderMouse();
                                x = (int) pos.x;
                                y = (int) pos.y;
                                x *= EditorMap.getCellSize();
                                y *= EditorMap.getCellSize();
                                x += mapOffset.x;
                                y += mapOffset.y;
                                drawRectangle(window, x, y, EditorMap.getCellSize(), EditorMap.getCellSize());
                            }
                        }
                    }
                    break;
                case Eraser:
                case Walkable:
                case Paint:
                    if (isMouseOnMap()) {
                        Vector2f pos = getMapPositionUnderMouse();
                        x = (int) pos.x;
                        y = (int) pos.y;
                        x *= EditorMap.getCellSize();
                        y *= EditorMap.getCellSize();
                        x += mapOffset.x;
                        y += mapOffset.y;
                        drawRectangle(window, x, y, EditorMap.getCellSize(), EditorMap.getCellSize());
                    }
                    break;
                default:
                    break;
            }
            //</editor-fold>
        }

        //<editor-fold desc="Tileset">
        Sprite tilesetSprite = new Sprite();
        tilesetSprite.setTexture(editorMap.getTileset().getTexture());
        tilesetSprite.setTextureRect(new IntRect((int) tilesetOffset.x, (int) tilesetOffset.y, 32 * 8, (int) MapEditorHandler.getScreenSize().y));
        tilesetSprite.setPosition(84 + editorWidth + 5, 0);
        rectangleShape.setPosition(84 + editorWidth + 5, 0);
        rectangleShape.setFillColor(new Color(200, 200, 200));
        rectangleShape.setSize(new Vector2f(32 * 8, MapEditorHandler.getScreenSize().y));
        window.draw(rectangleShape);
        window.draw(tilesetSprite);
        //</editor-fold>

        if (!resizing) {
            //<editor-fold desc="Tileset selection">
            if (isMouseOnTileset() && selectingTileset) {
                if (Input.isMousePressed()) {
                    Vector2f currentSelect = getTilesetPositionUnderMouse();
                    minX = (int) Math.max(0, Math.min(currentSelect.x, startSelectTileset.x));
                    minY = (int) Math.max(0, Math.min(currentSelect.y, startSelectTileset.y));
                    maxX = (int) Math.min(editorMap.getTileset().getWidth() - 1, Math.max(currentSelect.x, startSelectTileset.x));
                    maxY = (int) Math.min(editorMap.getTileset().getHeight() - 1, Math.max(currentSelect.y, startSelectTileset.y));
                    Vector2f corner = new Vector2f(minX, minY);
                    int w = maxX - minX + 1;
                    int h = maxY - minY + 1;
                    x = (int) corner.x;
                    y = (int) corner.y;
                    x *= EditorMap.SIZE;
                    y *= EditorMap.SIZE;
                    x += -tilesetOffset.x + 84 + editorWidth + 5;
                    y += -tilesetOffset.y;
                    drawRectangle(window, x, y, w * EditorMap.SIZE, h * EditorMap.SIZE, 4);
                } else {
                    Vector2f pos = getTilesetPositionUnderMouse();
                    x = (int) pos.x;
                    y = (int) pos.y;
                    x *= EditorMap.SIZE;
                    y *= EditorMap.SIZE;
                    x += -tilesetOffset.x + 84 + editorWidth + 5;
                    y += -tilesetOffset.y;
                    drawRectangle(window, x, y, EditorMap.SIZE, EditorMap.SIZE, 4);
                }
            }
            //</editor-fold>
        }

        //<editor-fold desc="Tools background">
        rectangleShape.setPosition(0, 0);
        rectangleShape.setSize(new Vector2f(79, MapEditorHandler.getScreenSize().y));
        rectangleShape.setFillColor(Color.WHITE);
        window.draw(rectangleShape);

        Text text = new Text();
        text.setFont(Textures.getFont());
        text.setCharacterSize(23);
        text.setColor(Color.BLACK);
        text.setPosition(5, 5);
        text.setString("Couche");
        window.draw(text);
        text.setPosition(5, 225);
        text.setString("Outil");
        window.draw(text);
        text.setPosition(5, 371);
        text.setString("Édition");
        window.draw(text);
        text.setPosition(5, 443);
        text.setString("Zoom");
        window.draw(text);
        buttonsLayer.forEach(e -> e.draw(window));
        eraserButton.draw(window);
        penButton.draw(window);
        moveButton.draw(window);
        walkableButton.draw(window);
        paintButton.draw(window);
        resizeButton.draw(window);
        zoom1Button.draw(window);
        zoom2Button.draw(window);
        zoom4Button.draw(window);
        zoom8Button.draw(window);
        //</editor-fold>

        //<editor-fold desc="Delimiters">
        rectangleShape.setSize(new Vector2f(5, MapEditorHandler.getScreenSize().y));
        rectangleShape.setFillColor(Color.BLACK);
        rectangleShape.setPosition(79, 0);
        window.draw(rectangleShape);
        rectangleShape.setPosition(84 + editorWidth, 0);
        window.draw(rectangleShape);
        //</editor-fold>

        //<editor-fold desc="Resize">
        if (resizing) {
            rectangleShape.setSize(MapEditorHandler.getScreenSize());
            rectangleShape.setPosition(0, 0);
            rectangleShape.setFillColor(new Color(0, 0, 0, 128));
            window.draw(rectangleShape);

            text.setCharacterSize(32);
            text.setFont(Textures.getFont());
            text.setColor(new Color(192, 192, 192));
            text.setString("Redimensionner");
            text.setPosition(300, 300);
            window.draw(text);
            text.setColor(Color.WHITE);
            for (int i = 0; i < resizeMenu.length; i++) {
                String item = resizeMenu[i].getText();
                text.setCharacterSize(28);
                text.setString(item);
                text.setPosition(300 + (i == menuIndex ? 30 : 0), 300 + (i + 1) * 30);
                window.draw(text);
            }
        }
        //</editor-fold>
    }
    //</editor-fold>
}