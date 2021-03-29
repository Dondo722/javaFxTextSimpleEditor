package sample;

import javafx.scene.control.ChoiceBox;

public class TextSizeBox extends ChoiceBox<String> {
    WorkingArea workingArea;
    TextSizeBox(WorkingArea workingArea){
        super();
        this.workingArea = workingArea;
        this.getItems().addAll("16","18","20","40","60");
        this.setPrefWidth(70);
        this.setValue("16");
        this.setOnAction(e-> changeSize(this.getValue()));
    }
    public void requestFocus(){}
    public void changeSize(String string){
        workingArea.textSize = string;
        workingArea.changeFontSize();
    }
}
