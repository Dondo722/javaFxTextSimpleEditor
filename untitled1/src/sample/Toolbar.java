package sample;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;


public class Toolbar extends HBox {
    ComboBox<String> textSizeBox = new ComboBox<>(){ public void requestFocus(){}};

    Toolbar(){
        super();
        textSizeBox.getItems().addAll(
                "20",
                "40",
                "60"
        );
        textSizeBox.setPromptText("Text size");
        super.getChildren().addAll(textSizeBox);
    }
}
