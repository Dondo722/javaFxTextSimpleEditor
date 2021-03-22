package sample;


import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;

public class WorkingArea extends ScrollPane implements EventHandler<KeyEvent> {
    Caret caret = new Caret();
    TextFlow textFlow  = new TextFlow(caret.getTextCaret());
    String  style = " -fx-font: 20 Times_New_Roman;";
    boolean selectability = false;
    SelectedText selectedText = new SelectedText(textFlow,caret);

    WorkingArea(){
        super();
        textFlow.setPrefHeight(Screen.getPrimary().getBounds().getHeight());
        textFlow.setOnMouseDragged(e-> {
            changeChoice(true);
            if (selectability) {
                Node node = e.getPickResult().getIntersectedNode();
                if (textFlow.getChildren().contains(node)){
                    int index = textFlow.getChildren().indexOf(node);
                    selectedText.select(textFlow.getChildren().get(index));
                }
            }
        });
        textFlow.setOnMousePressed(mouseEvent ->{
            changeChoice(false);
            System.out.println("Pressed");
        });
        textFlow.setOnMouseClicked(this::mouseCaretControl);
        super.setContent(textFlow);
    }
    public void requestFocus(){}
    @Override
    public void handle(KeyEvent keyEvent){keyPressed(keyEvent);} //key input

    // selectability change
    public void changeChoice(boolean changeOn)
    {
        selectability = changeOn;
        selectedText.setSelectable(selectability);
    }
    // return string info of "Text" node
    public String nodeToString(Node node){
        String nodeDate = node.toString();
        int startIndex = nodeDate.indexOf('"');
        int endIndex = nodeDate.lastIndexOf('"');

        return nodeDate.substring(startIndex + 1,endIndex);
    }

    // if mouse clicked on textFlow it's move caret to position where mouse was clicked
    public void mouseCaretControl(MouseEvent mouseEvent)
    {
        Node chosenNode = mouseEvent.getPickResult().getIntersectedNode();
        if (!(chosenNode instanceof Text)) return;
        int chosenNodeIndex = textFlow.getChildren().indexOf(chosenNode);
        int caretIndex  = caret.caretIndex(textFlow);
        Node caretNode = textFlow.getChildren().get(caretIndex);
        if (caretIndex < chosenNodeIndex) {
            moveCaretForward(caretNode, caretIndex, chosenNodeIndex);
        }
        else moveCaretBehind(chosenNodeIndex,caretNode);
    }


