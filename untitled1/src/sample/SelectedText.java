package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


public class SelectedText  {
    private final TextFlow textflow;
    private final Node caretNode;
    private Node firstNode = null;
    private Node secondNode = null;
    private final ObservableList<Node> nodes = FXCollections.observableArrayList();

    SelectedText(TextFlow textFlow, Caret caret){
        this.textflow = textFlow;
        caretNode = caret.caretNode(textFlow);
    }
    public void setSelectable(boolean selectable){
        if(!selectable) removeAll();
    }
    public void select(Node node){
        if(!nodes.contains(node) && node != caretNode){
            if (firstNode != null) {
                secondNode = node;
                addIntermediate();
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

    public void addRight(int firstToSecondNodeFlowIndex, int secondNodeFlowIndex){
        //nodes.remove(secondNode);
        for (int i = firstToSecondNodeFlowIndex; i <= secondNodeFlowIndex; i++) {
            add(textflow.getChildren().get(i));
        }
    }

    public void addLeft(int firstNodeFlowIndex, int secondNodeFlowIndex)
    {
        //nodes.remove(secondNode);
        int nodesId = 0;
        for (int i = secondNodeFlowIndex; i < firstNodeFlowIndex; i++){
            add(nodesId,textflow.getChildren().get(i));
            nodesId++;
        }
    }

    public void addIntermediate(){
        int firstNodeFlowIndex = textflow.getChildren().indexOf(firstNode);
        int secondNodeFlowIndex = textflow.getChildren().indexOf(secondNode);

        if (firstNodeFlowIndex < secondNodeFlowIndex ){
            int firstToSecondNodeFlowIndex = textflow.getChildren().indexOf(nodes.get(nodes.size()-1));
            addRight(firstToSecondNodeFlowIndex,secondNodeFlowIndex);
            if(firstNode != nodes.get(0)){
                Node nodeFirstLeft = nodes.get(nodes.indexOf(firstNode) - 1); // First node that left before firstNode
                if(nodes.get(0) == nodeFirstLeft) remove(nodeFirstLeft);
                else remove(nodes.get(0),nodeFirstLeft);
            }
        }
        else if (firstNodeFlowIndex > secondNodeFlowIndex){
            addLeft(firstNodeFlowIndex,secondNodeFlowIndex);

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

    public void chopEnds(Node node){

        if(node != nodes.get(0) || node != nodes.get(nodes.size()-1)){
            if (isBelongs(nodes.get(0),node,firstNode)){
                remove(nodes.get(0), node);
            }
            else if (isBelongs(firstNode,node,nodes.get(nodes.size()-1))){
                remove(node,nodes.get(nodes.size()-1));
            }
        }
    }
    public String toString(){
        String tempString;
        StringBuilder stringBuilder = new StringBuilder();
        for (Node node : nodes) {
            tempString = ((Text) node).getText();
            stringBuilder.append(tempString);
        }
        return stringBuilder.toString();
    }

    public ObservableList<Node> getNodes() {
        return nodes;
    }
}




















