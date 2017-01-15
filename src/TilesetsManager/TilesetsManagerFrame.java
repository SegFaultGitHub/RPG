package TilesetsManager;

import Game.Map.OldTilesets;
import Utils.Utils;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Thomas VENNER on 25/07/2016.
 */
public class TilesetsManagerFrame extends JFrame {
    private ImagePanel imagePanel;
    private JPanel mainPanel;
    private static String tileset;
    public static String getTileset() {
        return tileset;
    }

    public TilesetsManagerFrame() throws IOException {
        OldTilesets.initialize();
        mainPanel = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(e -> openFile());
        menu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(e -> save());
        menu.add(saveItem);
        menuBar.add(menu);
        imagePanel = new ImagePanel();

        setJMenuBar(menuBar);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollPane);
        this.pack();
        this.setSize(256 + 17, 55);
        this.setTitle("OldTilesets Manager");
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./graphics/tilesets"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
        fileChooser.showOpenDialog(null);
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileChooser.getSelectedFile().toString()));
            tileset = fileChooser.getSelectedFile().getName();
        } catch (Exception e) {
            System.err.println("Cannot open image.");
            System.exit(1);
        }
        try {
            mainPanel.remove(imagePanel);
        } catch (Exception ignored) { }
        imagePanel.setIcon(new ImageIcon(image));
        mainPanel.add(imagePanel);
        imagePanel.setIcon(new ImageIcon(image));
        this.setSize(271, 850);
        super.repaint();
        this.repaint();
        imagePanel.repaint();
        this.setLocationRelativeTo(null);
    }

    public void save() {
        JSONObject jsonObject = new JSONObject();
        File tilesetsFolder = new File("graphics/tilesets");
        for (String fileName : tilesetsFolder.list()) {
            if (fileName.endsWith(".png")) {
                HashMap<OldTilesets.Direction, Boolean>[] directions = OldTilesets.getDirections().get(fileName);
                JSONObject tileset = new JSONObject();
                for (int i = 0; i < directions.length; i++) {
                    JSONObject directionsJson = new JSONObject();
                    boolean up = directions[i].get(OldTilesets.Direction.up);
                    boolean down = directions[i].get(OldTilesets.Direction.down);
                    boolean right = directions[i].get(OldTilesets.Direction.right);
                    boolean left = directions[i].get(OldTilesets.Direction.left);
                    if (left && right && down && up) {
                        continue;
                    }
                    directionsJson.put("up", up);
                    directionsJson.put("down", down);
                    directionsJson.put("left", left);
                    directionsJson.put("right", right);
                    tileset.put(String.valueOf(i), directionsJson);
                }
                jsonObject.put(fileName, tileset);
            }
        }
        String string = Utils.reformatJSON(jsonObject, 0);
        try {
            FileWriter fileWriter = new FileWriter("datas/tilesets.json");
            fileWriter.write(string);
            fileWriter.close();
            JFrame frame = new JFrame();
            frame.add(new JLabel("Save done."));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
