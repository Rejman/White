package Controllers.widgets.inputPanels;

import Controllers.Controller;
import Models.Unit;
import Utils.Blocker;
import Utils.ModelStructure;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class UnitPanel extends Panel<Unit>{
    private VBox newUnitPanel = new VBox();
    private TextField shortcutTextField = new TextField();
    private CheckBox isRealNumberCheckBox = new CheckBox("real number");
    private Button button = new Button("Save unit");
    private Label unitName = new Label();
    private ArrayList<Unit> newUnits = new ArrayList<>();
    private PricePanel pricePanel = null;
    public static final String iconURL = "icons/scales-24.png";

    public ArrayList<Unit> getNewUnits() {
        return newUnits;
    }

    public void setPricePanel(PricePanel pricePanel) {
        this.pricePanel = pricePanel;
    }

    public UnitPanel(ModelStructure modelStructure) {
        super("Select or enter unit:",new Image(iconURL), modelStructure);
        selectBox.addData(modelStructure.getUnits());
        Blocker.blockButton(button, shortcutTextField, modelStructure.getNamesAndShortcuts(), new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "";
            }
        });
        newUnitPanel.getChildren().add(unitName);
        newUnitPanel.getChildren().add(shortcutTextField);
        newUnitPanel.getChildren().add(isRealNumberCheckBox);
        newUnitPanel.getChildren().add(button);
        newUnitPanel.setSpacing(5);
        this.getChildren().add(newUnitPanel);
        changeVisibility(newUnitPanel,startView);
        selectBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                Unit selectedUnit = (Unit) selectBox.getSelectedItem();
                if (selectedUnit == null) {
                    unitName.setText("Set shortcut for " + selectBox.getText()+":");
                    unitName.setGraphic(new ImageView(new Image(iconURL)));
                    changeVisibility(startView, newUnitPanel);
                    shortcutTextField.requestFocus();
                } else {
                    setExteranalNodes(selectedUnit);
                    showNextPanel();
                }
            }
        }));
        button.setOnAction(event->{
            long id = -(newUnits.size() + 1);
            String name = selectBox.getText();
            String shortcut = shortcutTextField.getText();
            if (shortcut.length() == 0) shortcut = null;
            boolean realNumber = isRealNumberCheckBox.isSelected();
            Unit newUnit = new Unit(id, name, shortcut, realNumber);

            modelStructure.addNewUnit(newUnit);
            selectBox.setSelectedItem(newUnit);
            newUnits.add(newUnit);
            setExteranalNodes(newUnit);
            showNextPanel();
        });
    }
    private void setExteranalNodes(Unit unit){
        if(pricePanel!=null){
            String shortcut = unit.getShortcut();
            if(shortcut==null){
                shortcut = unit.getName();
            }
            setTextInExternalLabel("["+shortcut+"]");
            pricePanel.setUnitName(shortcut);
            pricePanel.setQuantityFieldType(unit.isRealNumber());
        }
    }
    @Override
    public void reset() {
        super.reset();
        shortcutTextField.clear();
        isRealNumberCheckBox.setSelected(false);
        selectBox.addData(modelStructure.getUnits());
        changeVisibility(newUnitPanel,startView);
    }
}
