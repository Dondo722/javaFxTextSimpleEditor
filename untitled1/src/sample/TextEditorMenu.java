package sample;

import javafx.scene.layout.VBox;

public class TextEditorMenu extends VBox {
    WorkingArea workingArea;
    EditorMenuBar editorMenuBar = new EditorMenuBar();
    Toolbar toolbar = new Toolbar(workingArea);

    TextEditorMenu(WorkingArea workingArea){
      super();
      this.workingArea = workingArea;
      editorMenuBar = new EditorMenuBar();
      toolbar = new Toolbar(workingArea);
      super.getChildren().addAll(editorMenuBar,toolbar);
    }
}