    // Keys methods
    //***************************************************

    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.RIGHT) rightKey();
        else if(keyEvent.getCode() == KeyCode.LEFT) leftKey();
        else if (keyEvent.getCode() == KeyCode.UP) upKey();
        else if (keyEvent.getCode() == KeyCode.DOWN) downKey();
        else if (keyEvent.getCode() == KeyCode.ENTER) addToTextFlow("\n");
        else if (keyEvent.getCode() == KeyCode.CAPS || keyEvent.getCode() == KeyCode.CONTROL || keyEvent.getCode() == KeyCode.SHIFT || keyEvent.getCode() == KeyCode.ALT) return;
        else if(keyEvent.getCode() == KeyCode.BACK_SPACE) backSpaceKey();
        else  addToTextFlow(keyEvent.getText());

    }
    public void rightKey()
    {
        if (selectability) {
            moveForwardSelected();
            changeChoice(false);
        }
        else {
            int caretIndex  = caret.caretIndex(textFlow);
            if (caretIndex != textFlow.getChildren().size() - 1) {
                Node caretNode = textFlow.getChildren().get(caretIndex);
                moveCaretForward(caretNode, caretIndex, caretIndex + 1);
            }
        }
    }
    public void leftKey()
    {
        if (selectability) {
            moveBehindSelected();
            changeChoice(false);
        }
        else {
            int caretIndex = caret.caretIndex(textFlow);
            if (caretIndex > 0) {
                Node caret = textFlow.getChildren().get(caretIndex);
                moveCaretBehind(caretIndex - 1, caret);
            }
        }
    }
    public void upKey(){
        if (selectability) {
            moveBehindSelected();
            changeChoice(false);
        }
        else {
            double caretMaxX = caret.caretNode(textFlow).getBoundsInParent().getMaxX();
            double caretMaxY = caret.caretNode(textFlow).getBoundsInParent().getMaxY();
            double nodeHeight = caret.caretNode(textFlow).getBoundsInParent().getHeight();
            double caretYNew = caretMaxY - nodeHeight;
            upKeyGear(caretMaxX, caretMaxY, caretYNew);
        }
    }
    public void upKeyGear(double nodeX, double nodeY, double nodeYNew){
        Node node = getNodeByCoordinates(nodeX,nodeY,nodeYNew);
        Node caretNode = textFlow.getChildren().get(caret.caretIndex(textFlow));
        if (node == null)
        {
            node = firstEnterBefore(textFlow.getChildren().indexOf(caretNode));
            if (node == null)return;
        }
        moveCaretBehind(textFlow.getChildren().indexOf(node),caretNode);
    }
    public void downKey(){
        if (selectability) {
            moveForwardSelected();
            changeChoice(false);
        }
        else {
            double caretMaxX = caret.caretNode(textFlow).getBoundsInParent().getMaxX();
            double caretMaxY = caret.caretNode(textFlow).getBoundsInParent().getMaxY();
            double nodeHeight = caret.caretNode(textFlow).getBoundsInParent().getHeight();
            double caretYNew = caretMaxY + nodeHeight;
            downKeyGear(caretMaxX, caretMaxY, caretYNew);
        }
    }
    public void downKeyGear(double nodeX, double nodeY, double nodeYNew){
        Node node = getNodeByCoordinates(nodeX,nodeY,nodeYNew);
        System.out.println(node);
        Node caretNode = textFlow.getChildren().get(caret.caretIndex(textFlow));
        if (node == null)
        {
            node = secondEnterAfter(textFlow.getChildren().indexOf(caretNode));
            if (node == null)return;
        }
        int futureIndex = textFlow.getChildren().indexOf(node);
        if (node != textFlow.getChildren().get(textFlow.getChildren().size()-1))
            futureIndex -= 1;

        moveCaretForward(caretNode,caret.caretIndex(textFlow),futureIndex);
    }
    public void backSpaceKey(){
        remove();
    }


    //Key "logic" methods
    //***************************************************

    // first enter before is a part of upKey logic that return first \n in textFlow before caret
    public Node firstEnterBefore(int nodeIndex){
        for (int i = nodeIndex; i >= 0; i--){
            if (nodeToString(textFlow.getChildren().get(i)).equals("\n")) return textFlow.getChildren().get(i);
        }
        return null;
    }
    // second enter after is a part of downKey logic that return second \n in textFlow after caret if it's exist & last index of node if second \n doesn't exist
    public Node secondEnterAfter(int nodeIndex){
        int count = 0;
        for (int i = nodeIndex; i < textFlow.getChildren().size(); i++){

            if (nodeToString(textFlow.getChildren().get(i)).equals("\n")) {
                System.out.println(count + "     ");
                if(count == 1) return textFlow.getChildren().get(i);
                else count++;
            }
            if(i == textFlow.getChildren().size() - 1 && count == 1) return textFlow.getChildren().get(i);
        }
        return null;
    }
    // if node with this coordinates(nodeX, nodeYNew) exist then it will return it
    public Node getNodeByCoordinates(double nodeX, double nodeY, double nodeYNew){
        Bounds bounds;
        if(nodeY > nodeYNew){
            for (int i = 0; i < textFlow.getChildren().size() ; i++){
                bounds = textFlow.getChildren().get(i).getBoundsInParent();
                if (inBounds(nodeX,nodeYNew, bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(),bounds.getMaxY()))return textFlow.getChildren().get(i);
            }
        }
        else if(nodeY < nodeYNew){
            for (int i = caret.caretIndex(textFlow); i < textFlow.getChildren().size() ; i++){
                bounds = textFlow.getChildren().get(i).getBoundsInParent();
                if (inBounds(nodeX,nodeYNew, bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(),bounds.getMaxY())){
                    return textFlow.getChildren().get(i);
                }
            }
        }
        return null;
    }
    // if nodeX & nodeY is in bounds return true, else false
    public boolean inBounds(double nodeX, double nodeY, double boundsMinX, double boundsMaxX, double boundsMinY, double boundsMaxY){
        return boundsMinX <= nodeX && boundsMaxX >= nodeX && boundsMinY < nodeY && boundsMaxY >= nodeY;
    }
    // creates text element & set style before adding it to the textFlow
    public void addToTextFlow(String string){
        Text text = new Text(string);
        text.setStyle(style);
        if(selectability){
            moveBehindSelected();
            remove();
        }
        insert(text);
    }
    // inserts Text to textFlow
    public void insert(Text text){
        int caretIndex = caret.caretIndex(textFlow);
        Node caret = textFlow.getChildren().get(caretIndex);
        textFlow.getChildren().set(caretIndex,text);
        moveCaretBehind(caretIndex + 1 , caret);
    }
    // changing caret position forward in textFlow
    public void moveCaretForward(Node caretNode, int caretIndexBefore, int caretIndexAfter){
        TextFlow tempTextFlow = new TextFlow();
        caretIndexBefore += 1;            // we don't need caret pos itself, we need first next element index
        while (caretIndexBefore != textFlow.getChildren().size()){            //Coping text elements after caret position
            tempTextFlow.getChildren().add(textFlow.getChildren().get(caretIndexBefore));
        }
        textFlow.getChildren().remove(caretNode);           //deleting caret
        while (textFlow.getChildren().size() < caretIndexAfter ){            //Coping text before future caret position
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
        textFlow.getChildren().add(caretNode);                   //Adding Caret
        while(!tempTextFlow.getChildren().isEmpty()){                 //Adding the test og text
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
    }
    // changing caret position behind in textFlow
    public void moveCaretBehind(int index, Node caret){
        TextFlow tempTextFlow = new TextFlow();

        while (index != textFlow.getChildren().size()){
            tempTextFlow.getChildren().add(textFlow.getChildren().get(index));
        }
        textFlow.getChildren().add(caret);
        while (!tempTextFlow.getChildren().isEmpty()){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
    }
    // remove Node/Nodes (if selected)  in textFlow
    public void remove(){
        if(selectability) {
            textFlow.getChildren().removeAll(selectedText.nodes);
            changeChoice(false);
        }
        else {
            int caretIndex = caret.caretIndex(textFlow);
            if (caretIndex != 0)
                textFlow.getChildren().remove(caretIndex-1,caretIndex);
        }
    }

    // Selected text caret moves
    public void moveBehindSelected(){
        Node node = selectedText.nodes.get(0);
        int index = textFlow.getChildren().indexOf(node);
        Node caretNode = caret.caretNode(textFlow);
        if (caret.caretIndex(textFlow) != index - 1)
        moveCaretBehind(index,caretNode);
    }
    public void moveForwardSelected(){
        Node node = selectedText.nodes.get(selectedText.nodes.size()-1);
        int index = textFlow.getChildren().indexOf(node);
        Node caretNode = caret.caretNode(textFlow);
        int caretIndex = caret.caretIndex(textFlow);
        if (index != caretIndex + 1)
        moveCaretForward(caretNode,caretIndex,index);
    }
}
