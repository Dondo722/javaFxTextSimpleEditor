package sample;

import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;


public class TextFontBox extends ChoiceBox<String> {
    WorkingArea workingArea;
    public TextFontBox(WorkingArea workingArea){
        super();
        this.workingArea = workingArea;
        this.getItems().addAll(Font.getFontNames());
        this.setPrefWidth(180);
        this.setValue("Times New Roman");
        this.setOnAction(e -> workingArea.changeFont(this.getValue()));
    }
    public void requestFocus(){}
}
