package sample;

import javafx.scene.control.ChoiceBox;

public class TextSizeBox extends ChoiceBox<String> {
    public TextSizeBox(WorkingArea workingArea){
        super();
        int minTextSize = 12;
        int textChangeOn = 4;
        int maxTextSize = 45;
        int width = 70;
        String defaultSize = "16";
        for(int i = minTextSize; i < maxTextSize; i+=textChangeOn){
            this.getItems().add(String.valueOf(i));
        }
        this.setPrefWidth(width);
        this.setValue(defaultSize);
        this.setOnAction(e-> workingArea.changeSize(this.getValue()));
    }
    public void requestFocus(){}

}
