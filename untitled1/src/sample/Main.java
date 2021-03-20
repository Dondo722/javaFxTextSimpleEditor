package sample;

import com.sun.glass.ui.Size;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyledTextArea;

import javax.swing.*;


import static javafx.scene.input.DataFormat.PLAIN_TEXT;
import static javafx.scene.input.MouseDragEvent.MOUSE_DRAG_ENTERED;
import static javafx.scene.input.MouseDragEvent.MOUSE_DRAG_OVER;
import static javafx.scene.input.MouseEvent.MOUSE_CLICKED;
import static javafx.scene.input.MouseEvent.MOUSE_PRESSED;


public class Main extends Application {




    VBox vBox = new VBox();
    String style ;
    HBox hBox = new HBox();
    Caret caret = new Caret();
    TextFlow textFlow = new TextFlow(caret.getTextCaret());
    Button buttonFile;
    Button buttonFile2;
    Button buttonCopy;
    Button buttonPaste;
    Button buttonShow;

    ScrollPane scrollPane = new ScrollPane(){ public void requestFocus(){}};


    int styleInt = 20;

    double firstX = -1;
    double lastX = -1;
    double firstY = -1;
    double lastY = -1;

    boolean selectability = false;
    SelectedText selectedText = new SelectedText(textFlow,caret);

    public static void main(String[] args)
    {
        launch(args);
    }
    public void start(Stage stage)
    {
        BorderPane root = new BorderPane();;

        style = " -fx-font: " + styleInt + " Times_New_Roman;";
        //textFlow.setLineSpacing(5);

        //TextFlow Text settings





        buttonShow = new Button("show"){ public void requestFocus(){}};
        buttonShow.setOnAction((e)->{
            for (int i = 0; i < textFlow.getChildren().size();i++){
                System.out.println(nodeToString(textFlow.getChildren().get(i)));
                if (nodeToString(textFlow.getChildren().get(i)).equals("\n"))
                    System.out.println("NNNNNNNNNNNNNNNNNNNNNNNN");
            }

        });



        buttonFile = new Button(" coordinates "){ public void requestFocus(){}};//creating button & override it's focus off
        buttonFile.setOnAction((e)-> {
            System.out.println("BUTTON===========================");
            for (int i = 0; i < textFlow.getChildren().size()-1;i++){
                System.out.println(textFlow.getChildren().get(i));
                System.out.println("Bounds: " + (textFlow.getChildren().get(i).getBoundsInParent()));
            }
        });
        buttonCopy = new Button(" Copy "){ public void requestFocus(){}};
        buttonCopy.setOnAction((e)->{
            System.out.println("FIRST: " + selectedText.firstNode);
           for (Node node : selectedText.nodes)
                System.out.println(node);
        });
        buttonPaste = new Button(" Paste "){ public void requestFocus(){}};
        buttonPaste.setOnAction((e)->{
            setButtonPaste();
        });

        buttonFile2 = new Button(" Size + Caret ");
        buttonFile2.setFocusTraversable(false);//set focus off
        buttonFile2.setOnAction((e)-> {
            System.out.println("Caret: " + caret.caretIndex(textFlow));
            System.out.println("Size: " + textFlow.getChildren().size());

        });
        hBox.getChildren().addAll(buttonFile,buttonFile2,buttonCopy,buttonPaste,buttonShow);

        scrollPane.setContent(textFlow);
        root.setCenter(scrollPane);
        root.setTop(hBox);

        vBox.getChildren().addAll(textFlow);
        Scene scene = new Scene(root,400,400);



        root.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                //System.out.println(keyEvent.getText());
                //System.out.println("xMin: "+ textFlow.getBoundsInParent().getMinX() + " xMax: " + textFlow.getBoundsInParent().getMaxX());
                keyPressed(keyEvent);
            }
        });
        textFlow.setOnMouseClicked(mouseEvent -> {
                //System.out.println(mouseEvent.getPickResult());
                System.out.println(mouseEvent.getX());
                testingContains(mouseEvent);

            });
        textFlow.setOnMousePressed(mouseEvent ->{
            changeChoice(false);
            System.out.println("Pressed");
        });
