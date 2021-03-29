package sample;

import javafx.scene.control.CheckBox;
import javafx.scene.text.FontPosture;


public class TextItalicBox extends CheckBox {
    WorkingArea workingArea;
    TextItalicBox(WorkingArea workingArea){
        super("italic");
        this.workingArea = workingArea;
        this.setOnAction(e-> changeFontPosture());
    }
    public void requestFocus(){}
    public void changeFontPosture(){
        if(workingArea.fontPosture == FontPosture.REGULAR)
            workingArea.fontPosture = FontPosture.ITALIC;
        else workingArea.fontPosture = FontPosture.REGULAR;
        workingArea.changeFontPosture();
    }
}