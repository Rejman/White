package Controllers.widgets.inputPanels;

import Controllers.widgets.ToolPane;
import Utils.ModelStructure;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class SeparateSourcePanel extends SourcePanel{
    private ToolPane toolPane;
    public SeparateSourcePanel(ModelStructure modelStructure, ToolPane toolPane) throws IOException {
        super(modelStructure, null);
        this.toolPane = toolPane;
    }

    @Override
    protected void showNextPanel() {
        if(selectBox.getSelectedItem().getId()<0){
            System.out.println("nowy: ");
            System.out.println(getNewSource());
            selectBox.unselect();
            selectBox.setSelectedItem(getNewSource());
        }
        //this.selectBox.setSelectedItem(getNewSource());
        toolPane.close();
    }

}
