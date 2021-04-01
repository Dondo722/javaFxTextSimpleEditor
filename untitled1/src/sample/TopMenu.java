package sample;

import javafx.scene.layout.HBox;

public class TopMenu extends HBox {
    public TopMenu(WorkingArea workingArea, Toolbar toolbar){
        super();
        FileMenuBar fileMenuBar = new FileMenuBar(workingArea);
        ChangeMenuBar changeMenuBar = new ChangeMenuBar(toolbar);
        super.getChildren().addAll(fileMenuBar,changeMenuBar);
    }
}
