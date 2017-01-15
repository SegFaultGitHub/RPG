package TilesetsManager;

import Game.Map.OldTilesets;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Thomas VENNER on 25/07/2016.
 */
public class ImagePanel extends JLabel implements MouseListener {
    private ImageIcon imageIcon;

    public ImagePanel() {
        super();
        addMouseListener(this);
    }

    public void setIcon(ImageIcon imageIcon) {
        super.setIcon(imageIcon);
        this.imageIcon = imageIcon;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / 32;
        int y = e.getY() / 32;
        int index = x + y * 8;
        String tileset = TilesetsManagerFrame.getTileset();
        new SetDirectionsFrame(tileset, index, imageIcon, OldTilesets.getDirections().get(tileset)[index]);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}