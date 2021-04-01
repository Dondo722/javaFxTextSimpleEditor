package sample;

import javafx.scene.layout.VBox;

public class TextEditorMenu extends VBox {
    TextEditorMenu(WorkingArea workingArea){
        super();
        Toolbar toolbar = new Toolbar(workingArea);
        TopMenu topMenu = new TopMenu(workingArea, toolbar);
        super.getChildren().addAll(topMenu,toolbar);
    }
}