//        textFlow.setOnMouseReleased(mouseEvent -> {
//            System.out.println("RELEASED");
//            if(selectability){
//
//            }
//
//        });
//        textFlow.setOnMouseMoved(mouseEvent -> {
//            if (mouseEvent.getEventType() == MOUSE_CLICKED)
//            {
//                System.out.println("MOUSE_PRESSED");
//            }
//            System.out.println("MOVED");
//        });

        textFlow.setOnMouseDragged(mouseEvent -> {
            changeChoice(true);
            if (selectability) {
                Node node = mouseEvent.getPickResult().getIntersectedNode();
                if (textFlow.getChildren().contains(node)){
                    int index = textFlow.getChildren().indexOf(node);
                    selectedText.select(textFlow.getChildren().get(index));
                }
            }
        });
        //textFlow.setOnMouseReleased();


        stage.setScene(scene);
        stage.show();
    }




    public void  setButtonPaste()
    {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        String content = clipboard.getContent(PLAIN_TEXT).toString();
        for (int i = 0; content.length() > i; i++)
        {
            Text text = new Text();
            text.setText(String.valueOf(content.charAt(i)));
            insert(text);
        }
    }

    public void changeChoice(boolean changeOn)
    {
        selectability = changeOn;
        selectedText.setSelectable(selectability);
    }


    public void keyPressed(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.RIGHT) rightKey();
        else if(keyEvent.getCode() == KeyCode.LEFT) leftKey();
        else if (keyEvent.getCode() == KeyCode.UP) upKey();
        else if (keyEvent.getCode() == KeyCode.DOWN) downKey();
        else if (keyEvent.getCode() == KeyCode.ENTER) addToTextFlow("\n");
        else if (keyEvent.getCode() == KeyCode.CAPS) return;
        else if (keyEvent.getCode() == KeyCode.CONTROL) return;
        else if (keyEvent.getCode() == KeyCode.SHIFT) return;
        else addToTextFlow(keyEvent.getText());

    }

    public void rightKey()
    {
        int caretIndex  = caret.caretIndex(textFlow);
        if (caretIndex != textFlow.getChildren().size() - 1) {
            Node caretNode = textFlow.getChildren().get(caretIndex);
            mouseCaretControl(caretNode, caretIndex, caretIndex + 1);
        }
    }
    public void leftKey()
    {
        int caretIndex = caret.caretIndex(textFlow);
        if (caretIndex > 0) {
            Node caret = textFlow.getChildren().get(caretIndex);
            method(caretIndex - 1, caret);
        }
    }

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
//                    System.out.println("nodeX " + nodeX);
//                    System.out.println("nodeYNew" + nodeYNew);
//                    System.out.println("bounds.getMinX()" + bounds.getMinX());
//                    System.out.println("bounds.getMaxX()" + bounds.getMaxX());
//                    System.out.println("bounds.getMinY()" + bounds.getMinY());
//                    System.out.println("bounds.getMaxY()" + bounds.getMaxY());
                    return textFlow.getChildren().get(i);
                }
            }
        }
        return null;
    }



    public void upKey(){
      double caretMaxX = caret.caretNode(textFlow).getBoundsInParent().getMaxX();
      double caretMaxY = caret.caretNode(textFlow).getBoundsInParent().getMaxY();
      double nodeHeight = caret.caretNode(textFlow).getBoundsInParent().getHeight();
      double caretYNew = caretMaxY - nodeHeight;
      upKeyGear(caretMaxX,caretMaxY,caretYNew);
    }
    public void upKeyGear(double nodeX, double nodeY, double nodeYNew){
        Node node = getNodeByCoordinates(nodeX,nodeY,nodeYNew);
        Node caretNode = textFlow.getChildren().get(caret.caretIndex(textFlow));
        if (node == null)
        {
            node = firstEnterBefore(textFlow.getChildren().indexOf(caretNode));
            if (node == null)return;
        }
        method(textFlow.getChildren().indexOf(node),caretNode);
    }
    public Node firstEnterBefore(int nodeIndex){
        for (int i = nodeIndex; i >= 0; i--){
            if (nodeToString(textFlow.getChildren().get(i)).equals("\n")) return textFlow.getChildren().get(i);
        }
        return null;
    }


    public void downKey(){
        double caretMaxX = caret.caretNode(textFlow).getBoundsInParent().getMaxX();
        double caretMaxY = caret.caretNode(textFlow).getBoundsInParent().getMaxY();
        double nodeHeight = caret.caretNode(textFlow).getBoundsInParent().getHeight();
        double caretYNew = caretMaxY + nodeHeight;
        downKeyGear(caretMaxX,caretMaxY,caretYNew);
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

            mouseCaretControl(caretNode,caret.caretIndex(textFlow),futureIndex);
    }
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




    public boolean inBounds(double nodeX, double nodeY, double boundsMinX, double boundsMaxX, double boundsMinY, double boundsMaxY){
        return boundsMinX <= nodeX && boundsMaxX >= nodeX && boundsMinY < nodeY && boundsMaxY >= nodeY;
    }


    public void insert(Text text){
        text.setStyle(style);
        int caretIndex = caret.caretIndex(textFlow);
        Node caret = textFlow.getChildren().get(caretIndex);
        textFlow.getChildren().set(caretIndex,text);
        method(caretIndex + 1 , caret);
    }
    //Index = Future caret index
    public void method(int index, Node caret){
        TextFlow tempTextFlow = new TextFlow();

        while (index != textFlow.getChildren().size()){
            tempTextFlow.getChildren().add(textFlow.getChildren().get(index));
        }
        textFlow.getChildren().add(caret);
        while (!tempTextFlow.getChildren().isEmpty()){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }
    }


    public void testingContains(MouseEvent mouseEvent)
    {
        Node chosenNode = mouseEvent.getPickResult().getIntersectedNode();
        int chosenNodeIndex = textFlow.getChildren().indexOf(chosenNode);
        int caretIndex  = caret.caretIndex(textFlow);
        Node caretNode = textFlow.getChildren().get(caretIndex);

        if (caretIndex < chosenNodeIndex) {
            mouseCaretControl(caretNode, caretIndex, chosenNodeIndex);
        }
        else method(chosenNodeIndex,caretNode);
        return;

    }


    public void mouseCaretControl(Node caretNode, int caretIndexBefore, int caretIndexAfter){
        TextFlow tempTextFlow = new TextFlow();
        caretIndexBefore += 1; // we don't need caret pos itself, we need first next element index




        //Coping text elements after caret position
        while (caretIndexBefore != textFlow.getChildren().size()){
            //System.out.println("caretIndexBefore :  " + textFlow.getChildren().get(caretIndexBefore).toString());
            tempTextFlow.getChildren().add(textFlow.getChildren().get(caretIndexBefore));
        }

        //deleting caret

        textFlow.getChildren().remove(caretNode);

        //Coping text before future caret position
        while (textFlow.getChildren().size() < caretIndexAfter )
        {
            //System.out.println("caretIndexAfter :  " + tempTextFlow.getChildren().get(0).toString());
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }

        //Adding Caret

        textFlow.getChildren().add(caretNode);

        //Adding the test og text

        while(!tempTextFlow.getChildren().isEmpty()){
            textFlow.getChildren().add(tempTextFlow.getChildren().get(0));
        }

    }

    public void addToTextFlow(String string){
        Text text = new Text(string);
        text.setStyle(style);
        text.setBoundsType(TextBoundsType.VISUAL);
        insert(text);
    }




    public String nodeToString(Node node){
        String nodeDate = node.toString();
        int startIndex = nodeDate.indexOf('"');
        int endIndex = nodeDate.lastIndexOf('"');

        return nodeDate.substring(startIndex + 1,endIndex);
    }


}