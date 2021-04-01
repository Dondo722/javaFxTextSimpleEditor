package sample;

import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;


public class TextEditor extends BorderPane {
    private final WorkingArea workingArea = new WorkingArea();

    TextEditor() {
        super();
        TextEditorMenu textEditorMenu = new TextEditorMenu(workingArea);
        super.setTop(textEditorMenu);
        super.setCenter(workingArea);
    }
    public void addEventHandlers(){
        this.getScene().addEventFilter(KeyEvent.KEY_PRESSED,workingArea);
    }
}