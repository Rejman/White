package Controllers.widgets.inputPanels;

import Models.Category;
import Models.Expense;
import Models.Name;
import Models.Unit;
import Utils.ModelStructure;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashSet;

public class NamePanel extends Panel<Name>{
    private CategoryPanel categoryPanel;
    private Button button = new Button("Save name");
    private Label nameOfName = new Label();
    private ArrayList<Name> newNames = new ArrayList<>();
    private UnitPanel unitPanel = null;
    public ArrayList<Category> getNewCategories(){
        return categoryPanel.getNewCategories();
    }
    public ArrayList<Name> getNewNames() {
        return newNames;
    }
    public static final String iconURL = "icons/open-parcel-24.png";
    public void setUnitPanel(UnitPanel unitPanel) {
        this.unitPanel = unitPanel;
    }

    protected void saveName() {
        long id = -(newNames.size() + 1);
        String nameOfName = selectBox.getText();
        Category category = (Category) categoryPanel.selectBox.getSelectedItem();

        Name newName = new Name(id, nameOfName, category);
        newNames.add(newName);
        selectBox.setSelectedItem(newName);
        modelStructure.addNewName(newName);
        setTextInExternalLabel(nameOfName);
    }
    public void disable(){
        if(categoryPanel!=null){
            categoryPanel.disable();
        }
        this.setVisible(false);
        this.setDisable(true);
    }

    public void setNextPanel(Called nextPanel) {
        this.nextPanel = nextPanel;
        if(categoryPanel!=null){
            this.categoryPanel.setNextPanel(nextPanel);
        }
    }
    public NamePanel(ModelStructure modelStructure) {
        super("Select or enter expense:",new Image(iconURL), modelStructure);
        categoryPanel = new CategoryPanel(modelStructure);
        categoryPanel.namePanel = this;
        //categoryPanel.setNextPanel(nextPanel);
        selectBox.addData(modelStructure.getNames());

        this.getChildren().add(0,categoryPanel);
        changeVisibility(categoryPanel,startView);

        selectBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                Name selectedName = (Name) selectBox.getSelectedItem();
                HashSet<Unit> dedicatedUnits = new HashSet<>();
                for (Expense expense : modelStructure.getExpenses()) {
                    if (expense.getName().equals(selectedName)) {
                        dedicatedUnits.add(expense.getUnit());
                    }
                }
                if(unitPanel!=null){
                    unitPanel.selectBox.setDedicatedItems(dedicatedUnits);
                }
                if (selectedName == null) {
                    categoryPanel.header.setText("Select or enter category for " + selectBox.getText());
                    changeVisibility(startView, categoryPanel);
                    categoryPanel.selectBox.getSearchTextField().requestFocus();
                } else {
                    setTextInExternalLabel(selectedName.getName());
                    showNextPanel();
                }
            }
        }));
    }
    @Override
    public void reset() {
        super.reset();
        categoryPanel.reset();
        selectBox.addData(modelStructure.getNames());
        changeVisibility(categoryPanel,startView);
    }
}
