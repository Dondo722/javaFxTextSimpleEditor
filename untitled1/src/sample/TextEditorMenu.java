package sample;

import javafx.scene.layout.VBox;

public class TextEditorMenu extends VBox {
    WorkingArea workingArea;
    FileMenuBar fileMenuBar;
    Toolbar toolbar;

    TextEditorMenu(WorkingArea workingArea){
      super();
      this.workingArea = workingArea;
      fileMenuBar = new FileMenuBar(workingArea);
      toolbar = new Toolbar(workingArea);
      super.getChildren().addAll(fileMenuBar,toolbar);
    }
}
