package Editor;

import Utils.Input;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by Thomas VENNER on 18/12/2016.
 */
public class Button {
    private Sprite sprite;
    private Runnable method;
    private Vector2f position;
    private Vector2f size;
    private boolean selected;
    private Sprite selectedSprite;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Button(Sprite sprite, Runnable method, Vector2f position, Vector2f size) throws IOException {
        this.sprite = sprite;
        this.method = method;
        this.position = position;
        this.sprite.setPosition(position);
        this.size = size;
        this.selected = false;
        Texture texture = new Texture();
        texture.loadFromFile(Paths.get("graphics/editor/selected.png", new String[0]));
        this.selectedSprite = new Sprite();
        this.selectedSprite.setTexture(texture);
        this.selectedSprite.setPosition(position);
    }

    public void update() {
        if (Input.isMousePressedOnce() && Input.isMouseInRect((int)position.x, (int)position.y, (int)size.x, (int)size.y)) {
            run();
        }
    }

    private void run() {
        method.run();
    }

    public void draw(RenderWindow window) {
        if (selected) {
            window.draw(selectedSprite);
        }
        window.draw(sprite);
    }
}
