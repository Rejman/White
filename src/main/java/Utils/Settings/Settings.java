package Utils.Settings;

import java.io.Serializable;


public class Settings implements Serializable {

    //preferences
    public double colorIntense = 0.5;
    //logo
    public int logoWidth = 100;
    public int logoHeight = 100;
    public int logoTextSize = 36;
    public LimitedSet recentDBs = new LimitedSet(10);
    
    public Settings() {
    }

    public static String cutPath(String url) {
        int length = url.length();
        int id = url.lastIndexOf("\\");
        return url.substring(id + 1, length - 3);
    }
}
