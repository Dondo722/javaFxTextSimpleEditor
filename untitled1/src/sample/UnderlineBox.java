package sample;

import javafx.scene.control.CheckBox;


public class UnderlineBox extends CheckBox {
    WorkingArea workingArea;
    UnderlineBox(WorkingArea workingArea){
        super("underline");
        this.workingArea = workingArea;
        this.setOnAction(e-> changeUnderline());
    }
    public void requestFocus(){}
    public void changeUnderline(){
        workingArea.underline = !workingArea.underline;
        workingArea.changeUnderline();
    }
}