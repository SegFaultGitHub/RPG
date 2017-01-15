package Events;

import Window.WindowText;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;

/**
 * Created by Thomas VENNER on 24/07/2016.
 */
public class MessageCommand extends Command {
    private WindowText windowText;

    public MessageCommand(Event parent, Color windowColor, int width, String string, boolean lockSize, WindowText.Position position, Color fontColor, int screenWidth, int screenHeight) {
        super(parent);
        windowText = new WindowText(windowColor, width, string, lockSize, position, fontColor, screenWidth, screenHeight);
    }

    @Override
    public void update() {
        if (windowText.update()) {
            parent.nextCommand();
        }
    }

    @Override
    public void draw(RenderWindow window) {
        windowText.draw(window);
    }
}
