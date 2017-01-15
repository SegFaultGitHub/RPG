package Editor.Map;

import Utils.Utils;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

/**
 * Created by Thomas VENNER on 14/12/2016.
 */
public class EditorCell {
    private boolean walkable;
    private Integer[] content;

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public boolean isWalkable() {
        return walkable;
    }

    public EditorCell(Integer[] content, boolean walkable) {
        this.content = content;
        this.walkable = walkable;
    }

    public Integer[] getContent() {
        return content;
    }

    public void setContent(int content, int layer) {
        this.content[layer] = content;
    }

    public void draw(RenderTarget target, Texture texture, int x, int y, int layer, boolean even, int layerDiff) {
        if (content.length <= layer)
            return;
        if (layer == 0) {
            RectangleShape rectangleShape = new RectangleShape();
            rectangleShape.setSize(new Vector2f(EditorMap.SIZE * EditorMap.SCALE, EditorMap.SIZE * EditorMap.SCALE));
            rectangleShape.setPosition(x, y);
            float n = even ? 2f : 3f;
            rectangleShape.setFillColor(new Color((int)(n * 60), (int)(n * 60), (int)(n * 80)));
            target.draw(rectangleShape);
        }
        if (content[layer] == -1) return;
        int i = (content[layer] % 8) * 32;
        int j = (content[layer] / 8) * 32;
        Sprite sprite = new Sprite();
        sprite.setTexture(texture);
        sprite.setTextureRect(new IntRect(i, j, 32, 32));
        sprite.setPosition(x, y);
        sprite.setScale(EditorMap.SCALE, EditorMap.SCALE);
        if (layerDiff < 0) {
            sprite.setColor(new Color(192, 192, 192));
        } else if (layerDiff > 0) {
            sprite.setColor(new Color(255, 255, 255, 128));
        }
        target.draw(sprite);
    }

    public void draw(RenderTarget target, Texture texture, int x, int y, int layer, boolean even) {
        if (content.length <= layer)
            return;
        if (layer == 0) {
            RectangleShape rectangleShape = new RectangleShape();
            rectangleShape.setSize(new Vector2f(EditorMap.SIZE * EditorMap.SCALE, EditorMap.SIZE * EditorMap.SCALE));
            rectangleShape.setPosition(x, y);
            float n = even ? 2f : 3f;
            rectangleShape.setFillColor(new Color((int)(n * 60), (int)(n * 60), (int)(n * 80)));
            target.draw(rectangleShape);
        }
        if (content[layer] == -1) return;
        int i = (content[layer] % 8) * 32;
        int j = (content[layer] / 8) * 32;
        Sprite sprite = new Sprite();
        sprite.setTexture(texture);
        sprite.setTextureRect(new IntRect(i, j, 32, 32));
        sprite.setPosition(x, y);
        sprite.setScale(EditorMap.SCALE, EditorMap.SCALE);
        target.draw(sprite);
    }

    public String getContentString() {
        return Utils.implode(content, "/");
    }
}
