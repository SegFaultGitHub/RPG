package TilesetsManager;

import Game.Map.OldTilesets;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by Thomas VENNER on 25/07/2016.
 */
public class SetDirectionsFrame extends JFrame {
    private String tileset;
    private int index;

    private BufferedImage cropImage(BufferedImage src, int x, int y, int width, int height) {
        BufferedImage dest = src.getSubimage(x, y, width, height);
        return dest;
    }

    public SetDirectionsFrame(String tileset, int index, ImageIcon image, HashMap<OldTilesets.Direction, Boolean> directions) {
        int x = (index % 8) * 32;
        int y = (index / 8) * 32;
        BufferedImage im = cropImage((BufferedImage) image.getImage(), x, y, 32, 32);
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(im));

        JLabel upLabel = new JLabel("Up");
        JRadioButton upRB = new JRadioButton();
        upRB.setSelected(directions.get(OldTilesets.Direction.up));
        JLabel downLabel = new JLabel("Down");
        JRadioButton downRB = new JRadioButton();
        downRB.setSelected(directions.get(OldTilesets.Direction.down));
        JLabel leftLabel = new JLabel("Left");
        JRadioButton leftRB = new JRadioButton();
        leftRB.setSelected(directions.get(OldTilesets.Direction.left));
        JLabel rightLabel = new JLabel("Right");
        JRadioButton rightRB = new JRadioButton();
        rightRB.setSelected(directions.get(OldTilesets.Direction.right));

        JPanel mainPanel = new JPanel();
        Container upCont = new Container();
        upCont.setLayout(new BoxLayout(upCont, BoxLayout.X_AXIS));
        upCont.add(upLabel);
        upCont.add(upRB);
        Container downCont = new Container();
        downCont.setLayout(new BoxLayout(downCont, BoxLayout.X_AXIS));
        downCont.add(downLabel);
        downCont.add(downRB);
        Container leftCont = new Container();
        leftCont.setLayout(new BoxLayout(leftCont, BoxLayout.X_AXIS));
        leftCont.add(leftLabel);
        leftCont.add(leftRB);
        Container rightCont = new Container();
        rightCont.setLayout(new BoxLayout(rightCont, BoxLayout.X_AXIS));
        rightCont.add(rightLabel);
        rightCont.add(rightRB);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            HashMap<OldTilesets.Direction, Boolean> dirs = new HashMap<>();
            dirs.put(OldTilesets.Direction.up, upRB.isSelected());
            dirs.put(OldTilesets.Direction.down, downRB.isSelected());
            dirs.put(OldTilesets.Direction.left, leftRB.isSelected());
            dirs.put(OldTilesets.Direction.right, rightRB.isSelected());
            OldTilesets.setDirections(tileset, index, dirs);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));
        Container buttonsCont = new Container();
        buttonsCont.setLayout(new BoxLayout(buttonsCont, BoxLayout.X_AXIS));
        buttonsCont.add(okButton);
        buttonsCont.add(cancelButton);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(label);
        mainPanel.add(upCont);
        mainPanel.add(downCont);
        mainPanel.add(leftCont);
        mainPanel.add(rightCont);
        mainPanel.add(buttonsCont);

        this.add(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
