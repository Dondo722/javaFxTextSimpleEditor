package sample;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;


public class Toolbar extends HBox {
    WorkingArea workingArea;
    ComboBox<String> textSizeBox = new ComboBox<>(){public void requestFocus(){}};
    ChoiceBox<String> fontBox = new ChoiceBox<>(){public void requestFocus(){}};
    CheckBox boldBox = new CheckBox("bold "){public void requestFocus(){}};
    CheckBox italicBox = new CheckBox("italic "){public void requestFocus(){}};
    CheckBox underlineBox = new CheckBox("underline"){public void requestFocus(){}};
    Toolbar(WorkingArea workingArea){
        super();
        super.setSpacing(8);
        this.workingArea = workingArea;
        fontBox.setFocusTraversable(false);
        textSizeBox.setFocusTraversable(false);
        fontBox.getItems().addAll(Font.getFontNames());
        fontBox.setPrefWidth(180);
        fontBox.setValue("Times New Roman");
        fontBox.getSelectionModel().selectedItemProperty().addListener((v,oldValue,newValue) -> changeFont(newValue));
        textSizeBox.getItems().addAll(
                "16",
                "18",
                "20",
                "40",
                "60"
        );
        boldBox.setOnAction(e -> changeBold());
        italicBox.setOnAction(e -> changeItalic());
        underlineBox.setOnAction(e -> changeUnderline());

        textSizeBox.setPrefWidth(70);
        textSizeBox.setValue("16");
        textSizeBox.setEditable(true);
        textSizeBox.setOnAction(e-> changeSize(textSizeBox.getValue()));
        super.getChildren().addAll(fontBox,textSizeBox,boldBox,italicBox,underlineBox);
    }

    public void changeFont(String newValue){
        workingArea.textFont = newValue;
        workingArea.changeFont();
    }
    public void changeSize(String string){
        workingArea.textSize = string;
        workingArea.changeFont();
    }
    public void changeBold(){
        workingArea.bold = !workingArea.bold;
        workingArea.changeBold();
    }
    public void changeItalic(){
        workingArea.italic = !workingArea.italic;
        workingArea.changeItalic();
    }
    public void changeUnderline(){
        workingArea.underline = !workingArea.underline;
        workingArea.changeUnderline();
    }

}
