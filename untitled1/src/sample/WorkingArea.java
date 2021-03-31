package sample;


import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.text.*;

import static javafx.scene.input.DataFormat.PLAIN_TEXT;

public class WorkingArea extends ScrollPane implements EventHandler<KeyEvent> {
    public Caret caret = new Caret();
    public TextFlow textFlow  = new TextFlow(caret.getTextCaret());
    public String textSize = "16";
    public FontPosture fontPosture = FontPosture.REGULAR;
    public String  textFont = "Times New Roman";
    public FontWeight fontWeight = FontWeight.NORMAL;
    public boolean underline = false;
    public boolean selectability = false;
    public SelectedText selectedText = new SelectedText(textFlow,caret);

    public WorkingArea(){
        super();
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
        textFlow.setOnMousePressed(mouseEvent -> changeChoice(false));
        textFlow.setOnMouseClicked(mouseEvent -> caret.mouseCaretControl(textFlow,mouseEvent));
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

    // Keys methods
    //***************************************************

    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.RIGHT) rightKey();
        else if(keyEvent.getCode() == KeyCode.LEFT) leftKey();
        else if (keyEvent.getCode() == KeyCode.UP) upKey();
        else if (keyEvent.getCode() == KeyCode.DOWN) downKey();
        else if (keyEvent.getCode() == KeyCode.SPACE) addToTextFlow(" ");
        else if (keyEvent.getCode() == KeyCode.ENTER) addToTextFlow("\n");
        else if (keyEvent.getCode() == KeyCode.C && keyEvent.isControlDown()) ctrlCKey();
        else if(keyEvent.getCode() == KeyCode.V && keyEvent.isControlDown()) ctrlVKey();
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
                this.caret.moveCaretForward(textFlow, caretNode, caretIndex, caretIndex + 1);
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
                this.caret.moveCaretBehind(textFlow,caretIndex - 1, caret);
            }
        }
    }
    public void upKey(){
        if (selectability) {
            moveBehindSelected();
            changeChoice(false);
        }
        else {
            int nodeIndex = getNodeHigherIndex();
            if(nodeIndex == 0){
                if(!((Text)textFlow.getChildren().get(0)).getText().equals("\n"))
                    return;
            }
            this.caret.moveCaretBehind(textFlow, nodeIndex ,caret.caretNode(textFlow));
        }
    }
    public int getNodeHigherIndex(){
        int caretIndex = caret.caretIndex(textFlow);
        int startIndex = firstEnterBefore(caretIndex);
        int endIndex = firstEnterBefore(startIndex - 1);

        System.out.println(startIndex);
        System.out.println(endIndex);

        double nodeX = textFlow.getChildren().get(caretIndex).getBoundsInParent().getMaxX();
        for (int i = startIndex; i >= endIndex; i--){
            if(inBounds(textFlow.getChildren().get(i),nodeX))
                return i;
        }
        return startIndex;
    }
    public void downKey(){
        if (selectability) {
            moveForwardSelected();
            changeChoice(false);
        }
        else {
            int nodeIndex = getNodeLowerIndex();
            if(nodeIndex == -1) return;
            this.caret.moveCaretForward(textFlow,caret.caretNode(textFlow),caret.caretIndex(textFlow),nodeIndex);
        }
    }
    public int getNodeLowerIndex(){
        int caretIndex = caret.caretIndex(textFlow);
        int startIndex = firstEnterAfter(caretIndex);
        int endIndex = firstEnterAfter(startIndex + 1);
        if(startIndex == endIndex) return -1;
        System.out.println(startIndex);
        System.out.println(endIndex);

        double nodeX = textFlow.getChildren().get(caretIndex).getBoundsInParent().getMaxX();
        if(startIndex == caretIndex +1)startIndex +=1;
        for (int i = startIndex; i < endIndex; i++){
            if(inBounds(textFlow.getChildren().get(i),nodeX))
                return i;
        }
        if (endIndex + 1 == textFlow.getChildren().size()) return  endIndex;
        return endIndex - 1;
    }
    public void backSpaceKey(){
        remove();
    }
    public void ctrlCKey(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(selectedText.toString());
        clipboard.setContent(content);
    }
    public void ctrlVKey(){
        if(selectability){
            moveBehindSelected();
            remove();
        }
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = clipboard.getContent(PLAIN_TEXT).toString();
        for (int i = 0; content.length() > i; i++)
        {
            CustomText text = new CustomText(String.valueOf(content.charAt(i)));
            insert(text);
        }
    }

    //Key "logic" methods
    //***************************************************

    // first enter before is a part of upKey logic that return first \n in textFlow before caret
    public int firstEnterBefore(int nodeIndex){
        for (int i = nodeIndex; i >= 0; i--){
            if (((Text)textFlow.getChildren().get(i)).getText().equals("\n")) return i;
        }
        return 0;
    }
    public int firstEnterAfter(int nodeIndex){
        for (int i = nodeIndex; i < textFlow.getChildren().size(); i++){
            if (((Text)textFlow.getChildren().get(i)).getText().equals("\n")) return i;
        }
        return textFlow.getChildren().size() - 1;
    }
    public boolean inBounds(Node node, double caretMinX){
        double minX = node.getBoundsInParent().getMinX();
        double maxX = node.getBoundsInParent().getMaxX();
        return minX <= caretMinX && maxX >= caretMinX;
    }
    // creates text element & set style before adding it to the textFlow
    public void addToTextFlow(String string){
        CustomText text = new CustomText(string,textSize,textFont,fontWeight,fontPosture,underline);
        if(selectability){
            moveBehindSelected();
            remove();
        }
        insert(text);
    }
    // inserts Text to textFlow
    public void insert(CustomText text){
        int caretIndex = caret.caretIndex(textFlow);
        this.caret.changeSize(textFlow);
        Node caret = textFlow.getChildren().get(caretIndex);
        textFlow.getChildren().set(caretIndex,text);
        this.caret.moveCaretBehind(textFlow,caretIndex + 1 , caret);
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
        this.caret.moveCaretBehind(textFlow,index,caretNode);
    }
    public void moveForwardSelected(){
        Node node = selectedText.nodes.get(selectedText.nodes.size()-1);
        int index = textFlow.getChildren().indexOf(node);
        Node caretNode = caret.caretNode(textFlow);
        int caretIndex = caret.caretIndex(textFlow);
        if (index != caretIndex + 1)
        this.caret.moveCaretForward(textFlow,caretNode,caretIndex,index);
    }
    // Text changes
    // false if not global
    public void changeFont(String font){
        textFont = font;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setTextFont(textFont);
            }
        }
    }
    public void changeSize(String size){
        textSize = size;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setTextSize(textSize);
            }
        }
    }
    public void changeFontWeight(){
        if(fontWeight == FontWeight.NORMAL)
            fontWeight = FontWeight.BOLD;
        else fontWeight = FontWeight.NORMAL;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setFontWeight(fontWeight);
            }
        }
    }
    public void changeFontPosture(){
        if(fontPosture == FontPosture.REGULAR)
            fontPosture = FontPosture.ITALIC;
        else fontPosture = FontPosture.REGULAR;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setFontPosture(fontPosture);
            }
        }
    }
    public void changeUnderline(){
        underline = !underline;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setUnderline(underline);
            }
        }
    }

    public String getString(){
        String string = "";
        for (int i = 0; i< textFlow.getChildren().size(); i++){
            if( i != caret.caretIndex(textFlow))
            string += ((Text)textFlow.getChildren().get(i)).getText();
        }
        return string;
    }
    public void newTextFlow(){
        int caretIndex = caret.caretIndex(textFlow);
        if(caretIndex == 0){
            textFlow.getChildren().remove(1,textFlow.getChildren().size());
        }
        else {
            if(caretIndex + 1 != textFlow.getChildren().size())
            textFlow.getChildren().remove(caretIndex + 1,textFlow.getChildren().size());
            textFlow.getChildren().remove(0,caretIndex);
        }
    }
}
