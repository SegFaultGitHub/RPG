package Config;

import Utils.Utils;
import org.json.simple.JSONObject;

/**
 * Created by Thomas VENNER on 24/10/2016.
 */
public class Config {
    public static boolean DEBUG = false;

    public static void initialize() {
        JSONObject jsonObject = Utils.readJSON("config/config.json");
        DEBUG = (long) jsonObject.get("DEBUG") != 0;
    }
}
