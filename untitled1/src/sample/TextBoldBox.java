package sample;

import javafx.scene.control.CheckBox;


public class TextBoldBox extends CheckBox {
    public TextBoldBox(WorkingArea workingArea){
        super("bold");
        this.setOnAction(e -> workingArea.changeFontWeight());
    }
    public void requestFocus(){}
}