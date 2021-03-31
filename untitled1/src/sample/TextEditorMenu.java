package sample;

import javafx.scene.layout.VBox;

public class TextEditorMenu extends VBox {
    TopMenu topMenu;
    Toolbar toolbar;

    TextEditorMenu(WorkingArea workingArea){
        super();
        toolbar = new Toolbar(workingArea);
        topMenu = new TopMenu(workingArea,toolbar);
        super.getChildren().addAll(topMenu,toolbar);
    }
}
