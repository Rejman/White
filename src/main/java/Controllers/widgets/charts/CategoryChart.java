package Controllers.widgets.charts;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.ArrayList;

public class CategoryChart extends ExpenseChart{
    private ArrayList<String> categoriesNames = new ArrayList<>();

    public ArrayList<String> getCategoriesNames() {
        return categoriesNames;

    }

    public CategoryChart() {
        setTitle("Categories");
    }
    public void pullData(String dataBaseUrl){
        ArrayList<String> colors = new ArrayList<>();
        ObservableList<Data> observableList = getData();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dataBaseUrl);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM categ");

            while(rs.next()) {

                String name = rs.getString("name");
                String color = rs.getString("color");
                float number = rs.getFloat("total_price");
                observableList.add(new PieChart.Data(name,number));
                Color colorColor = Color.web(color);
                categoriesNames.add(name);
                String webColor = toHexString(colorColor);
                colors.add(webColor);

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        setColors(colors);
        addHover();
    }
}
