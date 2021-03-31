package sample;

import javafx.scene.control.ChoiceBox;

public class TextSizeBox extends ChoiceBox<String> {
    WorkingArea workingArea;
    public TextSizeBox(WorkingArea workingArea){
        super();
        this.workingArea = workingArea;
        for(int i = 12; i < 45; i+=4){
            this.getItems().add(String.valueOf(i));
        }
        this.setPrefWidth(70);
        this.setValue("16");
        this.setOnAction(e-> workingArea.changeSize(this.getValue()));
    }
    public void requestFocus(){}

}
