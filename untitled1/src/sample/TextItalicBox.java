package sample;

import javafx.scene.control.CheckBox;



public class TextItalicBox extends CheckBox {
    WorkingArea workingArea;
    public TextItalicBox(WorkingArea workingArea){
        super("italic");
        this.workingArea = workingArea;
        this.setOnAction(e -> workingArea.changeFontPosture());
    }
    public void requestFocus(){}
}