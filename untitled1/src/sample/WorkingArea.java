package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public String  textFont = "Times New Roman";
    public boolean italic = false;
    public boolean bold = false;
    public boolean underline = false;
    public boolean selectability = false;
    public SelectedText selectedText = new SelectedText(textFlow,caret);

    //KeyCombination crtlC = new KeyCombination(KeyCode.CONTROL);

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
            int nodeIndex = getNodeHigherIndex();
            if(nodeIndex == 0){
                if(!((Text)textFlow.getChildren().get(0)).getText().equals("\n"))
                    return;
            }
            moveCaretBehind(nodeIndex ,caret.caretNode(textFlow));
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
            moveCaretForward(caret.caretNode(textFlow),caret.caretIndex(textFlow),nodeIndex);
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
    //sets new Text Weight
    public void setTextFont(Text text, FontWeight fontWeight){
        Font font = text.getFont();
        String family = font.getFamily();
        double size = font.getSize();
        if(font.getStyle().length() == 7 || font.getStyle().length() == 4 ) setTextFont(text,family,fontWeight,FontPosture.REGULAR,size);
        else     setTextFont(text,family,fontWeight,FontPosture.ITALIC,size);
    }
    //sets new FontPosture
    public void setTextFont(Text text, FontPosture fontPosture){
        Font font = text.getFont();
        String family = font.getFamily();
        double size = font.getSize();
        if(font.getStyle().length() == 11 || font.getStyle().length() == 4 ) setTextFont(text,family,FontWeight.BOLD,fontPosture,size);
        else     setTextFont(text,family,FontWeight.NORMAL,fontPosture,size);
    }
    //sets TextFont by parameters
    public void setTextFont(Text text , String font, FontWeight fontWeight, FontPosture fontPosture,double size) {
        text.setFont(Font.font(font, fontWeight,fontPosture,size));
        if(underline) text.setUnderline(true);
    }
    // sets text font by default settings
    public void setTextFont(Text text) {
        if(italic && bold) text.setFont(Font.font(textFont, FontWeight.BLACK,FontPosture.ITALIC,Double.parseDouble(textSize)));
        else if(italic) text.setFont(Font.font(textFont,FontPosture.ITALIC,Double.parseDouble(textSize)));
        else if(bold) text.setFont(Font.font(textFont, FontWeight.BLACK,Double.parseDouble(textSize)));
        else text.setFont(Font.font(textFont,Double.parseDouble(textSize)));
        if(underline) text.setUnderline(true);
    }
    // creates text element & set style before adding it to the textFlow
    public void addToTextFlow(String string){
//        Text text = new Text(string);
//        setTextFont(text);
        CustomText text = new CustomText(string);
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
        this.caret.changeSize(textFlow);
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
        this.caret.changeSize(textFlow);
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
    // Text changes
    // false if not global
    public void changeFont(){
        if(selectability){

            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((CustomText)selectedText.nodes.get(i)).setTextFont(textFont);
                ((CustomText)selectedText.nodes.get(i)).setTextSize(textSize);
                //setTextFont(((Text)selectedText.nodes.get(i)));
            }
        }
    }
    public void changeBold(){
        FontWeight fontWeight;
        if(bold) fontWeight = FontWeight.BOLD;
        else fontWeight = FontWeight.NORMAL;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                setTextFont(((Text)selectedText.nodes.get(i)),fontWeight);
            }
        }
    }
    public void changeItalic(){
        FontPosture fontPosture;
        if(italic) fontPosture = FontPosture.ITALIC;
        else fontPosture = FontPosture.REGULAR;
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                setTextFont(((Text)selectedText.nodes.get(i)),fontPosture);
            }
        }
    }
    public void changeUnderline(){
        if(selectability){
            for (int i = 0; i < selectedText.nodes.size(); i++){
                ((Text)selectedText.nodes.get(i)).setUnderline(underline);
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
    public String getStringNodes(){
        String string = "";
        for (int i = 0; i< textFlow.getChildren().size(); i++){
            if( i != caret.caretIndex(textFlow))
                string += ((Text)textFlow.getChildren().get(i)).toString();
        }
        return string;
    }
    public TextFlow getTextFlow(){
        TextFlow textFlow = new TextFlow();
        for (int i = 0; i < this.textFlow.getChildren().size(); i++) {
            Node node = this.textFlow.getChildren().get(i);
            textFlow.getChildren().add(node);
        }
        return textFlow;
    }











}
