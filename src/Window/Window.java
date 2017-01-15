package Window;

import Textures.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;

import java.util.ArrayList;

/**
 * Created by Thomas VENNER on 23/07/2016.
 */
public abstract class Window {
    private Color windowColor;
    protected int x;
    protected int y;
    int width;
    int height;

    protected Window(Color windowColor, int x, int y, int width, int height) {
        this.windowColor = new Color(windowColor.r, windowColor.g, windowColor.b, 128);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void draw(RenderWindow window) {
        ArrayList<Sprite> toDraw = new ArrayList<>();
        Sprite background = new Sprite();
        background.setTexture(Textures.getWindowskin());
        background.setTextureRect(new IntRect(0, 0, 64, 64));
        background.setPosition(x + 2, y + 2);
        background.setScale(((float) width - 4f) / 64f, ((float) height - 4f) / 64f);
        background.setColor(windowColor);
        toDraw.add(background);

        Sprite tlCorner = new Sprite();
        tlCorner.setTexture(Textures.getWindowskin());
        tlCorner.setTextureRect(new IntRect(64, 0, 7, 7));
        tlCorner.setPosition(x, y);
        toDraw.add(tlCorner);

        Sprite trCorner = new Sprite();
        trCorner.setTexture(Textures.getWindowskin());
        trCorner.setTextureRect(new IntRect(121, 0, 7, 7));
        trCorner.setPosition(x + width - 7, y);
        toDraw.add(trCorner);

        Sprite blCorner = new Sprite();
        blCorner.setTexture(Textures.getWindowskin());
        blCorner.setTextureRect(new IntRect(64, 57, 7, 7));
        blCorner.setPosition(x, y + height - 7);
        toDraw.add(blCorner);

        Sprite brCorner = new Sprite();
        brCorner.setTexture(Textures.getWindowskin());
        brCorner.setTextureRect(new IntRect(121, 57, 7, 7));
        brCorner.setPosition(x + width - 7, y + height - 7);
        toDraw.add(brCorner);

        Sprite topBar = new Sprite();
        topBar.setTexture(Textures.getWindowskin());
        topBar.setTextureRect(new IntRect(71, 0, 1, 7));
        topBar.setPosition(x + 7, y);
        topBar.setScale(width - 14, 1);
        toDraw.add(topBar);

        Sprite leftBar = new Sprite();
        leftBar.setTexture(Textures.getWindowskin());
        leftBar.setTextureRect(new IntRect(64, 7, 7, 1));
        leftBar.setPosition(x, y + 7);
        leftBar.setScale(1, height - 14);
        toDraw.add(leftBar);

        Sprite bottomBar = new Sprite();
        bottomBar.setTexture(Textures.getWindowskin());
        bottomBar.setTextureRect(new IntRect(71, 57, 1, 7));
        bottomBar.setPosition(x + 7, y + height - 7);
        bottomBar.setScale(width - 14, 1);
        toDraw.add(bottomBar);

        Sprite rightBar = new Sprite();
        rightBar.setTexture(Textures.getWindowskin());
        rightBar.setTextureRect(new IntRect(121, 7, 7, 1));
        rightBar.setPosition(x + width - 7, y + 7);
        rightBar.setScale(1, height - 14);
        toDraw.add(rightBar);

        toDraw.forEach(window::draw);
    }
}
