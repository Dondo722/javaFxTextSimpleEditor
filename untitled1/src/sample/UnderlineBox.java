package sample;

import javafx.scene.control.CheckBox;


public class UnderlineBox extends CheckBox {
    public UnderlineBox(WorkingArea workingArea){
        super("underline");
        this.setOnAction(e-> workingArea.changeUnderline());
    }
    public void requestFocus(){}
}