package Controllers.widgets.inputPanels;

import Models.Category;

import Utils.ModelStructure;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class CategoryPanel extends Panel<Category> {
    private VBox newCategoryPanel = new VBox();
    private ColorPicker colorPicker = new ColorPicker();
    private Button button = new Button("Save category");
    private Label categoryName = new Label();
    private ArrayList<Category> newCategories = new ArrayList<>();
    protected NamePanel namePanel = null;
    public static final String iconURL = "icons/categorize-24.png";
    public ArrayList<Category> getNewCategories() {
        return newCategories;
    }

    public CategoryPanel(ModelStructure modelStructure) {
        super("",new Image(iconURL), modelStructure);
        getStyleClass().add("category-panel");
        //padding correction because this is new Pane in NamePane
        //setPadding(new Insets(0,5,0,5));
        //setPadding(new Insets(0));
        selectBox.addData(modelStructure.getCategories());
        newCategoryPanel.getChildren().add(categoryName);
        newCategoryPanel.getChildren().add(colorPicker);
        newCategoryPanel.getChildren().add(button);
        //newCategoryPanel.setSpacing(5);
        this.getChildren().add(newCategoryPanel);
        changeVisibility(newCategoryPanel,startView);

        selectBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                Category selectedCategory = (Category) selectBox.getSelectedItem();
                if (selectedCategory == null) {
                    categoryName.setText("Select color for "  + selectBox.getText()+":");
                    categoryName.setGraphic(new ImageView(new Image(iconURL)));
                    changeVisibility(startView, newCategoryPanel);
                    colorPicker.requestFocus();
                } else {
                    if(namePanel!=null){
                        namePanel.saveName();
                    }
                    showNextPanel();
                }
            }
        }));
        button.setOnAction(event->{
            long id = -(newCategories.size() + 1);
            String name = selectBox.getText();
            String color = colorPicker.getValue().toString();
            Category newCategory = new Category(id, name, color);
            modelStructure.addNewCategory(newCategory);
            selectBox.setSelectedItem(newCategory);

            newCategories.add(newCategory);
            if(namePanel!=null){
                namePanel.saveName();
            }
            showNextPanel();
        });
    }
    @Override
    public void reset() {
        super.reset();
        selectBox.addData(modelStructure.getCategories());
        changeVisibility(newCategoryPanel,startView);
    }
    public void enable(){
        this.setVisible(true);
        this.setDisable(false);
        colorPicker.setValue(Color.BLACK);
        selectBox.getSearchTextField().requestFocus();
    }
}
