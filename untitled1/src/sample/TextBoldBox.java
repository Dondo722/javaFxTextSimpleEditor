package sample;

import javafx.scene.control.CheckBox;


public class TextBoldBox extends CheckBox {
    WorkingArea workingArea;
    public TextBoldBox(WorkingArea workingArea){
        super("bold");
        this.workingArea = workingArea;
        this.setOnAction(e -> workingArea.changeFontWeight());
    }
    public void requestFocus(){}
}