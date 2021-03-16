package sample;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;

import javax.swing.*;
import java.util.List;

public class SelectedText  {
    TextFlow textflow;
    Node firstNode = null;
    Node secondNode = null;
    boolean selectable = false;
    public final ObservableList<Node> nodes = FXCollections.observableArrayList();


    SelectedText(TextFlow textFlow){
        this.textflow = textFlow;
    }
    public void setSelectable(boolean selectable){
        this.selectable = selectable;
        if(!selectable) removeAll();
    }


    public void add(Node node){
        if (nodes.contains(node) || node == Caret.caretNode(textflow))return;
        node.setStyle("-fx-font: 20 Times_New_Roman; -fx-fill: purple");
        nodes.add(node);
    }
    public void add(int i,Node node){
        if (nodes.contains(node) || node == Caret.caretNode(textflow))return;
        node.setStyle("-fx-font: 20 Times_New_Roman; -fx-fill: purple");
        nodes.add(i,node);
    }
    public void remove(Node node){
        if (!nodes.contains(node))return;
        node.setStyle("-fx-font: 20 Times_New_Roman;");
        nodes.remove(node);
    }
    public void removeAll(){
        while (!nodes.isEmpty()) {
            nodes.get(0).setStyle("-fx-font: 20 Times_New_Roman;");
            nodes.remove(nodes.get(0));
        }
        firstNode = null;
        secondNode = null;
    }

    public void select(Node node){
        if(!nodes.contains(node) && node != Caret.caretNode(textflow)){
            add(node);
            if (firstNode != null) {
                secondNode = node;
                checkOther();
            }
            else firstNode = node;
        }
        else if(nodes.contains(node) && node != Caret.caretNode(textflow) && node != firstNode){
            chopEnds(node);
        }
    }
//change this shit
    public void addOtherRight(int firstNodeFlowIndex, int nodeDifference){
        while (nodeDifference != 0){
            nodeDifference++;
            firstNodeFlowIndex++;
            add(textflow.getChildren().get(firstNodeFlowIndex));
        }
    }

    public void addOtherLeft(int firstNodeFlowIndex, int secondNodeFlowIndex)
    {
        nodes.remove(secondNode);
        int nodesId = 0;
        for (int i = secondNodeFlowIndex; i < firstNodeFlowIndex; i++){
            add(nodesId,textflow.getChildren().get(i));
            nodesId++;
        }
    }

    public void checkOther(){
        int firstNodeFlowIndex = textflow.getChildren().indexOf(firstNode);
        int secondNodeFlowIndex = textflow.getChildren().indexOf(secondNode);

        int nodeDifference = firstNodeFlowIndex - secondNodeFlowIndex;
        //SECOND NODE IS MORE
        if (nodeDifference < 0 ){
            addOtherRight(firstNodeFlowIndex,nodeDifference);
        }
        else {
            addOtherLeft(firstNodeFlowIndex,secondNodeFlowIndex);
        }
    }


    public boolean isBelongs(Node from,Node node, Node to){
        return (nodes.indexOf(from) <= nodes.indexOf(node) && nodes.indexOf(node) <= nodes.indexOf(to));
    }


    public void chopRight(Node node){
        int nodeIndex = nodes.indexOf(node);
        int size = nodes.size();
        for (int i = nodeIndex + 1; i < size; i++){
            remove(nodes.get(i));
            size = nodes.size();
        }
    }


    public void chopLeft(Node node){
    int nodeIndex = nodes.indexOf(node);
        for (int i = 0; i < nodeIndex; i++){
        remove(nodes.get(i));
        nodeIndex = nodes.indexOf(node);
    }
}



    public void chopEnds(Node node){

        if (isBelongs(nodes.get(0),node,firstNode)){
            chopLeft(node);
        }
        else if (isBelongs(firstNode,node,nodes.get(nodes.size()-1))){
            //chopRight(node);
        }
    }
}




















