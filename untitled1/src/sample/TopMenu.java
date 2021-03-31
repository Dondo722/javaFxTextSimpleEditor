package sample;

import javafx.scene.layout.HBox;

public class TopMenu extends HBox {

    WorkingArea workingArea;
    FileMenuBar fileMenuBar;
    ChangeMenuBar changeMenuBar;
    public TopMenu(WorkingArea workingArea, Toolbar toolbar){
        super();
        this.workingArea = workingArea;
        fileMenuBar = new FileMenuBar(workingArea);
        changeMenuBar = new ChangeMenuBar(workingArea,toolbar);
        super.getChildren().addAll(fileMenuBar,changeMenuBar);
    }
}
