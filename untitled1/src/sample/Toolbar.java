package sample;

import javafx.scene.layout.HBox;


public class Toolbar extends HBox {
    WorkingArea workingArea;
    TextSizeBox textSizeBox;
    TextFontBox fontBox;
    TextBoldBox boldBox;
    TextItalicBox italicBox;
    UnderlineBox underlineBox;
    Toolbar(WorkingArea workingArea){
        super();
        super.setSpacing(8);
        this.workingArea = workingArea;
        textSizeBox = new TextSizeBox(workingArea);
        fontBox = new TextFontBox(workingArea);
        boldBox = new TextBoldBox(workingArea);
        italicBox = new TextItalicBox(workingArea);
        underlineBox = new UnderlineBox(workingArea);
        super.getChildren().addAll(fontBox,textSizeBox,boldBox,italicBox,underlineBox);
    }
}
