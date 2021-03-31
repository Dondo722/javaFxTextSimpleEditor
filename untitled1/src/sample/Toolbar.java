package sample;

import javafx.scene.layout.HBox;


public class Toolbar extends HBox {
    WorkingArea workingArea;
    TextSizeBox textSizeBox;
    TextFontBox fontBox;
    TextBoldBox boldBox;
    TextItalicBox italicBox;
    UnderlineBox underlineBox;
    public Toolbar(WorkingArea workingArea){
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
    public Toolbar(WorkingArea workingArea,TextFontBox fontBox,TextSizeBox textSizeBox,TextBoldBox boldBox,TextItalicBox italicBox, UnderlineBox underlineBox){
        super();
        super.setSpacing(8);
        this.workingArea = workingArea;
        this.textSizeBox = textSizeBox;
        this.fontBox = fontBox;
        this.boldBox = boldBox;
        this.italicBox = italicBox;
        this.underlineBox = underlineBox;
        super.getChildren().addAll(this.fontBox,this.textSizeBox,this.boldBox,this.italicBox,this.underlineBox);
    }
}
