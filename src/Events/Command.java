package Events;

import org.jsfml.graphics.RenderWindow;

/**
 * Created by Thomas VENNER on 24/07/2016.
 */
public abstract class Command {
    protected Event parent;

    public Command(Event parent) {
        this.parent = parent;
    }

    protected void update() {

    }

    protected void draw(RenderWindow window) {

    }
}
