package Controllers.widgets.charts;

import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ExpenseChart extends PieChart {
    public double total = 0;
    public static final double OPACITY = 0.4;
    public static final double SIZE = 800;
    private ArrayList<String> colors = new ArrayList<>();

    public ArrayList<String> getColors() {
        return colors;
    }
    public ExpenseChart() {
        setLegendVisible(false);
        //setLabelLineLength(10);
        setLabelsVisible(false);
        setTitle("Select category");
        //setLegendSide(Side.LEFT);
    }
    public void onTransluency(){
        for (PieChart.Data elem:this.getData()
        ) {
            elem.getNode().setOpacity(OPACITY);
        }
    }
    public void setColors(ArrayList<String> hexColors) {
        colors = hexColors;
        int i = 0;
        for (PieChart.Data data : this.getData()) {
            total+=data.getPieValue();
            data.getNode().setStyle("-fx-pie-color: " + hexColors.get(i) + ";");
            i++;
        }
    }
    public void highlight(String name){
        getData().forEach((data) ->
        {

            if(data.getName().equals(name)){
                //data.getNode().setEffect(null);
                data.getNode().setOpacity(1);
                int value = (int) ((data.getPieValue()/total)*100);
                //String percent = String.format("%.2f", value);
                setTitle(data.getName()+"["+value+"%]");
                return;
            }
            else{
                /*GaussianBlur blur = new GaussianBlur();
                blur.setRadius(5);
                data.getNode().setEffect(blur);*/
                data.getNode().setOpacity(OPACITY);
            }

        });
    }
    public void addHover(){
        onTransluency();
        getData().forEach((data) ->
        {
            data.getNode().setCursor(Cursor.HAND);
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        data.getNode().setOpacity(1);
                    });
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                        data.getNode().setOpacity(OPACITY);
                    });

        });
    }
    public static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }
    public static String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()) + format(value.getOpacity()))
                .toUpperCase();
    }
}
