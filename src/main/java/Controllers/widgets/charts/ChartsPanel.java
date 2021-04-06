package Controllers.widgets.charts;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class ChartsPanel extends VBox {
    private Map<String, ProductPriceChart> charts = new HashMap<>();
    private String dataBaseUrl;
    private CategoryChart categoryChart;

    public Map<String, ProductPriceChart> getCharts() {
        return charts;
    }

    public CategoryChart getCategoryChart() {
        return categoryChart;
    }

    public ChartsPanel(String dataBaseUrl) {
        this.dataBaseUrl = dataBaseUrl;
        categoryChart = new CategoryChart();
    }
    public void pullData(){
        categoryChart.pullData(dataBaseUrl);
        categoryChart.getData().forEach((data) ->
        {
            data.getNode().setCursor(Cursor.HAND);
            data.getNode().
                    addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        String categoryName = data.getName();
                        ProductPriceChart productPriceChart = charts.get(categoryName);
                        if(productPriceChart ==null){
                            productPriceChart = new ProductPriceChart();
                            productPriceChart.pullData(dataBaseUrl,categoryName);
                            charts.put(categoryName, productPriceChart);
                            System.out.println("dodano "+categoryName);
                        }


                        this.getChildren().clear();
                        this.getChildren().add(new Button("test"));
                        this.getChildren().add(productPriceChart);
                    });
        });
        this.getChildren().add(categoryChart);
    }
}
