package Game.Player;

import Utils.Input;
import Game.Map.*;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

/**
 * Created by Thomas VENNER on 18/07/2016.
 */
public class Player {
    private static Color[] COLORS = {
            new Color(255, 0, 0),
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(0, 0, 255),
            new Color(255, 0, 255)
    };
    private static int INDEX_COLOR = 0;
    private static int SIZE = Map.SIZE - 3;
    private static Vector2f GRAVITY = new Vector2f(0, 1);
    private static float MAX_SPEED = SIZE - 1;

    private Color color;
    private Vector2f position;
    private Vector2f speed;

    public Player() {
        position = new Vector2f(26, 26);
        speed = new Vector2f(0, 0);
        color = COLORS[INDEX_COLOR];
    }

    public void update(Map map) {
        //<editor-fold desc="Colors">
        if (color.equals(COLORS[(INDEX_COLOR + 1) % COLORS.length])) {
            INDEX_COLOR++;
            INDEX_COLOR %= COLORS.length;
        }
        int red = color.r;
        int green = color.g;
        int blue = color.b;
        int red_goal = COLORS[(INDEX_COLOR + 1) % COLORS.length].r;
        int green_goal = COLORS[(INDEX_COLOR + 1) % COLORS.length].g;
        int blue_goal = COLORS[(INDEX_COLOR + 1) % COLORS.length].b;
        red += Math.signum(red_goal - red);
        green += Math.signum(green_goal - green);
        blue += Math.signum(blue_goal - blue);
        color = new Color(red, green, blue);
        //</editor-fold desc="Colors">

        //<editor-fold desc="Inputs">
        if (Input.isKeyPressed(Keyboard.Key.UP)) {
            speed = new Vector2f(speed.x, speed.y - 5);
        }
        if (Input.isKeyPressed(Keyboard.Key.RIGHT)) {
            speed = new Vector2f(speed.x + 5, speed.y);
        }
        else if (Input.isKeyPressed(Keyboard.Key.RIGHT)) {
            speed = new Vector2f(speed.x - 5, speed.y);
        }
        //</editor-fold desc="Inputs">

        //<editor-fold desc="Collision">
        speed = new Vector2f(speed.x + GRAVITY.x, speed.y + GRAVITY.y);
        float length = (float)Math.sqrt(speed.x * speed.x + speed.y * speed.y);
        if (length > MAX_SPEED) {
            speed = Vector2f.div(speed, length);
            speed = Vector2f.mul(speed, MAX_SPEED);
        }
        Vector2f[] corners = {
                new Vector2f(position.x + speed.x, position.y + speed.y),
                new Vector2f(position.x + SIZE + speed.x, position.y + speed.y),
                new Vector2f(position.x + speed.x, position.y + SIZE + speed.y),
                new Vector2f(position.x + SIZE + speed.x, position.y + SIZE + speed.y)
        };
        boolean canMove = true;
        for (Vector2f corner :
             corners) {
            Cell cell = map.getCell((int)(corner.x / Map.SIZE), (int)(corner.y / Map.SIZE));
            if (cell == null)
                continue;
            if (!cell.getWalkable()) {
                canMove = false;
                break;
            }
        }
        if (canMove) {
            position = new Vector2f(position.x + speed.x, position.y + speed.y);
        }
        else {
            speed = new Vector2f(0, 0);
        }
        //</editor-fold desc="Collision">
    }

    public void draw(RenderWindow window) {
        RectangleShape rect1 = new RectangleShape(new Vector2f(SIZE, SIZE));
        rect1.setPosition(position.x, position.y);
        rect1.setFillColor(Color.BLACK);
        RectangleShape rect2 = new RectangleShape(new Vector2f(SIZE - 6, SIZE - 6));
        rect2.setPosition(position.x + 3, position.y + 3);
        rect2.setFillColor(color);
        window.draw(rect1);
        window.draw(rect2);
    }
}
