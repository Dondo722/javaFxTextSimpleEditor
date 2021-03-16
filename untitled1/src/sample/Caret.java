package sample;


import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Caret{
    static protected Text textCaret = new Text("|");
//    textCaret.setStyle("-fx-font: 2 arial;");




    static public Text getTextCaret()
    {
        return textCaret;
    }

    static public int caretIndex(TextFlow textFlow)
    {
        return textFlow.getChildren().indexOf(textCaret);
    }
    static public Node caretNode(TextFlow textFlow)
    {
        return textFlow.getChildren().get(caretIndex(textFlow));
    }
}
