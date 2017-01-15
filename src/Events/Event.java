package Events;

import org.jsfml.graphics.RenderWindow;

import java.util.ArrayList;

/**
 * Created by Thomas VENNER on 24/07/2016.
 */
public class Event {
    private ArrayList<Command> commands;
    private int index;
    private int x, y;

    public Event(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
        index = 0;
    }

    public void nextCommand() {
        index++;
    }

    public void update() {
        Command command = commands.get(index);
        command.update();
    }

    public void draw(RenderWindow window) {
        Command command = commands.get(index);
        command.draw(window);
    }
}
