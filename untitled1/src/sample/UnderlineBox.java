package sample;

import javafx.scene.control.CheckBox;


public class UnderlineBox extends CheckBox {
    WorkingArea workingArea;
    public UnderlineBox(WorkingArea workingArea){
        super("underline");
        this.workingArea = workingArea;
        this.setOnAction(e-> workingArea.changeUnderline());
    }
    public void requestFocus(){}
}