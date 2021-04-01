package sample;


import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.text.*;


public class WorkingArea extends ScrollPane implements EventHandler<KeyEvent> {
    private Caret caret = new Caret();
    private TextFlow textFlow  = new TextFlow(caret.getTextCaret());
    private String textSize = "16";
    private FontPosture fontPosture = FontPosture.REGULAR;
    private String  textFont = "Times New Roman";
    private FontWeight fontWeight = FontWeight.NORMAL;
    private boolean underline = false;
    private boolean selectability = false;
    private SelectedText selectedText = new SelectedText(textFlow,caret);
    private final WorkingAreaLogic workingAreaLogic;

    public WorkingArea(){
        super();
        workingAreaLogic = new WorkingAreaLogic(this);
        textFlow.setOnMouseDragged(e-> {
            workingAreaLogic.changeSelectability(true);
            if (selectability) {
                Node node = e.getPickResult().getIntersectedNode();
                if (textFlow.getChildren().contains(node)){
                    int index = textFlow.getChildren().indexOf(node);
                    selectedText.select(textFlow.getChildren().get(index));
                }
            }
        });
        textFlow.setOnMousePressed(mouseEvent -> workingAreaLogic.changeSelectability(false));
        textFlow.setOnMouseClicked(mouseEvent -> workingAreaLogic.workingArea.caret.mouseCaretControl(textFlow,mouseEvent));
        super.setContent(textFlow);
    }
    public void requestFocus(){}
    @Override
    public void handle(KeyEvent keyEvent){workingAreaLogic.keyPressed(keyEvent);} //key input
//   inserts Text to textFlow
    public void insert(CustomText text){
        workingAreaLogic.insert(text);
    }

    public Caret getCaret() {
        return caret;
    }

    public TextFlow getTextFlow() {
        return textFlow;
    }

    public String getTextSize() {
        return textSize;
    }

    public FontPosture getFontPosture() {
        return fontPosture;
    }

    public String getTextFont() {
        return textFont;
    }

    public FontWeight getFontWeight() {
        return fontWeight;
    }

    public boolean isUnderline() {
        return underline;
    }

    public boolean isSelectability() {
        return selectability;
    }

    public SelectedText getSelectedText() {
        return selectedText;
    }

    public void setCaret(Caret caret) {
        this.caret = caret;
    }

    public void setTextFlow(TextFlow textFlow) {
        this.textFlow = textFlow;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public void setFontPosture(FontPosture fontPosture) {
        this.fontPosture = fontPosture;
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }

    public void setFontWeight(FontWeight fontWeight) {
        this.fontWeight = fontWeight;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public void setSelectability(boolean selectability) {
        this.selectability = selectability;
    }

    public void setSelectedText(SelectedText selectedText) {
        this.selectedText = selectedText;
    }

    // Text changes
    // false if not global
    public void changeFont(String font){
        textFont = font;
        if(selectability){
            for (int i = 0; i < selectedText.getNodes().size(); i++){
                ((CustomText)selectedText.getNodes().get(i)).setTextFont(textFont);
            }
        }
    }
    public void changeSize(String size){
        textSize = size;
        if(selectability){
            for (int i = 0; i < selectedText.getNodes().size(); i++){
                ((CustomText)selectedText.getNodes().get(i)).setTextSize(textSize);
            }
        }
    }
    public void changeFontWeight(){
        if(fontWeight == FontWeight.NORMAL)
            fontWeight = FontWeight.BOLD;
        else fontWeight = FontWeight.NORMAL;
        if(selectability){
            for (int i = 0; i < selectedText.getNodes().size(); i++){
                ((CustomText)selectedText.getNodes().get(i)).setFontWeight(fontWeight);
            }
        }
    }
    public void changeFontPosture(){
        if(fontPosture == FontPosture.REGULAR)
            fontPosture = FontPosture.ITALIC;
        else fontPosture = FontPosture.REGULAR;
        if(selectability){
            for (int i = 0; i < selectedText.getNodes().size(); i++){
                ((CustomText)selectedText.getNodes().get(i)).setFontPosture(fontPosture);
            }
        }
    }
    public void changeUnderline(){
        underline = !underline;
        if(selectability){
            for (int i = 0; i < selectedText.getNodes().size(); i++){
                ((CustomText)selectedText.getNodes().get(i)).setUnderline(underline);
            }
        }
    }
    public String getString(){
        return workingAreaLogic.getString();
    }
    public void newTextFlow(){
        workingAreaLogic.newTextFlow();
    }
}
