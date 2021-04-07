package Utils;

import javafx.scene.paint.Color;

public class ColorConvertor {
    public static final double BORDER = 0.5;
    public static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }
    public static String format(int val) {
        String in = Integer.toHexString(val);
        return in.length() == 1 ? "0" + in : in;
    }
    public static String toHEXString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()))
                .toUpperCase();
    }
    public static String toHEXString(int red, int green, int blue) {
        return "#" + (format(red) + format(green) + format(blue))
                .toUpperCase();
    }
    public static String toHEXString(String value) {
        Color color = Color.web(value);
        return toHEXString(color);
    }
    public static String toRGBAString(Color color, double alpha) {

        return "rgba("+color.getRed()*255+","+color.getGreen()*255+","+color.getBlue()*255+","+alpha+")";

    }
    public static String toRGBAString(String hexColor, double alpha) {
        Color color = Color.web(hexColor);
        return toRGBAString(color,alpha);

    }

    public static Color getContrastColor(Color color) {
        double blue = color.getBlue();
        double green = color.getGreen();
        double red = color.getRed();
        double sum = blue+green+red;

        if(sum>(BORDER*3)) return Color.BLACK;
        else return Color.WHITE;
    }
    public static Color getContrastColor(String hexColor) {
        Color color = Color.web(hexColor);
        return  getContrastColor(color);
    }
    public static String toHEXString(Color value, double alpha, Color bgColor){
        Color fgColor = value;

        double r = (1-alpha) * bgColor.getRed() + alpha * fgColor.getRed();
        double g = (1-alpha) * bgColor.getGreen() + alpha * fgColor.getGreen();
        double b = (1-alpha) * bgColor.getBlue() + alpha * fgColor.getBlue();
        Color newColor = new Color(r,g,b,1);
        return toHEXString(newColor);
    }
    public static String toHEXString(Color value, double alpha){
        Color bgColor = Color.WHITE;
        Color fgColor = value;

        double r = (1-alpha) * bgColor.getRed() + alpha * fgColor.getRed();
        double g = (1-alpha) * bgColor.getGreen() + alpha * fgColor.getGreen();
        double b = (1-alpha) * bgColor.getBlue() + alpha * fgColor.getBlue();
        Color newColor = new Color(r,g,b,1);
        return toHEXString(newColor);
    }
    public static String toHEXString(String value, double alpha) {
        Color color = Color.web(value);
        return toHEXString(color, alpha);
    }
    public static String toHEXString(String value, double alpha, Color bgColor) {
        Color color = Color.web(value);
        return toHEXString(color, alpha, bgColor);
    }
}
