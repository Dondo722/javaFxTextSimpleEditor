package sample;



import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;


public class TextEditor extends BorderPane {
    WorkingArea workingArea = new WorkingArea();
    TextEditorMenu textEditorMenu = new TextEditorMenu(workingArea);

    TextEditor() {
        super();
        super.setTop(textEditorMenu);
        super.setCenter(workingArea);
    }
    public void addEventHandlers(){
        this.getScene().addEventFilter(KeyEvent.KEY_PRESSED,workingArea);
    }
}