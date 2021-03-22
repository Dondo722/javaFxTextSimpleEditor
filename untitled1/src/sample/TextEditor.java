package sample;



import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;


public class TextEditor extends VBox {
    EditorMenuBar editorMenuBar = new EditorMenuBar();
    Toolbar toolbar = new Toolbar();
    WorkingArea workingArea = new WorkingArea();

    TextEditor() {
        super();
        super.getChildren().addAll(editorMenuBar, toolbar, workingArea);
    }
    public void addEventHandlers(){
        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED,workingArea);
    }
}