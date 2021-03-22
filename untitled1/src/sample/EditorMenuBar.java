package sample;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;

public class EditorMenuBar extends HBox {
    Menu fileMenu = new Menu("File");

    EditorMenuBar(){
        super();
        fileMenu.getItems().addAll(new MenuItem("Open"),new MenuItem("Save"));
        MenuBar menuBar = new MenuBar(){ public void requestFocus(){}};
        menuBar.getMenus().addAll(fileMenu);
        super.getChildren().addAll(menuBar);
    }
}
