package sample;


import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Caret{
     protected Text textCaret;
//    textCaret.setStyle("-fx-font: 2 arial;");

    Caret()
    {
        textCaret = new Text("|");
        textCaret.setStyle("-fx-font: 20 Times_New_Roman;");
    }


     public Text getTextCaret()
     {
        return textCaret;
     }

     public int caretIndex(TextFlow textFlow)
    {
        return textFlow.getChildren().indexOf(textCaret);
    }
     public Node caretNode(TextFlow textFlow)
    {
        return textFlow.getChildren().get(caretIndex(textFlow));
    }
}
