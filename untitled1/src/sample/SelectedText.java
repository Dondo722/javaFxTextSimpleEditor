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
    public void remove(int from, int to){
        for (int i = from; i < to; i++){
            remove(nodes.get(i));
            to--;
        }
    }
    public void remove(Node from, Node to){
        if(from == to) return;
        int fromId = nodes.indexOf(from);
        int toId = nodes.indexOf(to);
        while (fromId <= toId){
            remove(nodes.get(fromId));
            toId--;
        }
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
            if (firstNode != null) {
                secondNode = node;
                checkOther();
            }
            else {
                add(node);
                firstNode = node;
            }
        }
        else if(nodes.contains(node) && node != Caret.caretNode(textflow) && node != firstNode){
            chopEnds(node);
        }
    }
//change this shit
    public void addOtherRight(int firstToSecondNodeFlowIndex, int secondNodeFlowIndex){
        //nodes.remove(secondNode);
        for (int i = firstToSecondNodeFlowIndex; i <= secondNodeFlowIndex; i++) {
            add(textflow.getChildren().get(i));
        }
    }

    public void addOtherLeft(int firstNodeFlowIndex, int secondNodeFlowIndex)
    {
        //nodes.remove(secondNode);
        int nodesId = 0;
        for (int i = secondNodeFlowIndex; i < firstNodeFlowIndex; i++){
            add(nodesId,textflow.getChildren().get(i));
            nodesId++;
        }
    }

    public void checkOther(){
        int firstNodeFlowIndex = textflow.getChildren().indexOf(firstNode);
        int secondNodeFlowIndex = textflow.getChildren().indexOf(secondNode);

        if (firstNodeFlowIndex < secondNodeFlowIndex ){
            int firstToSecondNodeFlowIndex = textflow.getChildren().indexOf(nodes.get(nodes.size()-1));
            addOtherRight(firstToSecondNodeFlowIndex,secondNodeFlowIndex);
            if(firstNode != nodes.get(0)){
                Node nodeFirstLeft = nodes.get(nodes.indexOf(firstNode) -1); // First node that left before firstNode
                remove(nodes.get(0),nodeFirstLeft);
            }
        }
        else if (firstNodeFlowIndex > secondNodeFlowIndex){
            addOtherLeft(firstNodeFlowIndex,secondNodeFlowIndex);

            if (firstNode != nodes.get(nodes.size()-1))
            {
                Node nodeFirstRight = nodes.get(nodes.indexOf(firstNode) + 1); // First node that right after firstNode
                remove(nodeFirstRight,nodes.get(nodes.size()-1));
            }

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
            chopRight(node);
        }
    }
}




















