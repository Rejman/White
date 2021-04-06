package Controllers.widgets.charts;

import Controllers.widgets.SelectBox;
import Controllers.widgets.inputPanels.Panel;
import Models.Category;
import Utils.ModelStructure;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.sql.*;
import java.util.ArrayList;

public class ChartPanel extends Panel<Category> {
    private ExpenseChart expenseChart = new ExpenseChart();
    private SelectBox<Category> selectBox = new SelectBox<>();
    private ArrayList<String> categoriesNames = new ArrayList<>();
    private HBox hBox = new HBox();

    public ArrayList<String> getCategoriesNames() {
        return categoriesNames;

    }

    public ChartPanel(ModelStructure modelStructure) {
        super("",new Image("icons/shop-24.png"), modelStructure);
        this.setMaxWidth(600);
        this.setMinWidth(600);
        //getChildren().add(hBox);
        selectBox.addData(modelStructure.getCategories());
        hBox.getChildren().add(selectBox);
        pullData("database/rain.db");
        hBox.getChildren().add(expenseChart);


        selectBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                Category selectedCategory = (Category) selectBox.getSelectedItem();
                if (selectedCategory == null) {
                    System.out.println("null");
                } else {
                    System.out.println("not null");
                }
            }
        }));

    }
    public void pullData(String dataBaseUrl){
        ArrayList<String> colors = new ArrayList<>();
        ObservableList<PieChart.Data> observableList = expenseChart.getData();
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
                String webColor = ExpenseChart.toHexString(colorColor);
                colors.add(webColor);

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        expenseChart.setColors(colors);
        expenseChart.addHover();
    }
}
