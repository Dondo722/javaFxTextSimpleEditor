package sample;

import javafx.scene.layout.VBox;

public class TextEditorMenu extends VBox {
    WorkingArea workingArea;
    EditorMenuBar editorMenuBar;
    Toolbar toolbar;

    TextEditorMenu(WorkingArea workingArea){
      super();
      this.workingArea = workingArea;
      editorMenuBar = new EditorMenuBar(workingArea);
      toolbar = new Toolbar(workingArea);
      super.getChildren().addAll(editorMenuBar,toolbar);
    }
}
