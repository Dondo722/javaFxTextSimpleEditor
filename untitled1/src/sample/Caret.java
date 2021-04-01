package sample;


import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Caret{
    protected Text textCaret;
    Caret(){
        textCaret = new Text("|");
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
     public void changeSize(TextFlow textFlow){
         if(caretIndex(textFlow)  > 0 )
         textCaret.setFont(((Text) textFlow.getChildren().get(caretIndex(textFlow)-1)).getFont());
     }
    // changing caret position forward in textFlow
    public void moveCaretForward(TextFlow textFlow,Node caretNode, int caretIndexBefore, int caretIndexAfter){
        TextFlow tempTextFlow = new TextFlow();
        // we don't need caret pos itself, we need first next element index
        caretIndexBefore += 1;
        //Coping text elements after caret position
        while (caretIndexBefore != textFlow.getChildren().size()){
            tempTextFlow.getChildren().add(textFlow.getChildren().get(caretIndexBefore));
        }
        //deleting caret
        textFlow.getChildren().remove(caretNode);
        //Coping text before future caret position
        while (textFlow.getChildren().size() < caretIndexAfter ){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
        //Adding Caret
        textFlow.getChildren().add(caretNode);
        //Adding the test og text
        while(!tempTextFlow.getChildren().isEmpty()){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
        this.changeSize(textFlow);
    }
    // changing caret position behind in textFlow
    public void moveCaretBehind(TextFlow textFlow,int index, Node caret){
        TextFlow tempTextFlow = new TextFlow();

        while (index != textFlow.getChildren().size()){
            tempTextFlow.getChildren().add(textFlow.getChildren().get(index));
        }
        textFlow.getChildren().add(caret);
        while (!tempTextFlow.getChildren().isEmpty()){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
        this.changeSize(textFlow);
    }
    // if mouse clicked on textFlow then use this method to move caret
    public void mouseCaretControl(TextFlow textFlow,MouseEvent mouseEvent)
    {
        Node chosenNode = mouseEvent.getPickResult().getIntersectedNode();
        if (!(chosenNode instanceof Text)) return;
        int chosenNodeIndex = textFlow.getChildren().indexOf(chosenNode);
        int caretIndex  = this.caretIndex(textFlow);
        Node caretNode = textFlow.getChildren().get(caretIndex);
        if (caretIndex < chosenNodeIndex) {
            this.moveCaretForward(textFlow,caretNode, caretIndex, chosenNodeIndex);
        }
        else this.moveCaretBehind(textFlow,chosenNodeIndex,caretNode);
    }
}
