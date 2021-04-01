package sample;

import javafx.scene.Node;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import static javafx.scene.input.DataFormat.PLAIN_TEXT;

public class WorkingAreaLogic {
    public WorkingArea workingArea;
    WorkingAreaLogic(WorkingArea workingArea){
        this.workingArea = workingArea;
    }
    public void changeSelectability(boolean changeOn) {
        workingArea.setSelectability(changeOn);
        workingArea.getSelectedText().setSelectable(workingArea.isSelectability());
    }
    public void keyPressed(KeyEvent keyEvent) {
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
    public void rightKey(){
        if (workingArea.isSelectability()) {
            moveForwardSelected();
            changeSelectability(false);
        }
        else {
            int caretIndex  = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
            if (caretIndex != workingArea.getTextFlow().getChildren().size() - 1) {
                Node caretNode = workingArea.getTextFlow().getChildren().get(caretIndex);
                workingArea.getCaret().moveCaretForward(workingArea.getTextFlow(), caretNode, caretIndex, caretIndex + 1);
            }
        }
    }
    public void leftKey()
    {
        if (workingArea.isSelectability()) {
            moveBehindSelected();
            changeSelectability(false);
        }
        else {
            int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
            if (caretIndex > 0) {
                Node caret = workingArea.getTextFlow().getChildren().get(caretIndex);
                workingArea.getCaret().moveCaretBehind(workingArea.getTextFlow(),caretIndex - 1, caret);
            }
        }
    }
    public void upKey(){
        if (workingArea.isSelectability()) {
            moveBehindSelected();
            changeSelectability(false);
        }
        else {
            int nodeIndex = getNodeHigherIndex();
            if(nodeIndex == 0){
                if(!((Text)workingArea.getTextFlow().getChildren().get(0)).getText().equals("\n"))
                    return;
            }
            workingArea.getCaret().moveCaretBehind(workingArea.getTextFlow(), nodeIndex ,workingArea.getCaret().caretNode(workingArea.getTextFlow()));
        }
    }
    public int getNodeHigherIndex(){
        int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
        int startIndex = firstEnterBefore(caretIndex);
        int endIndex = firstEnterBefore(startIndex - 1);

        System.out.println(startIndex);
        System.out.println(endIndex);

        double nodeX = workingArea.getTextFlow().getChildren().get(caretIndex).getBoundsInParent().getMaxX();
        for (int i = startIndex; i >= endIndex; i--){
            if(inBounds(workingArea.getTextFlow().getChildren().get(i),nodeX))
                return i;
        }
        return startIndex;
    }
    public void downKey(){
        if (workingArea.isSelectability()) {
            moveForwardSelected();
            changeSelectability(false);
        }
        else {
            int nodeIndex = getNodeLowerIndex();
            if(nodeIndex == -1) return;
            workingArea.getCaret().moveCaretForward(workingArea.getTextFlow(),workingArea.getCaret().caretNode(workingArea.getTextFlow()),workingArea.getCaret().caretIndex(workingArea.getTextFlow()),nodeIndex);
        }
    }
    public int getNodeLowerIndex(){
        int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
        int startIndex = firstEnterAfter(caretIndex);
        int endIndex = firstEnterAfter(startIndex + 1);
        if(startIndex == endIndex) return -1;
        System.out.println(startIndex);
        System.out.println(endIndex);

        double nodeX = workingArea.getTextFlow().getChildren().get(caretIndex).getBoundsInParent().getMaxX();
        if(startIndex == caretIndex +1)startIndex +=1;
        for (int i = startIndex; i < endIndex; i++){
            if(inBounds(workingArea.getTextFlow().getChildren().get(i),nodeX))
                return i;
        }
        if (endIndex + 1 == workingArea.getTextFlow().getChildren().size()) return  endIndex;
        return endIndex - 1;
    }
    public void backSpaceKey(){
        remove();
    }
    public void ctrlCKey(){
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(workingArea.getSelectedText().toString());
        clipboard.setContent(content);
    }
    public void ctrlVKey(){
        if(workingArea.isSelectability()){
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
            if (((Text)workingArea.getTextFlow().getChildren().get(i)).getText().equals("\n")) return i;
        }
        return 0;
    }
    public int firstEnterAfter(int nodeIndex){
        for (int i = nodeIndex; i < workingArea.getTextFlow().getChildren().size(); i++){
            if (((Text)workingArea.getTextFlow().getChildren().get(i)).getText().equals("\n")) return i;
        }
        return workingArea.getTextFlow().getChildren().size() - 1;
    }
    public boolean inBounds(Node node, double caretMinX){
        double minX = node.getBoundsInParent().getMinX();
        double maxX = node.getBoundsInParent().getMaxX();
        return minX <= caretMinX && maxX >= caretMinX;
    }
    // creates text element & set style before adding it to the textFlow
    public void addToTextFlow(String string){
        CustomText text = new CustomText(string,workingArea.getTextSize(),workingArea.getTextFont(),workingArea.getFontWeight(),workingArea.getFontPosture(),workingArea.isUnderline());
        if(workingArea.isSelectability()){
            moveBehindSelected();
            remove();
        }
        insert(text);
    }
    // inserts Text to textFlow
    public void insert(CustomText text){
        int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
        workingArea.getCaret().changeSize(workingArea.getTextFlow());
        Node caret = workingArea.getTextFlow().getChildren().get(caretIndex);
        workingArea.getTextFlow().getChildren().set(caretIndex,text);
        workingArea.getCaret().moveCaretBehind(workingArea.getTextFlow(),caretIndex + 1 , caret);
    }
    // remove Node/getNodes() (if selected)  in textFlow
    public void remove(){
        if(workingArea.isSelectability()) {
            workingArea.getTextFlow().getChildren().removeAll(workingArea.getSelectedText().getNodes());
            changeSelectability(false);
        }
        else {
            int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
            if (caretIndex != 0)
                workingArea.getTextFlow().getChildren().remove(caretIndex-1,caretIndex);
        }
    }
    // Selected text caret moves
    public void moveBehindSelected(){
        Node node = workingArea.getSelectedText().getNodes().get(0);
        int index = workingArea.getTextFlow().getChildren().indexOf(node);
        Node caretNode = workingArea.getCaret().caretNode(workingArea.getTextFlow());
        if (workingArea.getCaret().caretIndex(workingArea.getTextFlow()) != index - 1)
            workingArea.getCaret().moveCaretBehind(workingArea.getTextFlow(),index,caretNode);
    }
    public void moveForwardSelected(){
        Node node = workingArea.getSelectedText().getNodes().get(workingArea.getSelectedText().getNodes().size()-1);
        int index = workingArea.getTextFlow().getChildren().indexOf(node);
        Node caretNode = workingArea.getCaret().caretNode(workingArea.getTextFlow());
        int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
        if (index != caretIndex + 1)
            workingArea.getCaret().moveCaretForward(workingArea.getTextFlow(),caretNode,caretIndex,index);
    }

    public String getString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i< workingArea.getTextFlow().getChildren().size(); i++){
            if( i != workingArea.getCaret().caretIndex(workingArea.getTextFlow()))
                stringBuilder.append(((Text)workingArea.getTextFlow().getChildren().get(i)).getText());
        }
        return stringBuilder.toString();
    }
    public void newTextFlow(){
        int caretIndex = workingArea.getCaret().caretIndex(workingArea.getTextFlow());
        if(caretIndex == 0){
            workingArea.getTextFlow().getChildren().remove(1,workingArea.getTextFlow().getChildren().size());
        }
        else {
            if(caretIndex + 1 != workingArea.getTextFlow().getChildren().size())
                workingArea.getTextFlow().getChildren().remove(caretIndex + 1,workingArea.getTextFlow().getChildren().size());
            workingArea.getTextFlow().getChildren().remove(0,caretIndex);
        }
    }
}
