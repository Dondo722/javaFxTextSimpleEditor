package sample;

import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;


public class TextFontBox extends ChoiceBox<String> {
    public TextFontBox(WorkingArea workingArea){
        super();
        int BoxWidth = 180;
        String defaultFont = "Times New Roman";
        this.getItems().addAll(Font.getFontNames());
        this.setPrefWidth(BoxWidth);
        this.setValue(defaultFont);
        this.setOnAction(e -> workingArea.changeFont(this.getValue()));
    }
    public void requestFocus(){}
}
