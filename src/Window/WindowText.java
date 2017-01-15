package Window;

import Textures.Textures;
import Utils.Input;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.window.Keyboard;

import java.util.ArrayList;

/**
 * Created by Thomas VENNER on 23/07/2016.
 */
public class WindowText extends Window {
    public enum Position {
        top, center, bottom
    }

    private ArrayList<Text> texts;
    private Color fontColor;
    private int maxLength;
    private boolean lockSize;
    private int fontSize = 16;
    protected int index;
    protected int length;

    public WindowText(Color windowColor, int width, String string, boolean lockSize, Position position, Color fontColor, int screenWidth, int screenHeight) {
        super(windowColor, 0, 0, width, 0);
        texts = new ArrayList<>();
        this.maxLength = width;
        this.lockSize = lockSize;
        string = string.replaceAll(" +$", " ");
        string = string.replaceAll(" +", " ");
        this.fontColor = fontColor;
        setString(string, maxLength);
        place(position, screenWidth, screenHeight);
    }

    public void setString(String string, int maxLength) {
        index = 0;
        ArrayList<String> strings = new ArrayList<>();
        int lastSpace = 0;
        int lastAdd = 0;
        FloatRect bounds;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == ' ') {
                lastSpace = i;
            }
            Text text = new Text();
            text.setFont(Textures.getFont());
            text.setCharacterSize(fontSize);
            text.setColor(fontColor);
            text.setString(string.substring(lastAdd, i));
            bounds = text.getLocalBounds();
            if (bounds.width >= maxLength) {
                strings.add(string.substring(lastAdd, lastSpace));
                i = lastSpace + 1;
                lastAdd = lastSpace + 1;
            }
        }
        strings.add(string.substring(lastAdd, string.length()));
        float height = 0;
        float maxWidth = 0;
        for (String s : strings) {
            Text text = new Text();
            text.setFont(Textures.getFont());
            text.setCharacterSize(fontSize);
            text.setString(s);
            length += s.length();
            text.setPosition(9, height + 2);
            bounds = text.getLocalBounds();
            if (bounds.width > maxWidth) {
                maxWidth = bounds.width;
            }
            height += bounds.height + fontSize / 2;
            texts.add(text);
        }
        if (!lockSize) {
            this.width = (int) maxWidth + 18;
            this.height = (int) height + fontSize / 3 + 4;
        }
    }

    public void place(Position position, int screenWidth, int screenHeight) {
        x = screenWidth / 2 - width / 2;
        if (position == Position.bottom) {
            y = screenHeight - height - 50;
        } else if (position == Position.center) {
            y = screenHeight / 2 - height / 2;
        } else {
            y = 50;
        }
    }

    public boolean update() {
        if (index != Integer.MAX_VALUE) {
            index += 3;
        }
        if (Input.isKeyPressedOnce(Keyboard.Key.RETURN)) {
            if (index >= length) {
                return true;
            } else {
                index = Integer.MAX_VALUE;
            }
        }
        return false;
    }

    public boolean fullyDrawn() {
        return index >= length;
    }

    @Override
    public void draw(RenderWindow window) {
        super.draw(window);
        int draw = 0;
        boolean needBreak = false;
        for (Text text : texts) {
            String string = text.getString();
            if (string.length() + draw >= index) {
                string = string.substring(0, index - draw);
                needBreak = true;
            } else {
                draw += string.length();
            }
            Text text2 = new Text();
            text2.setPosition(text.getPosition().x + x, text.getPosition().y + y);
            text2.setFont(text.getFont());
            text2.setColor(text.getColor());
            text2.setCharacterSize(text.getCharacterSize());
            text2.setString(string);
            window.draw(text2);
            if (needBreak) {
                break;
            }
        }
    }
}
