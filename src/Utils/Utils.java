package Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thomas VENNER on 18/07/2016.
 */
public class Utils {
    private static Random random = new Random();

    public static JSONObject readJSON(String path) {
        JSONParser jsonParser = new JSONParser();
        Object obj;

        try {
            obj = jsonParser.parse(new FileReader(path));
        } catch (Exception e) {
            return null;
        }

        return (JSONObject) obj;
    }

    public static String stringRepeat(String s, int n) {
        n = Math.max(0, n);
        return new String(new char[n]).replace("\0", s);
    }

    public static String reformatJSON(JSONObject jsonObject, int tab) {
        String result = stringRepeat(" ", 4 * (tab)) + "{\n";
        boolean first = true;
        for (Object obj : jsonObject.keySet()) {
            if (!first) {
                result += ",\n";
            }
            first = false;
            String key = obj.toString();
            Object value = jsonObject.get(key);
            result += stringRepeat(" ", 4 * (tab + 1)) + "\""+ key + "\" : ";
            if (value.getClass() == JSONObject.class) {
                result += reformatJSON((JSONObject) value, tab + 1);
            } else if (value.getClass() == JSONArray.class) {
                result += "[\n";
                boolean first2 = true;
                for (Object obj2 : (JSONArray) value) {
                    if (!first2) {
                        result += ",\n";
                    }
                    first2 = false;
                    if (obj2.getClass() == JSONObject.class) {
                        result += reformatJSON((JSONObject) obj2, tab + 2);
                    } else if (obj2.getClass() == String.class) {
                        String value2 = (String) obj2;
                        result += stringRepeat(" ", 4 * (tab + 2)) + '"' + value2 + '"';
                    }
                }
                result += "\n" + stringRepeat(" ", 4 * (tab + 1)) + "]";
            } else if (value.getClass() == String.class) {
                result += "\"" + value.toString() + "\"";
            } else {
                result += value.toString();
            }
        }
        result += "\n" + stringRepeat(" ", tab * 4) + "}";
        return result;
    }

    public static int constrain(int n, int min, int max) {
        return Math.max(Math.min(max, n), min);
    }

    public static float constrain(float n, float min, float max) {
        return Math.max(Math.min(max, n), min);
    }

    public static <T> String implode(T[] array, String s) {
        String result = "";
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                result += s;
            }
            result += array[i].toString();
        }
        return result;
    }

    public static String getRandomString(int n) {
        ArrayList<Character> chars = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            chars.add(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            chars.add(c);
        }
        for (char c = '0'; c <= '9'; c++) {
            chars.add(c);
        }
        String result = "";
        for (int i = 0; i < n; i++) {

            result += chars.get(Math.abs(random.nextInt()) % chars.size());
        }
        return result;
    }
}
