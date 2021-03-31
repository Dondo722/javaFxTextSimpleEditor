package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class SelectedText  {
    TextFlow textflow;
    Node caretNode;
    Node firstNode = null;
    Node secondNode = null;
    boolean selectable = false;
    public final ObservableList<Node> nodes = FXCollections.observableArrayList();

    SelectedText(TextFlow textFlow, Caret caret){
        this.textflow = textFlow;
        caretNode = caret.caretNode(textFlow);
    }
    public void setSelectable(boolean selectable){
        this.selectable = selectable;
        if(!selectable) removeAll();
    }
    public void select(Node node){
        if(!nodes.contains(node) && node != caretNode){
            if (firstNode != null) {
                secondNode = node;
                checkOther();
            }
            else {
                add(node);
                firstNode = node;
            }
        }
        else if(nodes.contains(node) && node != caretNode ){
            chopEnds(node);
        }
    }


    public void add(Node node){
        if (nodes.contains(node) || node == caretNode)return;
        ((Text)node).setFill(Color.GRAY);
        nodes.add(node);
    }
    public void add(int i,Node node){
        if (nodes.contains(node) || node == caretNode)return;
        ((Text)node).setFill(Color.GRAY);
        nodes.add(i,node);
    }

    public void remove(Node node){
        if (!nodes.contains(node))return;
        ((Text)node).setFill(Color.BLACK);
        nodes.remove(node);
    }
    public void remove(Node from, Node to){
        if(from == to) return;
        int fromId = nodes.indexOf(from);
        int toId = nodes.indexOf(to);
        while (fromId <= toId){
            if(nodes.get(fromId) != firstNode)
            remove(nodes.get(fromId));
            toId--;
        }
    }
    public void removeAll(){
        while (!nodes.isEmpty()) {
            ((Text)nodes.get(0)).setFill(Color.BLACK);
            nodes.remove(nodes.get(0));
        }
        firstNode = null;
        secondNode = null;
    }

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
                Node nodeFirstLeft = nodes.get(nodes.indexOf(firstNode) - 1); // First node that left before firstNode
                if(nodes.get(0) == nodeFirstLeft) remove(nodeFirstLeft);
                else remove(nodes.get(0),nodeFirstLeft);
            }
        }
        else if (firstNodeFlowIndex > secondNodeFlowIndex){
            addOtherLeft(firstNodeFlowIndex,secondNodeFlowIndex);

            if (firstNode != nodes.get(nodes.size()-1))
            {
                Node nodeFirstRight = nodes.get(nodes.indexOf(firstNode) + 1); // First node that right after firstNode
                if (nodes.get(nodes.size()-1) == nodeFirstRight) remove(nodeFirstRight);
                else remove(nodeFirstRight,nodes.get(nodes.size()-1));
            }

        }
    }


    public boolean isBelongs(Node from,Node node, Node to){
        return (nodes.indexOf(from) <= nodes.indexOf(node) && nodes.indexOf(node) <= nodes.indexOf(to));
    }

    public void chopRight(Node node){
        remove(node,nodes.get(nodes.size()-1));
    }


    public void chopLeft(Node node){
        remove(nodes.get(0), node);
    }


    public void chopEnds(Node node){

        if(node != nodes.get(0) || node != nodes.get(nodes.size()-1)){
            if (isBelongs(nodes.get(0),node,firstNode)){
                chopLeft(node);
            }
            else if (isBelongs(firstNode,node,nodes.get(nodes.size()-1))){
                chopRight(node);
            }
        }
    }
    public String toString(){
        String string = "";
        String tempString;
        for (Node node : nodes) {
            tempString = ((Text) node).getText();
            string += tempString;
        }
        return string;
    }
}




















