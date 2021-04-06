package Utils;

import java.io.*;
import java.util.Queue;


public class Settings implements Serializable{

    //preferences
    public double colorIntense = 0.5;
    //logo
    public int logoWidth = 100;
    public int logoHeight = 100;
    public int logoTextSize = 36;
    public String recentDB = null;

    public Settings(){
    }

}
