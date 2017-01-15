package Game.Map;

import org.jsfml.graphics.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas VENNER on 18/07/2016.
 */
public class Cell {
    private static List<Integer> NON_WALKABLES = Arrays.asList(
            1,
            2
    );
    private List<Integer> content;
    private boolean walkable;

    public List<Integer> getContent() {
        return content;
    }

    public Cell(List<Integer> content, boolean walkable) {
        this.content = content;
//        this.walkable = !NON_WALKABLES.contains(content);
        this.walkable = walkable;
    }

    public boolean getWalkable() {
        return walkable;
    }

    public void draw(RenderTarget window, Texture texture, int x, int y, int layer) {
        if (content.size() <= layer || content.get(layer) == -1)
            return;
        int i = (content.get(layer) % 8) * 32;
        int j = (content.get(layer) / 8) * 32;
        Sprite sprite = new Sprite();
        sprite.setTexture(texture);
        sprite.setTextureRect(new IntRect(i, j, 32, 32));
        sprite.setPosition(x, y);
        window.draw(sprite);
    }
}
