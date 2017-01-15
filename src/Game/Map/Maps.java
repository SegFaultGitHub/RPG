package Game.Map;

import Events.Command;
import Events.Event;
import Events.MessageCommand;
import Utils.Utils;

import Window.WindowText;
import org.jsfml.graphics.Color;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Thomas VENNER on 17/07/2016.
 */

public class Maps {
    private static HashMap<String, Map> maps;

    public static HashMap<String, Map> getMaps() {
        return maps;
    }

    public static void initialize(int screenWidth, int screenHeight) throws Exception {
        maps = new HashMap<>();
        JSONObject jsonObject = Utils.readJSON("datas/maps.json");

        for (Object obj : (JSONArray) jsonObject.get("maps")) {
            JSONObject mapJson = (JSONObject) obj;
            String name = mapJson.get("name").toString();
            String tileset = mapJson.get("tileset").toString();
            long width = (long) mapJson.get("width");
            long height = (long) mapJson.get("height");

            boolean[][] walkArray = new boolean[(int) width][(int) height];
            JSONArray contentJson = (JSONArray) mapJson.get("walk");
            for (int j = 0; j < height; j++) {
                String[] line = contentJson.get(j).toString().split(" ");
                for (int i = 0; i < width; i++) {
                    boolean walk = line[i].equals("1");
                    walkArray[i][j] = walk;
                }
            }
            Cell[][] cells = new Cell[(int) width][(int) height];
            contentJson = (JSONArray) mapJson.get("content");
            for (int j = 0; j < height; j++) {
                String[] line = contentJson.get(j).toString().split(" ");
                for (int i = 0; i < width; i++) {
                    List<String> contentStrings = Arrays.asList(line[i].split("/"));
                    ArrayList<Integer> content = new ArrayList<>();
                    for (String c : contentStrings) {
                        content.add(Integer.parseInt(c));
                    }
                    cells[i][j] = new Cell(content, walkArray[i][j]);
                }
            }

            ArrayList<Event> events = new ArrayList<>();
            JSONArray eventsJson = (JSONArray) mapJson.get("events");
            if (eventsJson != null) {
                for (int i = 0; i < eventsJson.size(); i++) {
                    JSONObject eventJson = (JSONObject) eventsJson.get(i);
                    long x = (long) eventJson.get("x");
                    long y = (long) eventJson.get("y");
                    Object appearance = eventJson.get("appearance");
                    Event event = new Event((int) x, (int) y);
                    ArrayList<Command> commands = new ArrayList<>();
                    JSONArray commandsJson = (JSONArray) eventJson.get("commands");
                    for (int j = 0; j < commandsJson.size(); j++) {
                        JSONObject commandJson = (JSONObject) commandsJson.get(j);
                        String type = commandJson.get("type").toString();
                        if (type.equals("message")) {
                            Color windowColor = Color.BLACK;
                            if (commandJson.containsKey("windowColor")) {
                                String[] color = commandJson.get("windowColor").toString().split(",");
                                windowColor = new Color(
                                        Integer.parseInt(color[0]),
                                        Integer.parseInt(color[1]),
                                        Integer.parseInt(color[2])
                                );
                            }
                            Color fontColor = Color.WHITE;
                            if (commandJson.containsKey("fontColor")) {
                                String[] color = commandJson.get("fontColor").toString().split(",");
                                fontColor = new Color(
                                        Integer.parseInt(color[0]),
                                        Integer.parseInt(color[1]),
                                        Integer.parseInt(color[2])
                                );
                            }
                            int windowWidth = 670;
                            if (commandJson.containsKey("width")) {
                                windowWidth = Integer.parseInt(commandJson.get("width").toString());
                            }
                            boolean lockSize = false;
                            if (commandJson.containsKey("lockSize")) {
                                lockSize = (boolean) commandJson.get("lockSize");
                            }
                            WindowText.Position position = WindowText.Position.bottom;
                            if (commandJson.containsKey("position")) {
                                String positionStr = commandJson.get("position").toString();
                                if (positionStr.equals("top")) {
                                    position = WindowText.Position.top;
                                } else if (positionStr.equals("center")) {
                                    position = WindowText.Position.center;
                                }
                            }
                            commands.add(new MessageCommand(
                                    event,
                                    windowColor,
                                    windowWidth,
                                    commandJson.get("text").toString(),
                                    lockSize,
                                    position,
                                    fontColor,
                                    screenWidth,
                                    screenHeight
                            ));
                        }
                        event.setCommands(commands);
                        events.add(event);
                    }
                }
            }

            maps.put(name, new Map(name, Tilesets.get(tileset), cells, (int) width, (int) height, events));
        }
    }

    public static Map get(String name) {
        return maps.get(name);
    }
}
