package sample;



import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.text.TextFlow;

import java.util.List;


public class SelectedText  {
    TextFlow textflow;
    Node caretNode;
    Node firstNode = null;
    Node secondNode = null;
    boolean selectable = false;
    public final List<Node> nodes = FXCollections.observableArrayList();


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
            chopNear(node);
        }
    }


    public void add(Node node){
        if (nodes.contains(node) || node == caretNode)return;
        node.setStyle("-fx-font: 20 Times_New_Roman; -fx-fill: purple");
        nodes.add(node);
    }
    public void add(int i,Node node){
        if (nodes.contains(node) || node == caretNode)return;
        node.setStyle("-fx-font: 20 Times_New_Roman; -fx-fill: purple");
        nodes.add(i,node);
    }

    public void remove(Node node){
        if (!nodes.contains(node))return;
        node.setStyle("-fx-font: 20 Times_New_Roman;");
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
            nodes.get(0).setStyle("-fx-font: 20 Times_New_Roman;");
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



    //change chops in future

    public void chopRight(Node node){
//        Node tempNode;
//        if(node != nodes.get(nodes.size() - 1)) tempNode = nodes.get(nodes.indexOf(node) + 1); // first index left after node
//        else tempNode = node;
        remove(node,nodes.get(nodes.size()-1));

    }


    public void chopLeft(Node node){
//        Node tempNode;
//        if (node != nodes.get(0)) tempNode = nodes.get(nodes.indexOf(node) - 1); // first index right before node
//        else tempNode = node;
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

    public void chopNear(Node node){
//        if(nodes.size() <= 3 && node != nodes.get(0) || nodes.get(nodes)){
//            int firstNodeIndex = nodes.indexOf(node);
//            if (firstNodeIndex == 0) remove(nodes.get(1));
//            else remove(nodes.get(0));
//        }
    }
}




















