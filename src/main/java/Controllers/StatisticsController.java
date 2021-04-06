package Controllers;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import Controllers.widgets.SearchBox;
import Controllers.widgets.charts.ExpenseChart;
import Dao.Stats;
import Models.Category;
import Utils.DateConvertor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class StatisticsController extends Controller{
    private SearchBox<Category> searchBox = new SearchBox<>();
    private ExpenseChart expenseChart = new ExpenseChart();
    HashSet<Category> detected = new HashSet<>();

    @FXML
    private HBox hBox;
    @FXML
    void initialize() throws ParseException {
        Stats stats = new Stats("database/rain.db");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020,01,01);
        //Date today = calendar.getTime();
        Date today = new Date();
        System.out.println(today);
        System.out.println(DateConvertor.toLong(today));
        HashSet<Category> categories = stats.getCategories(DateConvertor.toLong(today),DateConvertor.toLong(today));

        searchBox.addData(categories);
        hBox.getChildren().add(0,searchBox);
        hBox.getChildren().add(expenseChart);
        //pullData("database/rain.db");
        //reaction on select model from searchBox
        searchBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println(newSelection.getName());
                expenseChart.highlight(newSelection.getName());
            } else {
                expenseChart.onTransluency();
            }
        });
        expenseChart.getData().forEach((data) ->
        {
            data.getNode().setCursor(Cursor.HAND);
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                        data.getNode().setOpacity(1);
                        //searchBox.getSearchTextField().setText(data.getName());
                        searchBox.getSearchTextField().setText("");
                        detected.forEach((elem) ->{
                            if(elem.getName().equals(data.getName())){
                                searchBox.getSelectionModel().select(elem);
                                return;
                            }
                        });

                    });
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                        //data.getNode().setOpacity(ExpenseChart.OPACITY);
                        searchBox.getSearchTextField().setText("");
                    });

        });

    }
    private void pullData(String dataBaseUrl) {
        ArrayList<String> colors = new ArrayList<>();
        ObservableList<PieChart.Data> observableList = expenseChart.getData();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dataBaseUrl);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM categories_stat");
            while (rs.next()) {

                long id = rs.getLong("id");
                String name = rs.getString("name");
                String color = rs.getString("color");
                float number = rs.getFloat("total_price");
                Category category = new Category(id,name,color);
                observableList.add(new PieChart.Data(name, number));
                Color colorColor = Color.web(color);
                detected.add(category);
                String webColor = ExpenseChart.toHexString(colorColor);
                colors.add(webColor);

            }
            connection.close();
            searchBox.setDedicatedItems(detected);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        expenseChart.setColors(colors);
        expenseChart.onTransluency();
       // expenseChart.addHover();
    }

}
