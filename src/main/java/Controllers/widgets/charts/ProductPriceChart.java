package Controllers.widgets.charts;

import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.sql.*;

public class ProductPriceChart extends CategoryChart{

    protected void pullData(String dataBaseUrl, String categoryName) {
        setTitle(categoryName);
        ObservableList<Data> observableList = getData();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:"+dataBaseUrl);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM details2 WHERE category='"+categoryName+"';");

            while(rs.next()) {

                String name = rs.getString("name");
                float number = rs.getFloat("total_price");
                observableList.add(new PieChart.Data(name,number));

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        addHover();
    }
}
