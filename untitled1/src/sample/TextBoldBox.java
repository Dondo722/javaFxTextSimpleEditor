package sample;

import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class TextBoldBox extends CheckBox {
    WorkingArea workingArea;
    TextBoldBox(WorkingArea workingArea){
        super("bold");
        this.workingArea = workingArea;
        this.setOnAction(e -> changeFontWeight());
    }
    public void requestFocus(){}
    public void changeFontWeight(){
        if(workingArea.fontWeight == FontWeight.NORMAL)
            workingArea.fontWeight = FontWeight.BOLD;
        else workingArea.fontWeight = FontWeight.NORMAL;
        workingArea.changeFontWeight();
    }
}