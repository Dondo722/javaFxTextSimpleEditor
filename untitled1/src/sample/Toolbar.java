package sample;

import javafx.scene.layout.HBox;


public class Toolbar extends HBox {
    private final WorkingArea workingArea;
    private final TextFontBox fontBox;
    private final TextSizeBox textSizeBox;
    private final TextBoldBox boldBox;
    private final TextItalicBox italicBox;
    private final UnderlineBox underlineBox;
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

    public WorkingArea getWorkingArea() {
        return workingArea;
    }

    public TextFontBox getFontBox() {
        return fontBox;
    }

    public TextSizeBox getTextSizeBox() {
        return textSizeBox;
    }

    public TextBoldBox getBoldBox() {
        return boldBox;
    }

    public TextItalicBox getItalicBox() {
        return italicBox;
    }

    public UnderlineBox getUnderlineBox() {
        return underlineBox;
    }
}
