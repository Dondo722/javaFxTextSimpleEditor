package sample;

import javafx.scene.control.CheckBox;



public class TextItalicBox extends CheckBox {
    public TextItalicBox(WorkingArea workingArea){
        super("italic");
        this.setOnAction(e -> workingArea.changeFontPosture());
    }
    public void requestFocus(){}
}